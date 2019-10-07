package com.xhhp.mmall.util;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

/**
 * FTPUtil class
 *
 * @author Flc
 * @date 2019/9/28
 */

@Component
public class FTPUtil {


    private static String ftpIp="192.168.1.107";

    private static String ftpUser="ftpuser";

    private static String ftpPass="123456";

    private static int ftpPort=21;

    private FTPClient ftpClient;


    public static boolean upLoadFile(List<File> fileList) throws IOException {
        FTPUtil ftpUtil = new FTPUtil();
        boolean result = ftpUtil.upLoadFile("img",fileList);
        return result;
    }

    private boolean upLoadFile(String remotePath,List<File> fileList) throws IOException {
        boolean uploaded = true;
        FileInputStream fis = null;
        if(connectServer(ftpIp,ftpPort,ftpUser,ftpPass)) {
            try {
                System.out.println("连接上了");
                ftpClient.changeWorkingDirectory(remotePath);
                ftpClient.setBufferSize(1024);
                ftpClient.setControlEncoding("UTF-8");
                ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
                ftpClient.enterLocalPassiveMode();
                System.out.println(ftpClient.getPassiveLocalIPAddress());
                for (File fileItem: fileList
                     ) {
                    System.out.println(fileItem);
                    fis = new FileInputStream(fileItem);
                    boolean result = ftpClient.storeFile(fileItem.getName(),fis);
                    System.out.println("结果是："+ result);
                }
            } catch (IOException e) {
                uploaded = false;
                System.out.println("上传文件异常");
            } finally {
                fis.close();
                ftpClient.disconnect();
            }
        }
        return uploaded;
    }

    private boolean connectServer(String ip, int port, String user, String pwd) {
        boolean isSuccess = false;
        ftpClient = new FTPClient();
        try {
            ftpClient.connect(ip);
            isSuccess = ftpClient.login(user,pwd);
        } catch (IOException e) {
            System.out.println("连接ftp服务器异常");
        }
        return isSuccess;
    }

    public FTPClient getFtpClient() {
        return ftpClient;
    }

    public void setFtpClient(FTPClient ftpClient) {
        this.ftpClient = ftpClient;
    }
}
