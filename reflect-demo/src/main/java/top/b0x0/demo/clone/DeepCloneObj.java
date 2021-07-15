package top.b0x0.demo.clone;

import lombok.SneakyThrows;
import top.b0x0.demo.reflect.domain.Student;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Arrays;

/**
 * @author musui
 */
public class DeepCloneObj implements Cloneable {
    private String name;
    private Integer age;
    private Integer[] hobby;

    public DeepCloneObj() {
        name = "李四";
        age = 18;
        hobby = new Integer[3];
        for (int i = 0; i < hobby.length; i++) {
            hobby[i] = i;
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer[] getHobby() {
        return hobby;
    }

    public Integer getHobby(Integer index) {
        return hobby[index];
    }

    public void setHobby(Integer[] hobby) {
        this.hobby = hobby;
    }

    public void setHobby(Integer index, Integer value) {
        this.hobby[index] = value;
    }

    @Override
    public String toString() {
        return "DeepCloneObj{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", hobby=" + Arrays.toString(hobby) +
                '}';
    }

    /**
     * 深拷贝
     * 方式一: {@link DeepCloneUtils}
     * 方式二: 如下:手动实现数据填充
     *
     * @return /
     * @throws CloneNotSupportedException /
     */
    @Override
    protected DeepCloneObj clone() throws CloneNotSupportedException {
        // 深拷贝
        DeepCloneObj obj = (DeepCloneObj) super.clone();
        obj.hobby = new Integer[hobby.length];
        System.arraycopy(hobby, 0, obj.hobby, 0, hobby.length);
        return obj;
    }

}