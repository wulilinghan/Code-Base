package top.b0x0.demo.infrastructure.mybatis.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import top.b0x0.demo.infrastructure.mybatis.bulkInsert.mysql.BulkInsert;

import java.util.Properties;

/**
 * @author TANG
 * @date 2021-02-14
 */
public class BigdbProperties {

    private static final Logger log = LoggerFactory.getLogger(BigdbProperties.class);

    private static final Properties BIGDB_PROPERTIES = new Properties();

    static {
        try {
            BIGDB_PROPERTIES.load(BulkInsert.class.getClassLoader().getResourceAsStream("bigdb.properties"));
            log.info("--------->> BIG_DB_PROPERTIES : {}", BIGDB_PROPERTIES.toString());
        } catch (Exception e) {
            log.warn("Load Properties error : {}", e.getMessage());
        }
    }

    public static Properties getBigdbProperties() {
        return BIGDB_PROPERTIES;
    }
}
