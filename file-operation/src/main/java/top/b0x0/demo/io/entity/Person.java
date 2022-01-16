package top.b0x0.demo.io.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * (Person)实体类
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {
    private static final long serialVersionUID = -58339070754787685L;

    private String id;

    private String username;

    private String formername;

    private String age;

    private String sex;

    private String address;

    private String urlFrontofidcard;

    private String urlBackofidcard;

    private String birthday;

    private Date createTime;

}

