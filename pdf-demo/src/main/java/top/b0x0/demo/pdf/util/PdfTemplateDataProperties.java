package top.b0x0.demo.pdf.util;

import top.b0x0.demo.pdf.entity.TemplateDataModel;
import lombok.Data;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author TANG
 */
@Data
@Configuration
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@PropertySource(value = "classpath:pdfList.properties", ignoreResourceNotFound = true, encoding = "UTF-8")
@ConfigurationProperties(prefix = "pdf")
public class PdfTemplateDataProperties {

    private List<TemplateDataModel> bsActiveList;

    private List<TemplateDataModel> bsEntryList;

}