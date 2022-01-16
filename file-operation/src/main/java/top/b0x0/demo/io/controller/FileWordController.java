package top.b0x0.demo.io.controller;


import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.DocxRenderData;
import com.deepoove.poi.data.MiniTableRenderData;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.TextRenderData;
import com.deepoove.poi.data.style.Style;
import com.deepoove.poi.data.style.TableStyle;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.b0x0.demo.io.dao.ExportDao;
import top.b0x0.demo.io.domain.Columns;
import top.b0x0.demo.io.domain.DbVO;
import top.b0x0.demo.io.domain.Tables;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Word
 *
 * @author TANG
 * @since 2021/04/19
 */
@RestController
@RequestMapping("word")
@Api(tags = "Word")
public class FileWordController {

    @Resource
    private ExportDao exportDao;

    public static String DB_NAME = "bfp2.0_dev";
    public static String TITLE = DB_NAME + "数据库说明文档";
    public static String DB_TYPE = "MySQL";
    public static String PROJECT_VERSION = "2.0";
    public static String PROJECT_NAME = "bfp";
    public static String UPDATE_BY = "张三";

    @GetMapping("/dbWord")
    @ApiOperation(value = "导出数据库词典")
    public void exportDoc(@Validated DbVO dbVO, HttpServletResponse response) throws IOException {
        DB_NAME = dbVO.getDbName();
        TITLE = StringUtils.hasText(dbVO.getTitle()) ? dbVO.getTitle() : DB_NAME + "数据库说明文档";
        PROJECT_NAME = StringUtils.hasText(dbVO.getProjectName()) ? dbVO.getProjectName() : DB_NAME;
        PROJECT_VERSION = StringUtils.hasText(dbVO.getProjectVersion()) ? dbVO.getProjectVersion() : "1.0";

        System.out.println("DB_NAME = " + DB_NAME);
        System.out.println("TITLE = " + TITLE);
        System.out.println("PROJECT_NAME = " + PROJECT_NAME);
        System.out.println("PROJECT_VERSION = " + PROJECT_VERSION);

        List<Tables> tablesList = exportDao.getTables(DB_NAME);
        // 表头
        RowRenderData header = renderTableHeader();
        int count = 0;
        String localDateNow = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());
        List<Map<String, Object>> tableList = new ArrayList<>();
        // 最终返回数据
        Map<String, Object> exportData = new HashMap<>(8);
        exportData.put("title", TITLE);
        exportData.put("projectName", PROJECT_NAME);
        exportData.put("dbType", dbVO.getDbType());
        exportData.put("projectVersion", PROJECT_VERSION);
        exportData.put("updateTime", localDateNow);
        exportData.put("remark", "创建数据库说明文档");
        exportData.put("updateBy", dbVO.getUpdateBy());
        // 循环创建表
        for (Tables table : tablesList) {
            count++;
            // 通过[表名]获取该表的[所有字段集合] 并渲染
            List<Columns> columnsList = exportDao.getColumns(DB_NAME, table.getTableName());
            // 获取某张表的字段列表
            List<RowRenderData> rowList = renderTableRow(columnsList);
            Map<String, Object> data = new HashMap<>(8);
            data.put("no", count);
            data.put("tableComment", table.getTableComment());
            data.put("engine", table.getEngine());
            data.put("tableCollation", table.getTableCollation());
            data.put("tableType", table.getTableType());
            data.put("tableName", table.getTableName());
            // 渲染表对象 MiniTableRenderData
            MiniTableRenderData miniTableRenderData = new MiniTableRenderData(header, rowList);
            // 自适应性宽度
//            miniTableRenderData.setWidth(0);
            miniTableRenderData.setWidth(20);
            data.put("tableData", miniTableRenderData);
            tableList.add(data);
        }
        exportData.put("tableList", new DocxRenderData(new ClassPathResource("templates/table_template.docx").getFile(), tableList));

