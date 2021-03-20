package top.b0x0.demo.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.b0x0.demo.mybatis.dao.CustomerMapper;
import top.b0x0.demo.mybatis.domain.Customer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * mybatis 一级和二级缓存 测试
 * 参考文档:
 * https://tech.meituan.com/2018/01/19/mybatis-cache.html
 * https://blog.csdn.net/tengliu6/article/details/79189551
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MybatisCacheTest {

    private static SqlSessionFactory factory;

    static {
        String resource = "mybatis-config.xml";
        InputStream inputStream = null;
        try {
            inputStream = Resources.getResourceAsStream(resource);
        } catch (IOException e) {
            System.err.println("read mybatis-config.xml fail");
            e.printStackTrace();
            System.exit(1);
        }
        // 加载mybatis-config.xml配置文件，并创建SqlSessionFactory对象
        factory = new SqlSessionFactoryBuilder().build(inputStream);
    }


    /**
     * 一级缓存 测试 1
     * 连续用同一个sqlSession查询三次, 发现只打印了一次SQL, 证明第二三次查询的缓存
     */
    @Test
    public void level1CacheTest() {
        // 自动提交事务
        SqlSession sqlSession = factory.openSession(true);
        CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
        System.out.println("第一次查询: " + customerMapper.find(2));
        System.out.println("第二次查询: " + customerMapper.find(2));
        System.out.println("第二次查询: " + customerMapper.find(2));
        sqlSession.close();
    }

    /**
     * 一级缓存 测试 2
     * 用同一个sqlSession查询二次后, 做一次新增操作, 然后再查询一次, 发现第二次查询没有打印SQL,其他两次都打印了SQL(都是查询的数据库),
     * 说明中间的新增操作清空了缓存
     */
    @Test
    public void level1CacheTest2() {
        // 自动提交事务
        SqlSession sqlSession = factory.openSession(true);

        CustomerMapper customerMapper = sqlSession.getMapper(CustomerMapper.class);
        System.out.println("第一次查询: " + customerMapper.find(2));
        System.out.println("第二次查询: " + customerMapper.find(2));
        System.out.println("增加了" + customerMapper.save(new Customer(3L, "李四", "15688888888", new ArrayList<>())) + "个学生");
        System.out.println("第三次查询: " + customerMapper.find(2));
        sqlSession.close();
    }

    /**
     * 一级缓存 测试 3
     * 操作: 开启两个SqlSession，在sqlSession1中查询数据，使一级缓存生效，在sqlSession2中更新数据库
     * <p>
     * 结果: sqlSession2更新了id为2的客户的姓名，从李四改为了李四222，但session1之后的查询中，id为2的客户的名字还是李四，出现了脏数据，说明一级缓存只在 [数据库会话内部共享] 。
     */
    @Test
    public void level1CacheTest3() {
        SqlSession sqlSession1 = factory.openSession(true);
        SqlSession sqlSession2 = factory.openSession(true);

        CustomerMapper customerMapper1 = sqlSession1.getMapper(CustomerMapper.class);
        CustomerMapper customerMapper2 = sqlSession2.getMapper(CustomerMapper.class);

        System.out.println("customerMapper1第一次查询: " + customerMapper1.find(3));
        System.out.println("customerMapper2第一次查询: " + customerMapper2.find(3));

        System.out.println("customerMapper2第一次更新: " + customerMapper2.update(new Customer(3L, "李四222", "15688888888", new ArrayList<>())) + "个学生");
        System.out.println("customerMapper1第二次查询: " + customerMapper1.find(3));
        System.out.println("customerMapper2第二次查询: " + customerMapper2.find(3));
        sqlSession1.close();
        sqlSession2.close();
    }

    /*
     * 总结
     * 1. MyBatis一级缓存的生命周期和SqlSession一致。
     * 2. MyBatis一级缓存内部设计简单，只是一个没有容量限定的HashMap，在缓存的功能性上有所欠缺。
     * 3. MyBatis的一级缓存最大范围是SqlSession内部，有多个SqlSession或者分布式的环境下，数据库写操作会引起脏数据，建议设定缓存级别为Statement。
     */

}
