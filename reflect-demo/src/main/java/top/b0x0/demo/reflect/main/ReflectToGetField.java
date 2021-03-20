package top.b0x0.demo.reflect.main;

import top.b0x0.demo.reflect.domain.Student;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * 获取成员变量并调用：
 *
 * 1.批量的
 * 		1).Field[] getFields():获取所有的"公有字段"
 * 		2).Field[] getDeclaredFields():获取所有字段，包括：私有、受保护、默认、公有；
 * 2.获取单个的：
 * 		1).public Field getField(String fieldName):获取某个"公有的"字段；
 * 		2).public Field getDeclaredField(String fieldName):获取某个字段(可以是私有的)
 *
 * 	 设置字段的值：
 * 		Field --> public void set(Object obj,Object value):
 * 					参数说明：
 * 					1.obj:要设置的字段所在的对象；
 * 					2.value:要为字段设置的值；
 *
 * @author musui
 */
public class ReflectToGetField {
    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, ClassNotFoundException, NoSuchFieldException {

        //1.获取Class对象
        Class stuClass = Class.forName("top.b0x0.demo.reflect.domain.Student");

        //2.获取字段
        System.out.println("\n=============================获取所有公有的字段=============================");
        Field[] fieldArray = stuClass.getFields();
        for(Field field : fieldArray){
            System.out.println(field);
        }

        System.out.println("\n=============================获取所有的字段(包括私有、受保护、默认的)=============================");
        fieldArray = stuClass.getDeclaredFields();
        for(Field field : fieldArray){
            System.out.println(field);
        }

        System.out.println("\n=============================获取公有字段**并调用=============================");
        Field stuClassField = stuClass.getField("name");
        System.out.println(stuClassField);
        //产生Student对象--》Student stu = new Student();
        Object obj = stuClass.getConstructor().newInstance();
        // 为Student对象中的name属性赋值--》stu.name = "李四"
        stuClassField.set(obj, "李四");
        //验证
        Student stu = (Student)obj;
        System.out.println("验证姓名：" + stu.name);


        System.out.println("\n=============================获取私有字段****并调用=============================");
        stuClassField = stuClass.getDeclaredField("age");
        System.out.println(stuClassField);
        //暴力反射，忽略修饰符
        stuClassField.setAccessible(true);
        stuClassField.set(obj, 25);

    }
}