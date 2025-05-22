package com.example.controller;


import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.entity.File1;
import com.example.mapper.File1Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.List;

@RestController
@RequestMapping("/file")
public class File1Controller {
    @Value("${files.upload.path}")
    private String fileUploadPath;

    @Resource
    private File1Mapper file1Mapper;

    /**
     * 文件上传接口
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    public String upload(@RequestParam MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String type = FileUtil.extName(originalFilename);
        long size = file.getSize();

        //先存储到磁盘
        File uploadParentFile =new File(fileUploadPath);

        //判断配置的文件的目录是否存在
        if(!uploadParentFile.exists()) {
            uploadParentFile.mkdirs();
        }
        //定义一个唯一的文件标识码
        String uuid = IdUtil.fastSimpleUUID();
        String fileUUID = uuid + StrUtil.DOT + type;
        File uploadFile = new File(fileUploadPath + fileUUID);


        //把获取到的文件存储到磁盘目录
        file.transferTo(uploadFile);
        String url = "http://localhost:9090/file/" + fileUUID;

        //存储数据库
        File1 saveFile = new File1();
        saveFile.setName(originalFilename);
        saveFile.setType(type);
        saveFile.setSize(size / 1024);
        saveFile.setUrl(url);
        file1Mapper.insert(saveFile);
        return url;
    }

//    /**
//     * 文件上传接口
//     * @param file 前端传递过来的文件
//     * @return
//     * @throws IOException
//     */
//    @PostMapping("/upload")
//    public String upload(@RequestParam MultipartFile file) throws IOException {
//        String originalFilename = file.getOriginalFilename();
//        String type = FileUtil.extName(originalFilename);
//        long size = file.getSize();
//
//        // 定义一个文件唯一的标识码
//        String fileUUID = IdUtil.fastSimpleUUID() + StrUtil.DOT + type;
//
//        File uploadFile = new File(fileUploadPath + fileUUID);
//        // 判断配置的文件目录是否存在，若不存在则创建一个新的文件目录
//        File parentFile = uploadFile.getParentFile();
//        if(!parentFile.exists()) {
//            parentFile.mkdirs();
//        }
//
//        String url;
//        // 获取文件的md5
//        String md5 = SecureUtil.md5(file.getInputStream());
//        // 从数据库查询是否存在相同的记录
//        File1 dbFiles = getFileByMd5(md5);
//        if (dbFiles != null) {
//            url = dbFiles.getUrl();
//        } else {
//            // 上传文件到磁盘
//            file.transferTo(uploadFile);
//            // 数据库若不存在重复文件，则不删除刚才上传的文件
//            url = "localhost:9090/file/" + fileUUID;
//        }
//
//
//        // 存储数据库
//        File1 saveFile = new File1();
//        saveFile.setName(originalFilename);
//        saveFile.setType(type);
//        saveFile.setSize(size/1024); // 单位 kb
//        saveFile.setUrl(url);
//        saveFile.setMd5(md5);
//        file1Mapper.insert(saveFile);
//
//        return url;
//    }



    @GetMapping("/{fileUUID}")
    public void download(@PathVariable String fileUUID, HttpServletResponse response) throws IOException {
        // 根据文件的唯一标识码获取文件
        File uploadFile = new File(fileUploadPath + fileUUID);
        // 设置输出流的格式
        ServletOutputStream os = response.getOutputStream();
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileUUID, "UTF-8"));
        response.setContentType("application/octet-stream");

        // 读取文件的字节流
        os.write(FileUtil.readBytes(uploadFile));
        os.flush();
        os.close();
    }


    /**
     * 通过文件的md5查询文件
     *
     * @param md5
     * @return
     */
    private File1 getFileByMd5(String md5) {
        // 查询文件的md5是否存在
        QueryWrapper<File1> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("md5", md5);
        List<File1> filesList = file1Mapper.selectList(queryWrapper);
        return filesList.size() == 0 ? null : filesList.get(0);
    }

}