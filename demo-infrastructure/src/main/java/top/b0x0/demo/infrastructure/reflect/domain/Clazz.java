package top.b0x0.demo.infrastructure.reflect.domain;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @author musui
 */
@Data
@Accessors(chain = true)
public class Clazz {
    private String clazzName;
    private String clazzAvatarUrl;

    private List<Student> studentList;
}
