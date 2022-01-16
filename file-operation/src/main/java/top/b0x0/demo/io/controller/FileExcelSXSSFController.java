package top.b0x0.demo.io.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.io.entity.Person;
import top.b0x0.demo.io.service.PersonService;
import top.b0x0.demo.io.utils.DateUtils;
import top.b0x0.demo.io.utils.ExcelUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 基于Apache POI导出大数据量（百万级）Excel的实现
 * <p>
 * swagger地址：http://localhost:8082/doc.html#/home
 *
 * @author Created By ManJiis 2022-01-15 20:28
 */
@RestController
@RequestMapping("file/excel")
@Api(tags = "Apache POI导出")
public class FileExcelSXSSFController {

    @Resource
    private PersonService personService;

    private static final long EXPORT_MAX_SIZE = 1000000;

    private static final String EXPORT_PATH = "D://SoftTemp//";

    @PostConstruct
    public void init() {
        File file = new File(EXPORT_PATH);
        if (!file.exists()) {
            // 创建该文件夹目录
            boolean mkdirs = file.mkdirs();
            System.out.println(EXPORT_PATH + " 文件夹创建" + (mkdirs ? "成功" : "失败"));
        }
    }

    /**
     * 百万数据导出
     */
    @GetMapping("millionsOfDataExcel")
    @ApiOperation("百万数据导出")
    public HashMap<String, Object> millionsOfDataExcel() {
        long start = System.currentTimeMillis();

        PageRequest pageRequest = PageRequest.of(0, 1000000);
        Page<Person> personPage = personService.queryByPage(new Person(), pageRequest);

        List<String> head = new ArrayList<>();
        head.add("ID");
        head.add("用户名");
        head.add("旧用户名");
        head.add("年龄");
        head.add("性别");
        head.add("地址");
        head.add("身份证正面");
        head.add("身份证反面");
        head.add("生日");
        head.add("创建日期");

        List<List<String>> body = new ArrayList<>();
        List<Person> personList = personPage.get().collect(Collectors.toList());
        toBody(body, personList);

        File file = new File(EXPORT_PATH);
        OutputStream os = null;
        String filePath = file.getAbsolutePath() + File.separator + start + ".xlsx";
        try {
            System.out.println("start export xlsx...");
            // .xlsx格式
            os = new FileOutputStream(filePath);
            ExcelUtils.exportExcel(head, body, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        long end = System.currentTimeMillis();
        int totalSize = personList.size();
        String second = (end - start) / (double) 1000 + "秒";
        System.out.println("end ...共" + totalSize + "条数据,用时" + second);
        System.out.println("文件路径：" + filePath);

        HashMap<String, Object> map = new HashMap<>(4);
        map.put("总量", totalSize);
        map.put("耗时", second);
        map.put("文件路径", filePath);
        return map;
    }

    private void toBody(List<List<String>> body, List<Person> personList) {
        for (Person person : personList) {
            List<String> row = new ArrayList<>();
            row.add(person.getId());
            row.add(person.getUsername());
            row.add(person.getFormername());
            row.add(person.getAge());
            row.add("0".equals(person.getSex()) ? "女" : "男");
            row.add(person.getAddress());
            row.add(person.getUrlFrontofidcard());
            row.add(person.getUrlBackofidcard());
            row.add(person.getBirthday());
            row.add(DateUtils.date2DateTimeStr(person.getCreateTime()));
            body.add(row);
        }
    }


    /**
     * 千万数据导出
     * <p>
     * 分多个Excel 打包成一个zip压缩包文件
     */
    @GetMapping("overMillionsOfDataExcel")
    @ApiOperation("千万数据导出")
    public HashMap<String, Object> overMillionsOfDataExcel() {
        long start = System.currentTimeMillis();

        long countAll = personService.countAll();
        int totalPage = ExcelUtils.getPages(countAll, EXPORT_MAX_SIZE);
        System.out.println("totalPage = " + totalPage);

        List<String> head = new ArrayList<>();
        head.add("ID");
        head.add("用户名");
        head.add("旧用户名");
        head.add("年龄");
        head.add("性别");
        head.add("地址");
        head.add("身份证正面");
        head.add("身份证反面");
        head.add("生日");
        head.add("创建日期");

        List<File> fileList = new ArrayList<>();

        for (int i = 0; i < totalPage; i++) {
            PageRequest pageRequest = PageRequest.of(i, 1000000);
            Page<Person> personPage = personService.queryByPage(new Person(), pageRequest);

            List<List<String>> body = new ArrayList<>();
            List<Person> personList = personPage.get().collect(Collectors.toList());
            toBody(body, personList);

            File file = new File(EXPORT_PATH);
            OutputStream os = null;
            try {
                if (i == 0) {
                    System.out.println("start export xlsx...");
                }

                Workbook workbook = ExcelUtils.exportExcel(head, body, true);

                // .xlsx格式
                String filePath = file.getAbsolutePath() + File.separator + (i + 1) + "_" + start + ".xlsx";
                os = new FileOutputStream(filePath);
                workbook.write(os);

                // 记录生成文件
                fileList.add(new File(filePath));
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (os != null) {
                    try {
                        os.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        File finalOutFile = null;
        if (fileList.size() > 1) {
            String subTmpName = fileList.get(0).getName();
            System.out.println("subTmpName = " + subTmpName);
            finalOutFile = new File(EXPORT_PATH + subTmpName + "等多个文件.zip");
            try (ZipOutputStream outputStream = new ZipOutputStream(new FileOutputStream(finalOutFile))) {
                for (File file : fileList) {
                    addZipEntry("", file, outputStream);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                for (File file : fileList) {
                    file.delete();
                }
            }
        } else {
            File file = fileList.get(0);
            if (file != null) {
                finalOutFile = file;
            }
        }

        long end = System.currentTimeMillis();
        String second = (end - start) / (double) 1000 + "秒";
        System.out.println("end ...共" + countAll + "条数据,用时" + second);
        String finalFilePath = finalOutFile != null ? finalOutFile.getAbsolutePath() : null;
        System.out.println("文件路径：" + finalFilePath);

        HashMap<String, Object> map = new HashMap<>(4);
        map.put("总量", countAll);
        map.put("耗时", second);
        map.put("文件路径", finalFilePath);
        return map;
    }

    /**
     * 将文件写入到zip文件中
     *
     * @param baseName        /
     * @param srcFile         /
     * @param zipOutputStream /
     */
    private static void addZipEntry(String baseName, File srcFile, ZipOutputStream zipOutputStream) throws IOException {
        InputStream is = null;
        String entry = baseName + srcFile.getName();
        if (srcFile.isDirectory()) {
            File[] files = srcFile.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    // 递归导入文件
                    addZipEntry(entry + File.separator, file, zipOutputStream);
                }
            }
        } else {

            is = new FileInputStream(srcFile);
            zipOutputStream.putNextEntry(new ZipEntry(entry));

            int len = 0;
            byte[] buffer = new byte[2 * 1024];
            while ((len = is.read(buffer)) > 0) {
                zipOutputStream.write(buffer, 0, len);
                zipOutputStream.flush();
            }
            zipOutputStream.closeEntry();
        }
        if (is != null) {
            is.close();
        }
    }
}

