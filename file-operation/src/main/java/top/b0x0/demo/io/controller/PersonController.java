package top.b0x0.demo.io.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.io.entity.Person;
import top.b0x0.demo.io.service.PersonService;

import javax.annotation.Resource;

/**
 * (Person)表控制层
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
@RestController
@RequestMapping("person")
public class PersonController {

    @Resource
    private PersonService personService;

    @GetMapping
    public ResponseEntity<Page<Person>> queryByPage(Person person, PageRequest pageRequest) {
        return ResponseEntity.ok(this.personService.queryByPage(person, pageRequest));
    }

    @GetMapping("{id}")
    public ResponseEntity<Person> queryById(@PathVariable("id") String id) {
        return ResponseEntity.ok(this.personService.queryById(id));
    }


}

