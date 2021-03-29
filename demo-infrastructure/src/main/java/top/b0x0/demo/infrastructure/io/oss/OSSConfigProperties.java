package top.b0x0.demo.infrastructure.io.oss;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Properties;


/**
 * @author musui
 */
public class OSSConfigProperties {

    private static final Logger logger = LoggerFactory.getLogger(OSSConfigProperties.class);
    private static final Properties PROPERTIES = new Properties();

    static {
        try {
            PROPERTIES.load(OSSConfigProperties.class.getClassLoader().getResourceAsStream("oss-config.properties"));
        } catch (IOException e) {
            logger.warn("Load Properties Ex", e);
        }
    }

    public static Properties getProperties() {
        return PROPERTIES;
    }
}
