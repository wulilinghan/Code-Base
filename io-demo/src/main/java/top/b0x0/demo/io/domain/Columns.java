package top.b0x0.demo.io.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author musui
 */
@Data
@ToString
public class Columns implements Serializable {
    private static final long serialVersionUID = 103814573127575128L;

    private Long ordinalPosition;

    private String columnName;

    private String columnType;

    private String columnKey;

    private String extra;

    private String isNullable;

    private String columnComment;

    private String columnDefault;

    private String dataType;

    private Long characterMaximumLength;


}