package top.b0x0.demo.io.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import top.b0x0.demo.io.dao.PersonDao;
import top.b0x0.demo.io.entity.Person;
import top.b0x0.demo.io.service.PersonService;

import javax.annotation.Resource;

/**
 * (Person)表服务实现类
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
@Service("personService")
public class PersonServiceImpl implements PersonService {
    @Resource
    private PersonDao personDao;


    @Override
    public Person queryById(String id) {
        return this.personDao.queryById(id);
    }

    @Override
    public Page<Person> queryByPage(Person person, PageRequest pageRequest) {
        long total = this.personDao.count(person);
        return new PageImpl<>(this.personDao.queryAllByLimit(person, pageRequest), pageRequest, total);
    }

    @Override
    public long countAll() {
        return personDao.count(null);
    }

}
