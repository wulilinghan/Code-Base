package top.b0x0.demo.infrastructure.pdf.service;


import java.util.List;

/**
 * @author TANG
 * @date 2021-02-21
 */
public interface IMerService {
    List<String> activePdfUpload(String merId);

    List<String> entryPdfUpload(String merId);

    List<String> entryPdfUpload2(String merId, top.b0x0.demo.infrastructure.infrastructure.pdf.util.PdfTemplateDataProperties pdfTemplateDataProperties);

    void threadPoolTest();
}