        XWPFTemplate template = XWPFTemplate.compile(new ClassPathResource("templates/export_dic_template.docx").getFile()).render(exportData);

//        String outFileName = TITLE + localDateNow + ".docx";
//        FileOutputStream out = new FileOutputStream("C:\\" + outFileName);
//        template.write(out);
//        out.flush();
//        out.close();
//        template.close();

        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + new String((TITLE + localDateNow).getBytes(), StandardCharsets.ISO_8859_1) + ".docx");
        ServletOutputStream outputStream = response.getOutputStream();
        template.write(outputStream);
        template.close();
        outputStream.flush();
        outputStream.close();
        System.out.println("=====生成文件结束");

    }

    /**
     * 字体样式
     *
     * @return Style
     */
    public static Style getHeardFontStyle() {
        Style style = new Style();
        style.setBold(true);
        style.setFontSize(10);
//        style.setColor("000000"); // 黑色
        style.setColor("F8F8FF"); // 白色
        style.setFontFamily("宋体");
        return style;
    }

    /**
     * 表头单元格背景
     */
    public static TableStyle getHeaderTableStyle() {
        TableStyle style = new TableStyle();
//        style.setBackgroundColor("B7B7B7"); //灰色
        style.setBackgroundColor("4F81BD"); //深蓝色
//        style.setBackgroundColor("7BA0CE"); //中等蓝色
//        style.setBackgroundColor("DBE5F1"); //浅蓝色
        // 左对齐
//        style.setAlign(STJc.Enum.forInt(1));
        return style;
    }

    /**
     * 表头字体设置
     *
     * @return RowRenderData
     */
    private static RowRenderData renderTableHeader() {
        // 设置表头
        RowRenderData header = RowRenderData.build(
                new TextRenderData("序号", getHeardFontStyle()),
                new TextRenderData("字段名称", getHeardFontStyle()),
                new TextRenderData("字段描述", getHeardFontStyle()),
                new TextRenderData("字段类型", getHeardFontStyle()),
                new TextRenderData("长度", getHeardFontStyle()),
                new TextRenderData("允许空", getHeardFontStyle()),
                new TextRenderData("缺省值", getHeardFontStyle()));
        header.setRowStyle(getHeaderTableStyle());
        return header;
    }


    /**
     * 渲染一张表的所有行
     *
     * @return List<RowRenderData>
     */
    private static List<RowRenderData> renderTableRow(List<Columns> columnsList) {
        List<RowRenderData> result = new ArrayList<>();
        for (Columns columns : columnsList) {
            Long characterMaximumLength = columns.getCharacterMaximumLength();
            characterMaximumLength = characterMaximumLength == null ? 0 : characterMaximumLength;
            // 渲染每一行数据
            RowRenderData row = RowRenderData.build(
                    new TextRenderData(columns.getOrdinalPosition().toString()),
                    new TextRenderData(columns.getColumnName()),
                    new TextRenderData(columns.getColumnComment()),
                    new TextRenderData(columns.getDataType()),
                    new TextRenderData(characterMaximumLength.toString()),
                    new TextRenderData(columns.getIsNullable()),
                    new TextRenderData(columns.getColumnDefault())
            );
            result.add(row);
        }
        return result;
    }

    /**
     * 获取数据库的所有表名及表的信息
     *
     * @return list
     */
    private static List<Map<String, String>> getTableName(List<Tables> tablesList) {
        List<Map<String, String>> list = new ArrayList<>();
        for (Tables tables : tablesList) {
            // 存放单独一张表的结构信息
            Map<String, String> result = new HashMap<>();
            result.put("table_name", tables.getTableName());
            result.put("table_type", tables.getTableType());
            result.put("engine", tables.getEngine());
            result.put("table_collation", tables.getTableCollation());
            result.put("table_comment", tables.getTableComment());
            result.put("create_options", tables.getCreateOptions());
            list.add(result);
        }
        return list;
    }
}