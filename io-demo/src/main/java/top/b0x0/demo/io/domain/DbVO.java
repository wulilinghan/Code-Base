package top.b0x0.demo.io.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

import static top.b0x0.demo.io.FileWordController.DB_TYPE;


/**
 * @author musui
 */
@ToString
@Data
@ApiModel("导出数据库词典请求实体")
public class DbVO {
    @ApiModelProperty(value = "数据库名称", required = true)
    @NotBlank(message = "数据库名称不能为空")
    private String dbName;

    @ApiModelProperty("文档名称")
    private String title;

    @ApiModelProperty("数据库类型 (例如:MySQL、Oracle),目前仅支持MySQL")
    private String dbType = DB_TYPE;

    @ApiModelProperty("当前项目版本 (例如:1.0、2.0)")
    private String projectVersion;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("修改人")
    private String updateBy;

}