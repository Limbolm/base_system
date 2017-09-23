package com.smpl.base.Utils;

import com.smpl.base.Exception.BusinessException;

import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * 文件管理工具类
 * 主要为文件的 上传下载 及文件的相关操作
 *
 */
public final class FileManageUtil {

    private static void downLaodFile(HttpServletResponse response, String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            response.setContentType("application/force-download");// 设置强制下载不打开对话框
            response.addHeader("Content-Disposition", "attachment;fileName=" + file.getName());// 设置文件名
            byte[] buffer = new byte[1024];
            FileInputStream fis = null;
            BufferedInputStream bis = null;
            try {
                fis = new FileInputStream(file);
                bis = new BufferedInputStream(fis);
                OutputStream os = response.getOutputStream();
                int i = bis.read(buffer);
                while (i != -1) {
                    os.write(buffer, 0, i);
                    i = bis.read(buffer);
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }else{
            new BusinessException("文件不存在！");
    }
    }
}

