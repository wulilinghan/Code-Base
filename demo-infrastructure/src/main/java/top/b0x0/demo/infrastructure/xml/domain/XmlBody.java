package top.b0x0.demo.infrastructure.xml.domain;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

/**
 * @author TANG
 * @date 2021-03-15
 */
@Data
public class XmlBody {

    @JacksonXmlProperty(localName = "Ret_Details")
    private RetDetails retDetails;

}
