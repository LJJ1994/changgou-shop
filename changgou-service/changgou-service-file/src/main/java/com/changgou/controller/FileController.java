package com.changgou.controller;

import com.changgou.file.FastDFSFile;
import com.changgou.entity.FastDFSClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @Author: LJJ
 * @Program: changgou
 * @Description:
 * @Create: 2020-03-03 09:01:01
 * @Modified By:
 */
@RestController
@CrossOrigin
public class FileController {
    @Value("${fdfs.storage_server.ip}")
    String storageIp;

    @PostMapping("/upload")
    public String upload(@RequestParam("file") MultipartFile file) {
        String path ="";
        try {
            path=saveFile(file);
            System.out.println(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return path;
    }

    /**
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public String saveFile(MultipartFile multipartFile) throws IOException {
        //1. 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        //2. 获取文件内容
        byte[] content = multipartFile.getBytes();
        //3. 获取文件扩展名
        String ext = "";
        if (fileName != null && !"".equals(fileName)) {
            ext = fileName.substring(fileName.lastIndexOf("."));
        }
        //4. 创建文件实体类对象
        FastDFSFile fastDFSFile = new FastDFSFile(fileName, content, ext);
        //5. 上传
        String[] uploadResults = FastDFSClient.upload(fastDFSFile);
        //6. 拼接上传后的文件的完整路径和名字, uploadResults[0]为组名, uploadResults[1]为文件名称和路径
        //7. 返回
        return storageIp + "/" + uploadResults[0] + "/" + uploadResults[1];
    }
}
