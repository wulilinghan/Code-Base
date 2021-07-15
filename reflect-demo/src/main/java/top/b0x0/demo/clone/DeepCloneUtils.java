package top.b0x0.demo.clone;

import top.b0x0.demo.reflect.domain.Student;

import java.io.*;

/**
 * 深拷贝工具类
 *
 * @author TANG
 * @version 1.0.0
 * @date 2021-07-15
 * @since jdk1.8
 */
public class DeepCloneUtils {

    /**
     * 使用序列化实现深拷贝
     *
     * @param obj /
     * @return /
     * @throws IOException            /
     * @throws ClassNotFoundException /
     */
    public Object clone(Object obj) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(obj);
        oos.flush();
        ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
        return ois.readObject();
    }
}
