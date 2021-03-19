package top.b0x0.demo.reflect.domain;

import lombok.Data;

import java.util.List;

/**
 * @author musui
 */
@Data
public class Clazz {
    private String clazzName;
    private String clazzAvatarUrl;

    private List<Student> studentList;
}
