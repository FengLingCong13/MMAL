package com.xhhp.mmall.service.impl;

import com.google.common.collect.Lists;
import com.xhhp.mmall.service.IFileService;
import com.xhhp.mmall.util.FTPUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * IFileServiceImpl class
 *
 * @author Flc
 * @date 2019/9/27
 */
@Service("iFileService")
public class IFileServiceImpl implements IFileService {
    @Override
    public String upload(MultipartFile file, String path) {
        //原始文件名
        String fileName = file.getOriginalFilename();
        //扩展名
        String fileExtensionName =  fileName.substring(fileName.lastIndexOf(".")+1);

        String newName = UUID.randomUUID().toString() + fileExtensionName;
        String newName2 = UUID.randomUUID().toString() +"."+ fileExtensionName;
        System.out.println(path);
        File fileDir = new File(path);
        System.out.println(fileDir);
        if(!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }

        File targetFile = new File(path, newName2);
        System.out.println(newName);
        try {
            file.transferTo(targetFile);
            //文件上传成功

            //将targetFile上传到ftp服务器上
            boolean result = FTPUtil.upLoadFile(Lists.newArrayList(targetFile));
            System.out.println(result);
            //上传完之后，删除upload下面的文件
            //targetFile.delete();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return targetFile.getName();
    }
}
