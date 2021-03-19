package top.b0x0.demo.reflect.utils;

import cn.hutool.core.io.FileUtil;
import com.aliyun.oss.HttpMethod;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.GeneratePresignedUrlRequest;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.OSSObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @author musui
 */
public class OSSUrlTime {
    private static final Logger log = LoggerFactory.getLogger(OSSUrlTime.class);

    private static final String SPRING_ACTIVE_PRD = "prd";
    private static final String SPRING_ACTIVE_TEST = "test";
    private static final String SPRING_ACTIVE_DEV = "dev";

    public static final String OSS_HTTP_PREFIX = "http://";
    public static final String OSS_HTTPS_PREFIX = "https://";
    public static final String OSS_SUFFIX = "oss-cn-beijing.aliyuncs.com";
    public static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    public static final String ACCESS_KEY_ID = "GtAI8FghmHujkYiU8";
    public static final String ACCESS_KEY_SECRET = "lki9ver3fLWIPmL7pOvxMfFNJjtnhM";
    public static final String BUCKET_NAME;

    static String active;

    static {
        Properties properties = ConfigProperties.getProperties();
        active = properties.getProperty("spring.profile.active");
        //开发、测试环境与生产环境 bucket区分开
        if (SPRING_ACTIVE_PRD.equals(active)) {
            BUCKET_NAME = "prd-top-b0x0";
        } else if (SPRING_ACTIVE_TEST.equals(active)) {
            BUCKET_NAME = "test-top-b0x0";
        } else {
            BUCKET_NAME = "dev-top-b0x0";
        }
    }

