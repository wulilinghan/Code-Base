package top.b0x0.demo.io.utils;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFCell;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Apache POI操作Excel对象
 * HSSF：操作Excel 2007之前版本(.xls)格式,生成的EXCEL不经过压缩直接导出
 * XSSF：操作Excel 2007及之后版本(.xlsx)格式,内存占用高于HSSF
 * SXSSF:从POI3.8 beta3开始支持,基于XSSF,低内存占用,专门处理大数据量(建议)。
 * <p>
 * 注意:
 * 值得注意的是 SXSSFWorkbook 只能写(导出)不能读(导入)
 * <p>
 * 说明:
 * .xls 格式的excel(最大行数 65536 行,最大列数 256 列)
 * .xlsx 格式的excel(最大行数 1048576 行,最大列数 16384 列)
 * <p>
 *
 * @author Created By ManJiis 2022-01-15 18:28
 */
public class ExcelUtils {

    private static final String LOCAL_WORKBOOK_NAME = "workbook";
    private static final String LOCAL_WORKBOOK_DEFAULT_SHEET_NAME = "sheet";

    /**
     * @param total    总条数
     * @param pageSize 每页记录数
     * @return 总页数
     */
    public static int getPages(long total, long pageSize) {
        if (total <= 0 && pageSize <= 0) {
            throw new IllegalArgumentException("args: total: " + total + " pageSize: " + pageSize);
        }
        long totalPages = total / pageSize;
        if (total % pageSize != 0) {
            totalPages++;
        }
        return (int) totalPages;
    }

    /**
     * 设置行头样式
     *
     * @param workbook /
     */
    private static CellStyle getWorkbookHeadCellStyle(Workbook workbook) {
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        headerStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
        // 设置颜色
        headerStyle.setFillForegroundColor(HSSFColor.LIGHT_YELLOW.index);
        // 前景色纯色填充
        headerStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        headerStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
        headerStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
        Font headerFont = workbook.createFont();
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        headerStyle.setFont(headerFont);
        return headerStyle;
    }

    /**
     * @param heard              头
     * @param body               数据
     * @param os                 os
     */
    public static void exportExcel(List<String> heard, List<List<String>> body, OutputStream os) {
        SXSSFWorkbook workbook = (SXSSFWorkbook) ThreadLocalUtils.get(LOCAL_WORKBOOK_NAME);
        if (workbook == null) {
            // 大于10000行时会把之前的行写入硬盘
            workbook = getSXSSFWorkbookAndPutLocalThread(heard);
        }

        Sheet sheet = workbook.getSheet(LOCAL_WORKBOOK_DEFAULT_SHEET_NAME);

        // 生成数据
        int rowIndex = 1;
        for (List<String> data : body) {
            // 创建行
            SXSSFRow dataRow = (SXSSFRow) sheet.createRow(rowIndex);
            for (int k = 0; k < data.size(); k++) {
                String cellVal = data.get(k);
                // 创建单元格
                SXSSFCell cell = (SXSSFCell) dataRow.createCell(k);
                cell.setCellValue(cellVal);
            }
            rowIndex++;
        }

        try {
            workbook.write(os);
            // 刷新此输出流并强制将所有缓冲的输出字节写出
            os.flush();
            // 关闭流
            os.close();
            // 释放workbook所占用的所有windows资源
            workbook.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param heard              头
     * @param body               数据
     * @param regenerateWorkbook 是否重新生成一个Excel
     */
    public static Workbook exportExcel(List<String> heard, List<List<String>> body, boolean regenerateWorkbook) {
        SXSSFWorkbook workbook = (SXSSFWorkbook) ThreadLocalUtils.get(LOCAL_WORKBOOK_NAME);

        if (regenerateWorkbook) {
            workbook = getSXSSFWorkbookAndPutLocalThread(heard);
        } else {
            if (workbook == null) {
                workbook = getSXSSFWorkbookAndPutLocalThread(heard);
            }
        }

        Sheet sheet = workbook.getSheet(LOCAL_WORKBOOK_DEFAULT_SHEET_NAME);

        // 生成数据
        int rowIndex = 1;
        for (List<String> data : body) {
            // 创建行
            SXSSFRow dataRow = (SXSSFRow) sheet.createRow(rowIndex);
            for (int k = 0; k < data.size(); k++) {
                String cellVal = data.get(k);
                // 创建单元格
                SXSSFCell cell = (SXSSFCell) dataRow.createCell(k);
                cell.setCellValue(cellVal);
            }
            rowIndex++;
        }
        return workbook;
    }

    private static SXSSFWorkbook getSXSSFWorkbookAndPutLocalThread(List<String> heard) {
        // 大于10000行时会把之前的行写入硬盘
        SXSSFWorkbook workbook = new SXSSFWorkbook(10000);
        workbook.setCompressTempFiles(true);

        // 生成一个(带名称)表格
        SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(LOCAL_WORKBOOK_DEFAULT_SHEET_NAME);

        CellStyle headCellStyle = getWorkbookHeadCellStyle(workbook);

        // 生成head行
        SXSSFRow headerRow = (SXSSFRow) sheet.createRow(0);
        for (int i = 0; i < heard.size(); i++) {
            headerRow.createCell(i).setCellValue(heard.get(i));
            headerRow.getCell(i).setCellStyle(headCellStyle);
        }
        ThreadLocalUtils.put(LOCAL_WORKBOOK_NAME, workbook);
        return workbook;
    }

}
