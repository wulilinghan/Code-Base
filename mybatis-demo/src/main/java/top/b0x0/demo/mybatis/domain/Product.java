package top.b0x0.demo.mybatis.domain;

import lombok.Data;

import java.math.BigDecimal;

/**
 * Created on 2020-10-29
 *
 * @author musui
 */
@Data
public class Product {
    private long id;
    private String name;
    private String description;
    private BigDecimal price;

}