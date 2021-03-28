package top.b0x0.demo.clone;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author musui
 */
@Data
@Accessors(chain = true)
public class ShallowCloneObj implements Cloneable {
    private String name;
    private Integer age;
    private Integer[] hobby;

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
    protected ShallowCloneObj clone() throws CloneNotSupportedException {
        // 浅拷贝
        return (ShallowCloneObj) super.clone();
    }
}