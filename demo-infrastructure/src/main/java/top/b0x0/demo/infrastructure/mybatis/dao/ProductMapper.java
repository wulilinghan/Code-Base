package top.b0x0.demo.infrastructure.mybatis.dao;


import top.b0x0.demo.infrastructure.mybatis.domain.Product;

import java.util.List;

/**
 * Created on 2020-10-29
 */
public interface ProductMapper {
    // 根据id查询商品信息
    Product find(long id);
    // 根据名称搜索商品信息
    List<Product> findByName(String name);
    // 保存商品信息
    long save(Product product);
}