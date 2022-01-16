package top.b0x0.demo.io.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import top.b0x0.demo.io.entity.Person;

/**
 * (Person)表服务接口
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
public interface PersonService {

    Person queryById(String id);

    long countAll();


    Page<Person> queryByPage(Person person, PageRequest pageRequest);


}
