# 工程简介
## Hutool IO流相关
    IO的操作包括读和写，应用场景包括网络操作和文件操作。IO操作在Java中是一个较为复杂的过程，我们在面对不同的场景时，要选择不同的InputStream和OutputStream实现来完成这些操作。
    而如果想读写字节流，还需要Reader和Writer的各种实现类。这些繁杂的实现类，一方面给我我们提供了更多的灵活性，另一方面也增加了复杂性。

### 封装
io包的封装主要针对流、文件的读写封装，主要以工具类为主，提供常用功能的封装，这包括：
- IoUtil 流操作工具类
- FileUtil 文件读写和操作的工具类。
- FileTypeUtil 文件类型判断工具类
- WatchMonitor 目录、文件监听，封装了JDK1.7中的WatchService
- ClassPathResource针对ClassPath中资源的访问封装
- FileReader 封装文件读取
- FileWriter 封装文件写入

### 流扩展
    除了针对JDK的读写封装外，还针对特定环境和文件扩展了流实现。
包括：
- BOMInputStream针对含有BOM头的流读取
- FastByteArrayOutputStream 基于快速缓冲FastByteBuffer的OutputStream，随着数据的增长自动扩充缓冲区（from blade）
- FastByteBuffer 快速缓冲，将数据存放在缓冲集中，取代以往的单一数组（from blade）


# 基于Apache POI导出大数据量（百万级）Excel的实现

```xml
        <!-- Apache POI -->
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi</artifactId>
            <version>3.10-FINAL</version>
        </dependency>
        <dependency>
            <groupId>org.apache.poi</groupId>
            <artifactId>poi-ooxml</artifactId>
            <version>3.10-FINAL</version>
        </dependency>
```
### 使用POI导出大数据量Excel Demo

模拟了100W进行一次性Excel导出。 千万数据通过拆分成多个文件，最终打成一个压缩包实现。


