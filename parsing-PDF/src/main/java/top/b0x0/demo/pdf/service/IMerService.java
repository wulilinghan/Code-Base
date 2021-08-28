package top.b0x0.demo.pdf.service;

import top.b0x0.demo.pdf.util.PdfTemplateDataProperties;

import java.util.List;

/**
 * @author TANG
 * @date 2021-02-21
 */
public interface IMerService {
    List<String> activePdfUpload(String merId);

    List<String> entryPdfUpload(String merId);

    List<String> entryPdfUpload2(String merId, PdfTemplateDataProperties pdfTemplateDataProperties);

    void threadPoolTest();
}