    /**
     * 生成有时效性的链接
     *
     * @param objectName 文件名 xxx.png
     * @return usr  eg: http://dev-bfp-image.oss-cn-beijing.aliyuncs.com/9ea735c6ebc24d9e94967933e0b6f781_shop1614147412933.png?Expires=1614222661&OSSAccessKeyId=LTAI85nOmyS7z2P8&Signature=5TCHhtjbTdu2Kzch4kLw5LWZjyQ%3D
     */
    public static URL urlTime(String objectName) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        // 设置URL过期时间为1小时。
        Date expiration = new Date(System.currentTimeMillis() + 300 * 1000);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。
        URL url = ossClient.generatePresignedUrl(BUCKET_NAME, objectName, expiration);
        ossClient.shutdown();
        return url;
    }

    /**
     * 生成永久访问地址
     *
     * @param objectName 文件名 xxx.png
     * @return url string  eg: http://dev-bfp-image.oss-cn-beijing.aliyuncs.com/9ea735c6ebc24d9e94967933e0b6f781_shop1614147412933.png
     */
    public static String url(String objectName) {
        return OSS_HTTP_PREFIX + BUCKET_NAME + "." + OSS_SUFFIX + "/" + objectName;
    }

    public static void downloadFile(String objectName, String fileDir) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建
        ossClient.getObject(new GetObjectRequest(BUCKET_NAME, objectName), new File(fileDir));
        ossClient.shutdown();
    }

    public static Map<String, File> downloadFiles(Map<String, String> objectNameMap, String tempParentDir) {
        // 创建OSSClient实例。
        OSSClient ossClient = new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建
        Map<String, File> fileMap = new HashMap<>();
        for (String key : objectNameMap.keySet()) {
            String filePath = tempParentDir + "/" + key + ".jpg";
            File file = new File(filePath);
            ossClient.getObject(new GetObjectRequest(BUCKET_NAME, objectNameMap.get(key)), file);
            fileMap.put(key, file);
        }
        ossClient.shutdown();
        return fileMap;
    }

    /**
     * 上传文件流
     *
     * @param objectNameMap /
     * @param tempParentDir /
     * @return /
     */
    public static Map<String, File> updateFiles(Map<String, String> objectNameMap, String tempParentDir) {
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
        // 下载OSS文件到本地文件。如果指定的本地文件存在会覆盖，不存在则新建
        Map<String, File> fileMap = new HashMap<>();
        for (String key : objectNameMap.keySet()) {
            String filePath = tempParentDir + "/" + key + ".jpg";
            File file = new File(filePath);
            ossClient.getObject(new GetObjectRequest(BUCKET_NAME, objectNameMap.get(key)), file);
            fileMap.put(key, file);
        }
        ossClient.shutdown();
        return fileMap;
    }

    /**
     * 构建OSSClient示例
     *
     * @return OSSClient
     */
    public static OSSClient getOssInstance() {
        return new OSSClient(ENDPOINT, ACCESS_KEY_ID, ACCESS_KEY_SECRET);
    }

    /**
     * 根据文件名获取文件存储对象 OSSObject
     *
     * @param objectName 文件名 xxxx.png
     * @return OSSObject
     */
    public static OSSObject getOssObject(String objectName) {
        OSSClient ossClient = getOssInstance();
        Date expiration = new Date(System.currentTimeMillis() + 3600 * 1000);
        GeneratePresignedUrlRequest request = new GeneratePresignedUrlRequest(BUCKET_NAME, objectName, HttpMethod.GET);
        request.setExpiration(expiration);
        // 生成签名URL（HTTP GET请求）。
        URL signedUrl = ossClient.generatePresignedUrl(request);
        // 添加GetObject请求头。
        Map<String, String> customRequestHeaders = new HashMap<>();
        //customHeaders.put("Range", "bytes=100-1000");
        return ossClient.getObject(signedUrl, customRequestHeaders);
    }

    /**
     * 通用处理逻辑抽取
     * 根据 文件OSS存储对象 获取字节数组
     *
     * @param ossObject /
     * @return /
     * @throws IOException /
     */
    private static byte[] getBytes(OSSObject ossObject) throws IOException {
        InputStream inputStream = ossObject.getObjectContent();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024 * 4];
        int index;
        while ((index = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, index);
        }
        inputStream.close();
        return bos.toByteArray();
    }

    /**
     * 根据文件名获取OSS文件输入流
     *
     * @param objectName 文件名 xxx.png
     * @return /
     */
    public static InputStream getObjectInputStream(String objectName) throws IOException {
        OSSObject ossObject = getOssObject(objectName);
        InputStream inputStream = new ByteArrayInputStream(new byte[1024]);
        if (ossObject != null) {
            inputStream = ossObject.getObjectContent();
        }
        return inputStream;
    }

    /**
     * 根据文件名获取OSS字节数组
     *
     * @param objectName 文件名 xxx.png
     * @return /
     */
    public static byte[] getObjectByteArray(String objectName) throws IOException {
        OSSObject ossObject = getOssObject(objectName);
        byte[] bytes = new byte[1024];
        if (ossObject != null) {
            bytes = getBytes(ossObject);
            ossObject.close();
        }
        return bytes;
    }


    /**
     * 通过文件字节数组写入本地测试
     *
     * @param objectName 文件名xxx.png
     */
    public static void writeFile2Local4teArray(String objectName) {
        byte[] bytes;
        try {
            bytes = getObjectByteArray(objectName);
            File mkdir = FileUtil.mkdir("D:\\test");
            try (FileOutputStream fileOutputStream = new FileOutputStream(mkdir + "/" + objectName)) {
                fileOutputStream.write(bytes);
                fileOutputStream.flush();
            }
        } catch (IOException e) {
            log.error("writeFile2Local4teArray error: {}", e.getMessage());
        }
    }

    /**
     * 通过文件输入流写入本地测试
     *
     * @param objectName 文件名xxx.png
     */
    public static void writeFile2Local4InputStream(String objectName) {
        InputStream inputStream;
        byte[] buffer = new byte[1024 * 4];
        int index;
        FileOutputStream fileOutputStream;
        try {
            inputStream = getObjectInputStream(objectName);
            File mkdir = FileUtil.mkdir("D:\\test\\test2");
            fileOutputStream = new FileOutputStream(mkdir + "/" + objectName);
            while ((index = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, index);
                fileOutputStream.flush();
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException e) {
            log.error("writeFile2Local4InputStream error: {}", e.getMessage());
        }
    }

    public static void main(String[] args) {
//        String url = url("9ea735c6ebc24d9e94967933e0b6f781_shop1614147412933.png");
//        System.out.println("url = " + url);
//        URL urlTime = urlTime("9ea735c6ebc24d9e94967933e0b6f781_shop1614147412933.png");
//        System.out.println("urlTime = " + urlTime);
        // 阿里OSS文件名
        writeFile2Local4teArray("9ea735c6ebc24d9e94967933e0b6f781_shop1614147412933.png");
        writeFile2Local4InputStream("9ea735c6ebc24d9e94967933e0b6f781_shop1614147412933.png");
    }
}
