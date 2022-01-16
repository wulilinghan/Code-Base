package top.b0x0.demo.io.test;

import org.junit.Test;
import org.springframework.data.domain.PageRequest;
import top.b0x0.demo.io.BaseTest;
import top.b0x0.demo.io.dao.PersonDao;
import top.b0x0.demo.io.entity.Person;

import javax.annotation.Resource;
import java.util.List;

public class MapperTest extends BaseTest {

    @Resource
    PersonDao personDao;

    @Test
    public void t_queryById() {
        Person person = personDao.queryById("1360895586659864580");
        System.out.println("person = " + person);
    }

    @Test
    public void t_queryAllByLimit() {

        PageRequest pageRequest = PageRequest.of(1, 1000000);
        List<Person> personList = personDao.queryAllByLimit(new Person(), pageRequest);
        int size = personList.size();
        System.out.println("size = " + size);
    }
}
