package top.b0x0.demo.infrastructure.reflect.main;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


/**
 * 通过Class对象可以获取某个类中的：构造方法、成员变量、成员方法；并访问成员；
 *
 * 1.获取构造方法：
 * 		1).批量的方法：
 * 			public Constructor[] getConstructors()：所有"公有的"构造方法
 public Constructor[] getDeclaredConstructors()：获取所有的构造方法(包括私有、受保护、默认、公有)

 * 		2).获取单个的方法，并调用：
 * 			public Constructor getConstructor(Class... parameterTypes):获取单个的"公有的"构造方法：
 * 			public Constructor getDeclaredConstructor(Class... parameterTypes):获取"某个构造方法"可以是私有的，或受保护、默认、公有；
 *
 * 			调用构造方法：
 * 			Constructor-->newInstance(Object... initargs)
 *
 * @author musui
 */
public class ReflectToGetConstructor {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException {

        //1.加载Class对象
        Class stuClazz = Class.forName("top.b0x0.demo.infrastructure.reflect.domain.Student");


        //2.获取所有公有构造方法
        System.out.println("=============================所有公有构造方法=============================");
        Constructor[] conArray = stuClazz.getConstructors();
        for (Constructor constructor : conArray) {
            System.out.println(constructor);
        }


        System.out.println("\n=============================所有的构造方法(包括：私有、受保护、默认、公有)=============================");
        conArray = stuClazz.getDeclaredConstructors();
        for (Constructor constructor : conArray) {
            System.out.println(constructor);
        }

        System.out.println("\n=============================获取公有、无参的构造方法=============================");
        //1>、因为是无参的构造方法所以类型是一个null,不写也可以：这里需要的是一个参数的类型，切记是类型
        //2>、返回的是描述这个无参构造函数的类对象。
        Constructor con = stuClazz.getConstructor(null);
        System.out.println("con = " + con);

        //调用无参构造方法
        Object obj = con.newInstance();
        System.out.println("obj = " + obj);
        //	Student stu = (Student)obj;

        System.out.println("\n=============================获取私有构造方法，并调用=============================");
        con = stuClazz.getDeclaredConstructor(Integer.class, String.class);
        System.out.println(con);
        //暴力访问(忽略掉访问修饰符)
        con.setAccessible(true);
        //调用构造方法
        obj = con.newInstance(16, "c763e8191645441a914b1bf2fe602c12_shop1612358526684.png");
        System.out.println("obj = " + obj);
    }
}