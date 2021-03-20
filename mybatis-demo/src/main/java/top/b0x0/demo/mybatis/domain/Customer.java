package top.b0x0.demo.mybatis.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2020-10-29
 *
 * @author musui
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Customer {

    private long id;

    private String name;

    private String phone;

    private List<Address> addresses = new ArrayList<>();


}