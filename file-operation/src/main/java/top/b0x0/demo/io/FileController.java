package top.b0x0.demo.io;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.b0x0.demo.io.domain.R;
import top.b0x0.demo.io.util.SystemUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TANG
 * @since 2021/04/12
 */
@Slf4j
@RestController
@Api(tags = "文件管理")
public class FileController {

    @PostMapping("/up1")
    @ApiOperation("批量文件上传")
    public R fileUpload(@RequestParam("file") MultipartFile[] files, @RequestParam("isErr") String err) throws Exception {
        if (files == null || files.length == 0) {
            return R.fail("文件不能为空");
        }
        for (MultipartFile file : files) {
            // 获取文件名
            String fileName = file.getOriginalFilename();
            if (!StringUtils.hasText(fileName)) {
                return R.fail("文件不能为空");
            }
        }

        List<String> fileNameList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty() && file.getSize() > 0) {
                String fileName = file.getOriginalFilename();
                String sysOs = SystemUtils.SYS_OS_NAME;
                File folder;
                if (sysOs != null && sysOs.toLowerCase().startsWith(SystemUtils.WIN)) {
                    folder = FileUtil.mkdir("D:/test/upload");
                } else {
                    folder = FileUtil.mkdir("/home/upload");
                }
                String uploadFileName = folder + "/" + fileName;
                if ("isErr".equals(err)) {
                    int i = 1 / 0;
                }
                file.transferTo(new File(uploadFileName));
                log.info("文件:{} 上传路径:{} 上传成功...", fileName, folder);
                fileNameList.add(uploadFileName);
            }
        }
        return R.ok(fileNameList);
    }
}
