package top.b0x0.demo.io.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;
import top.b0x0.demo.io.entity.Person;

import java.util.List;

/**
 * (Person)表数据库访问层
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
public interface PersonDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    Person queryById(String id);

    /**
     * 查询指定行数据
     *
     * @param person   查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<Person> queryAllByLimit(@Param("p") Person person, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param person 查询条件
     * @return 总行数
     */
    long count(Person person);


}

