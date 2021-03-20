package top.b0x0.demo.mybatis;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import top.b0x0.demo.mybatis.bulkInsert.mysql.BulkInsert;
import top.b0x0.demo.mybatis.util.BigdbProperties;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * 批量插入数据测试
 *
 * @author TANG
 * @date 2021-02-14
 */
@SpringBootTest
public class BulkInsertTest {
    private static final Logger log = LoggerFactory.getLogger(BulkInsertTest.class);

    @Test
    public void test1() throws ClassNotFoundException, SQLException {
        String url = "jdbc:mysql://127.0.0.1:3306/bigdb?serverTimezone=Asia/Shanghai&characterEncoding=utf8&useSSL=false";
        String driveName = "com.mysql.cj.jdbc.Driver";
        String user = "root";
        String password = "root@123";
        //jdbc.driver
        //jdbc.url
        //jdbc.username
        //jdbc.password
        Properties bigdbProperties = BigdbProperties.getBigdbProperties();
        url = bigdbProperties.getProperty("jdbc.url");
        driveName = bigdbProperties.getProperty("jdbc.driver");
        user = bigdbProperties.getProperty("jdbc.username");
        password = bigdbProperties.getProperty("jdbc.password");
        Connection conn = null;
        // 指定连接类型
        Class.forName(driveName);
        // 获取连接
        conn = DriverManager.getConnection(url, user, password);
        if (conn != null) {
            log.info("-------> 获取连接成功");
            BulkInsert.insert(conn);
        } else {
            log.info("-------> 获取连接失败");
        }
    }
}
