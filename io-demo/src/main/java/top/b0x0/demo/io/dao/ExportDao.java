package top.b0x0.demo.io.dao;

import org.apache.ibatis.annotations.Param;
import top.b0x0.demo.io.domain.Columns;
import top.b0x0.demo.io.domain.Tables;

import java.util.List;

/**
 * @author musui
 */
public interface ExportDao {

    /**
     * 获取指定数据库中所有表信息
     *
     * @param dbName /
     * @return /
     */
    List<Tables> getTables(String dbName);

    /**
     * 获取指定数据库,指定表的所有字段
     *
     * @param dbName    /
     * @param tableName /
     * @return /
     */
    List<Columns> getColumns(@Param("dbName") String dbName,@Param("tableName") String tableName);

    List<Columns> getColumsByTableList(@Param("dbName") String dbName, @Param("list") List<String> list);

}