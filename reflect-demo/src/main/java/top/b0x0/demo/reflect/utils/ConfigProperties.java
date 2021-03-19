package top.b0x0.demo.reflect.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


/**
 * 读取properties文件
 *
 * @author musui
 */
public class ConfigProperties {
    private static final Logger log = LoggerFactory.getLogger(ConfigProperties.class);

    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(ConfigProperties.class.getClassLoader().getResourceAsStream("config.properties"));
        } catch (IOException e) {
            log.warn("Load Properties Ex", e);
        }
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }
}