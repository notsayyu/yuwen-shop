package com.notsay.yuwenshop.biz.service;

import com.notsay.yuwenshop.biz.dto.FileStoreDTO;
import com.notsay.yuwenshop.common.constants.BaseConstants;
import com.notsay.yuwenshop.common.enums.FileNameSuffixEnum;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/25 16:56
 */
public interface FileStoreService {
    FileStoreDTO saveFile(MultipartFile inputFile);

    void downloadFile(String fileKey, String fileOriginName, HttpServletResponse response);

    void previewFile(String fileKey, String fileOriginName, Long byteSize, HttpServletResponse response);

    void deleteFile(String fileKey);

    /**
     * 设置请求头, 根据文件类型进行设置
     *
     * @param response
     * @param fileName
     * @param fileSize
     * @throws UnsupportedEncodingException
     */
    default void setHeader(HttpServletResponse response, String fileName, Long fileSize) throws UnsupportedEncodingException {
        String type = "attachment";
        String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
        //根据文件类型区分, 部分文件类型, 优先使用浏览器展示
        int index = fileName.lastIndexOf(BaseConstants.FILE_SEPARATOR);
        if (index != -1) {
            String suffix = fileName.substring(index + 1);
            if (FileNameSuffixEnum.containSuffix(suffix)) {
                type = "inline";
                contentType = FileNameSuffixEnum.getEnumBySuffix(suffix).getContentType();
            }
        }
        //如果需要浏览器自动展示, 需要设置为inline, 下载则设置为attachment     https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Content-Disposition
        String dispositionValue = type + ";filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8.displayName());
        //如果有特定的展示需求, 可以设置contentType
        response.setContentType(contentType);
        response.setHeader("Content-Disposition", dispositionValue);
        response.setHeader("Content-Length", "" + fileSize);
    }
}