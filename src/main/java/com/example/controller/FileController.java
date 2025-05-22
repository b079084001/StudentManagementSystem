package com.example.controller;

import cn.hutool.core.io.FileUtil;
import com.example.common.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

@RestController
@RequestMapping("/files")
public class FileController {
    @Value("${ip}")
    private String ip;

    @Value("${server.port}")
    private String port;

    private static final String ROOT_PATH = System.getProperty("user.dir") + "/files";


    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        long flag = System.currentTimeMillis();
        String fileName = flag + '_' + originalFilename;

        File finalFile = new File(ROOT_PATH + "/" + fileName);
        if (!finalFile.getParentFile().exists()) {
            finalFile.getParentFile().mkdirs();
        }
        file.transferTo(finalFile);
//        String url = "http://localhost:9090/files/download?fileName=" + fileName;
        String url = "http://" + ip + ":" + port + "/files/download?fileName=" + fileName;
        return Result.success(url);
    }

    @GetMapping("/download")
    public Result download(String fileName, HttpServletResponse response) throws IOException {
        File file = new File(ROOT_PATH + "/" + fileName);
        ServletOutputStream os = response.getOutputStream();
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, "UTF-8"));
        response.setContentType("application/octet-stream");
        //os.write(FileUtil.readBytes(file));
        FileUtil.writeToStream(file, os);
        os.flush();
        os.close();
        return Result.success();
    }

}
