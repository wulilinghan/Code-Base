package top.b0x0.demo.http.controller.provide;

import cn.hutool.core.io.FileUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import top.b0x0.demo.http.common.R;
import top.b0x0.demo.http.common.SysEnvUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author TANG
 * @since 2021-04-08
 */
@RestController
@RequestMapping("file")
@Api(tags = "文件上传")
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @PostMapping("/up1")
    @ApiOperation("文件上传")
    public R fileUpload(@RequestParam("file") MultipartFile[] files, @RequestParam("isErr") String err) throws Exception {
        if (files == null) {
            return R.fail("文件上传错误, 服务端未拿到上传的文件！");
        }
        List<String> fileNameList = new ArrayList<>();
        for (MultipartFile file : files) {
            if (!file.isEmpty() && file.getSize() > 0) {
                String fileName = file.getOriginalFilename();
                String sysOs = SysEnvUtils.SYS_OS_NAME;
                File folder;
                if (sysOs != null && sysOs.toLowerCase().startsWith(SysEnvUtils.SYS_WIN)) {
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
