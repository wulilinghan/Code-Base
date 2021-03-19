package top.b0x0.demo.reflect.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.b0x0.demo.reflect.domain.Clazz;
import top.b0x0.demo.reflect.domain.Student;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author musui
 */
public class ProcessUtils {

    private static final Logger log = LoggerFactory.getLogger(ProcessUtils.class);

    public static final String URL_ = "url";

    /**
     * 修改对象中的特定字段的值
     *
     * @param object /
     */
    public static void processPhoto(Object object) {
        if (object == null) {
            return;
        }
        try {
            Field[] fields = object.getClass().getDeclaredFields();
            for (Field field : fields) {
                coreProcessing(object, field);
                // 判断属性是否是List类型
                if (field.getType() == java.util.List.class) {
                    // 获取到list的size方法
                    Method m = List.class.getDeclaredMethod("size");
                    //调用list的size方法，得到list的长度
                    int size = (Integer) m.invoke(field.get(object));

                    Type type = field.getGenericType();
                    // ParameterizedType 泛型实例,带有<>的参数, 例如 List<Student>
                    if (type instanceof ParameterizedType) {
                        ParameterizedType pt = (ParameterizedType) type;
                        //得到对象list中实例的类型
                        Class clz = (Class) pt.getActualTypeArguments()[0];
                        System.out.println("clz = " + clz);
                        //遍历list，调用get方法，获取list中的对象实例
                        for (int i = 0; i < size; i++) {
                            // 获取list的get方法
                            Method getMethod = List.class.getDeclaredMethod("get", int.class);
                            if (!field.isAccessible()) {
                                field.setAccessible(true);
                            }
                            // 获取到list中 第 i 个的对象
                            Object listObj = getMethod.invoke(field.get(object), i);
                            Field[] listObjFields = listObj.getClass().getDeclaredFields();
                            for (Field objField : listObjFields) {
                                coreProcessing(listObj, objField);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("处理图片异常: {}", e.getMessage());
        }
    }

    private static void coreProcessing(Object obj, Field field) throws IllegalAccessException {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        Object fieldValue = field.get(obj);
        // 图片处理
        if (field.getName().toLowerCase().contains(URL_) && StringUtils.isNotBlank(fieldValue.toString())) {
            URL url = OSSUrlTime.urlTime(String.valueOf(fieldValue));
            field.set(obj, String.valueOf(url));
        }
    }

    public static void main(String[] args) {
        Student student = new Student();
        student.setName("张三");
        student.setAge(15);
        student.setAvatarUrl("c763e8191645441a914b1bf2fe602c12_shop1612358526684.png");

        Student student2 = new Student();
        student2.setName("李四");
        student2.setAge(20);
        student2.setAvatarUrl("c763e8191645441a914b1bf2fe602c12_shop1612358526684.png");

        List<Student> studentList = new ArrayList<>();
        studentList.add(student);
        studentList.add(student2);

        Clazz clazz = new Clazz();
        clazz.setClazzName("A班");
        clazz.setClazzAvatarUrl("c763e8191645441a914b1bf2fe602c12_shop1612358526684.png");
        clazz.setStudentList(studentList);


//        Field[] fields = clazz.getClass().getDeclaredFields();
//        Field field1 = fields[1];
//        field1.setAccessible(true);
//        Class<?> type = field1.getType();
//        String name = type.getName();
//        System.out.println("name = " + name);

        processPhoto(clazz);

        System.out.println("clazz = " + clazz);
    }
}
