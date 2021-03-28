package top.b0x0.demo.reflect.domain;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author musui
 */
@Data
@Accessors(chain = true)
public class Student {
    public String name;
    private Integer age;
    private String avatarUrl;

    /**
     * =================成员方法===============================
     */
    public Student() {
        System.out.println("public无参构造方法执行了。。。");
    }

    public Student(String name, Integer age, String avatarUrl) {
        this.name = name;
        this.age = age;
        this.avatarUrl = avatarUrl;
        System.out.println("public全参构造方法执行了。。。");
    }

    private Student(Integer age, String avatarUrl) {
        this.age = age;
        this.avatarUrl = avatarUrl;
        System.out.println("private构造方法执行了。。。");
    }

    protected Student(Integer age) {
        this.age = age;
        System.out.println("protected构造方法执行了。。。");
    }


    /**
     * *************成员方法***************
     */
    public void publicShow(String s) {
        System.out.println("调用了：公有的，String参数的show1(): s = " + s);
    }

    protected void protectedShow() {
        System.out.println("调用了：受保护的，无参的show2()");
    }

    void defaultShow() {
        System.out.println("调用了：默认的，无参的show3()");
    }

    private String privateShow(int age) {
        System.out.println("调用了，私有的，并且有返回值的，int参数的show4(): age = " + age);
        return "abcd";
    }

}
