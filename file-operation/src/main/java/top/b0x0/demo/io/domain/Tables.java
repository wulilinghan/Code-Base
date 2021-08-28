package top.b0x0.demo.io.domain;

import lombok.Data;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author musui
 */
@Data
@ToString
public class Tables implements Serializable {
    private static final long serialVersionUID = -95389627276769266L;


    private String tableName;

    private String tableType;

    private String engine;

    private String tableCollation;

    private String createOptions;

    private String tableComment;

}