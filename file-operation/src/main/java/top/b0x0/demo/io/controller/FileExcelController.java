package top.b0x0.demo.io.controller;

import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import top.b0x0.demo.io.domain.R;
import top.b0x0.demo.io.domain.SysUser;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Excel
 *
 * @author TANG
 * @since 2021/04/19
 */
@Slf4j
@RestController
@RequestMapping("excel")
@Api(tags = "Excel")
public class FileExcelController {

    @GetMapping("export")
    @ApiOperation(value = "导出", tags = "Excel-hutool")
    public void export(HttpServletResponse response) throws UnsupportedEncodingException {

        List<SysUser> list = new ArrayList<>();
        list.add(SysUser.builder().userId(1111L).username("zhangsan1").phone("17688888888").enabled(1).build());
        list.add(SysUser.builder().userId(2222L).username("zhangsan2").phone("17688888886").enabled(1).build());
        list.add(SysUser.builder().userId(4444L).username("zhangsan3").phone("17688888885").enabled(1).build());
        list.add(SysUser.builder().userId(5555L).username("zhangsan4").phone("17688888884").enabled(1).build());
        list.add(SysUser.builder().userId(6666L).username("zhangsan5").phone("17688888883").enabled(1).build());
        list.add(SysUser.builder().userId(7777L).username("zhangsan5").phone("17688888882").enabled(0).build());

        // 通过工具类创建writer，默认创建xls格式
        ExcelWriter writer = ExcelUtil.getWriter(true);

        //自定义标题别名
        writer.addHeaderAlias("username", "姓名");
        writer.addHeaderAlias("phone", "手机号");
        writer.addHeaderAlias("enabled", "是否禁用");

        // 合并单元格后的标题行，使用默认标题样式
        writer.merge(2, "用户信息");
        writer.setOnlyAlias(true);
        // 强制输出标题
        writer.write(list, true);

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        String fileName = "userInfo" + System.currentTimeMillis();
        // .xlsx  .xls
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = null;
        try {
            out = response.getOutputStream();
            writer.flush(out, true);
        } catch (IOException e) {
            log.error("导出Excel异常: {}", e.getMessage());
        } finally {
            writer.close();
        }
    }

    @PostMapping("importFromResource")
    @ApiOperation(value = "测试", tags = "Excel-hutool")
    public R testImportExcel() throws IOException {
        ClassPathResource resource = new ClassPathResource("images/userInfo1618833462354.xlsx");
        File srcFile = resource.getFile();
        ExcelReader reader = ExcelUtil.getReader(srcFile);
        // 从第三行数据开始读
        List<List<Object>> read = reader.read(2);
        return R.ok(read);
    }

    /**
     * eg: images/userInfo1618833462354.xlsx
     *
     * @param files /
     * @return /
     */
    @PostMapping("import")
    @ApiOperation(value = "导入", tags = "Excel-hutool")
    public R importExcel(@RequestParam(value = "file", required = false) MultipartFile[] files) {
        if (files == null || files.length == 0) {
            return R.fail("文件不能为空");
        }
        for (MultipartFile file : files) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            if (!StringUtils.hasText(fileName)) {
                return R.fail("文件不能为空");
            }
            // 获取文件后缀
            String prefix = fileName.substring(fileName.lastIndexOf("."));
            if (!prefix.toLowerCase().contains("xls") && !prefix.toLowerCase().contains("xlsx")) {
                return R.fail("文件格式异常，请上传Excel文件格式");
            }
        }

        List<Object> result = new ArrayList<>();
        for (MultipartFile multipartFile : files) {
            //通过创建临时文件将 MultipartFile转为File
            File file = null;
            try {
                String originalFilename = multipartFile.getOriginalFilename();
                assert originalFilename != null;
                String[] filename = originalFilename.split("\\.");
                file = File.createTempFile(filename[0], filename[1]);
                multipartFile.transferTo(file);
                ExcelReader reader = ExcelUtil.getReader(file);
                // 从第三行数据开始读
                // 按照自己需要更改参数
                List<List<Object>> read = reader.read(2);
                result.add(read);
                boolean delete = file.delete();
                System.out.println("delete = " + delete);
            } catch (IOException e) {
                log.error("MultipartFile转成File异常:{}", e.getMessage());
            }
        }
        return R.ok(result);
    }


}
