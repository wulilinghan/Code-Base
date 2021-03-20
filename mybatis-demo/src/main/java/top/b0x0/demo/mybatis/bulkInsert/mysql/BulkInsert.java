package top.b0x0.demo.mybatis.bulkInsert.mysql;

import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.b0x0.demo.mybatis.util.BigdbProperties;
import top.b0x0.demo.mybatis.util.RandomUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

/**
 * 往mysql插入一千万条数据.
 *
 * @author TANG
 * @date 2019/12/08
 */
public class BulkInsert {
    private static final Logger log = LoggerFactory.getLogger(BulkInsert.class);

    private static List<String> nameList = new ArrayList<>();
    private static List<String> sexList = new ArrayList<>();
    private static List<String> addrList = new ArrayList<>();
    // INSERT INTO `bigdb`.`person`(`id`, `username`, `formername`, `age`, `sex`, `address`, `url_frontOfIDCard`, `url_backOfIDCard`, `birthday`, `create_time`) VALUES ('1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);

    static {

        nameList.add("张三");
        nameList.add("李四");
        nameList.add("王五");
        nameList.add("小六");
        nameList.add("唐僧");
        nameList.add("猪八戒");
        nameList.add("沙僧");
        nameList.add("白龙马");
        nameList.add("孙悟空");
        nameList.add("六耳猕猴");

        sexList.add("1");
        sexList.add("0");

        addrList.add("中国广东省深圳市罗湖区松园西街");
        addrList.add("中国广东省深圳市罗湖区红桂路1002号-14");
        addrList.add("中国广东省深圳市罗湖区蔡屋围红宝路");
        addrList.add("中国广东省深圳市罗湖区桂园路28号");
        addrList.add("中国广东省深圳市罗湖区蔡屋围宝安南路2095号");
        addrList.add("中国广东省深圳市罗湖区蔡屋围深南东路5016号");
    }

    /**
     * .往mysql插数据
     *
     * @param conn /
     */
    public static void insert(Connection conn) {
        // 开始时间
        Long begin = System.currentTimeMillis();
        // sql前缀
        final String prefix = "INSERT INTO `person`(`id`, `username`, `formername`, `age`, `sex`, `address`, `url_frontOfIDCard`, `url_backOfIDCard`, `birthday`, `create_time`) VALUES ";
        try {
            // 保存sql后缀
            StringBuffer suffix;
            // 设置事务为非自动提交
            conn.setAutoCommit(false);
            // 比起st，pst会更好些
            // 准备执行语句
            PreparedStatement pst = conn.prepareStatement("select 1");
            // 外层循环，总提交事务次数,插十万条数据提交一次事务
            for (int i = 1; i <= 100; i++) {
                suffix = new StringBuffer();
                // 第j次提交步长
                for (int j = 1; j <= 100000; j++) {
                    Random random = new Random();
                    // 随机生成0~100的数 (B - A )+A
                    int age = random.nextInt(100);
                    Snowflake snowflake = IdUtil.getSnowflake(1, 1);
                    long id = snowflake.nextId();
                    List<String> randomSex = RandomUtils.randomChooseElements(sexList, 1);
                    String sex = randomSex.get(0);
                    List<String> randomUsername = RandomUtils.randomChooseElements(nameList, 1);
                    String username = randomUsername.get(0);
                    String formername = username + "old";
                    List<String> randomAddr = RandomUtils.randomChooseElements(addrList, 1);
                    String address = randomAddr.get(0);

                    // 构建SQL后缀
                    suffix.append("('")
                            .append(id).append("','")
                            .append(username).append("','")
                            .append(formername).append("','")
                            .append(age).append("','")
                            .append(sex).append("','")
                            .append(address).append("','")
                            .append(id + "url_frontOfIDCard.png").append("','")
                            .append(id + "url_backOfIDCard.png").append("','")
                            .append(LocalDateTime.now()).append("','")
                            .append(LocalDateTime.now()).append("'),");
                    // INSERT INTO `person`(`id`, `username`, `formername`, `age`, `sex`, `address`, `url_frontOfIDCard`, `url_backOfIDCard`, `birthday`, `create_time`) VALUES ('1', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL);
                    if (i == 1 && j == 3) {
                        String tempSql = prefix + suffix.substring(0, suffix.length() - 1);
                        log.info("---------> tempSql : {}", tempSql);
                    }
                }
                // INSERT INTO person (id,username,age,sex) VALUES ('1360876997911384064','孙悟空','91','0'),('1360876997911384065','张三','64','1'),('1360876997911384066','沙僧','62','1')
                // 构建完整SQL
                String sql = prefix + suffix.substring(0, suffix.length() - 1);
                // 添加执行SQL
                pst.addBatch(sql);
                // 执行操作
                pst.executeBatch();
                // 提交事务
                conn.commit();
                // 清空上一次添加的数据
                suffix = new StringBuffer();
            }
            // 头等连接
            pst.close();
            conn.close();
        } catch (Exception e) {
            log.error("bulk insert error : {}", e.getMessage());
        }
        // 结束时间
        Long end = System.currentTimeMillis();
        // 耗时
        log.info("---------> 1000万条数据插入花费时间 : " + (end - begin) / 1000 + " s");
    }

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/bigdb?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false";
        String driveName = "com.mysql.cj.jdbc.Driver";
        String user = "root";
        String password = "root@123";
        //jdbc.driver
        //jdbc.url
        //jdbc.username
        //jdbc.password
        Properties bigdbProperties = BigdbProperties.getBigdbProperties();
        url = bigdbProperties.getProperty("jdbc.url");
        driveName = bigdbProperties.getProperty("jdbc.driver");
        user = bigdbProperties.getProperty("jdbc.username");
        password = bigdbProperties.getProperty("jdbc.password");
        Connection conn = null;
        // 指定连接类型
        Class.forName(driveName);
        // 获取连接
        conn = DriverManager.getConnection(url, user, password);
        if (conn != null) {
            log.info("-------> 获取连接成功");
            insert(conn);
        } else {
            log.info("-------> 获取连接失败");
        }
    }
}
