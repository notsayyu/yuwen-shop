package com.notsay.yuwenshop.common.utils;

import com.notsay.yuwenshop.common.constants.BaseConstants;
import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/29 11:02
 */
@Slf4j
public class FileUtil {

    /**
     * 获取文件后缀名
     *
     * @param fileName
     * @return
     */
    public static String getExtension(String fileName) {
        if (fileName == null) {
            log.info("try to get file extension name, but input fileName is null");
            return "";
        }
        String extension;
        int index = fileName.lastIndexOf(BaseConstants.FILE_SEPARATOR);
        if (index > 0) {
            extension = fileName.substring(index);
        } else {
            extension = "";
        }
        return extension;
    }
}