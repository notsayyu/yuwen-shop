package com.notsay.yuwenshop.biz.service.impl;

import com.notsay.yuwenshop.biz.dto.FileStoreDTO;
import com.notsay.yuwenshop.biz.service.FileStoreService;
import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.excepion.BusinessException;
import com.notsay.yuwenshop.common.properties.FileStoreProperties;
import com.notsay.yuwenshop.common.utils.FileUtil;
import com.notsay.yuwenshop.common.utils.UuidUtil;
import lombok.Cleanup;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/25 16:56
 */
@Service
@Slf4j
@ConditionalOnProperty(prefix = "file-store", name = "type", havingValue = "disk")
public class DiskFileStoreServiceImpl implements FileStoreService {
    private final String basePath;
    private final long maxFileSize;

    public DiskFileStoreServiceImpl(FileStoreProperties fileStoreProperties, FileStoreProperties.DiskFileStoreProperties diskFileStoreProperties) throws IOException {
        basePath = diskFileStoreProperties.getDiskStoreBasePath();
        String maxFileSizeStr = fileStoreProperties.getMaxFileSize();
        maxFileSize = DataSize.parse(maxFileSizeStr).toBytes();
        log.info("文件存储采用本地磁盘存储的方式, 文件存储路径为:[{}], 单文件最大大小为:[{}]", basePath, maxFileSizeStr);
        //如果存储目录不存在, 则创建
        Path path = Paths.get(basePath);
        if (Files.notExists(path)) {
            log.info("采用本地磁盘存储的方式存储文件时, 文件夹不存在, 准备创建");
            Files.createDirectories(path);
            log.info("采用本地磁盘存储的方式存储文件时, 文件夹不存在, 创建成功");
        }
    }


    @Override
    public FileStoreDTO saveFile(MultipartFile inputFile) {
        try {
            @Cleanup InputStream inputStream = inputFile.getInputStream();
            String extension = FileUtil.getExtension(inputFile.getOriginalFilename());
            String fileId = UuidUtil.getUUID() + extension;
            Path targetFilePath = Paths.get(basePath, fileId);
            if (Files.notExists(targetFilePath)) {
                Files.createFile(targetFilePath);
                @Cleanup OutputStream outputStream = Files.newOutputStream(targetFilePath);
                IOUtils.copy(inputStream, outputStream);
                return new FileStoreDTO(fileId, inputFile.getOriginalFilename(), inputFile.getSize());
            } else {
                log.error("存储文件失败，自动生成的文件id已存在，文件id为:{}", fileId);
                throw new BusinessException(Code.FILE_ID_EXISTS);
            }

        } catch (IOException e) {
            log.error("长传文件过程中发生IO异常", e);
            throw new BusinessException(Code.DISK_SAVE_ERROR);
        }
    }

    @Override
    public void downloadFile(String fileId, String fileOriginName, HttpServletResponse response) {
        try {
            Path path = Paths.get(basePath, fileId);
            if (Files.notExists(path)) {
                log.error("下载文件时, 文件id不存在, id为:[{}]", fileId);
                throw new BusinessException(Code.FILE_ID_EXISTS);
            }
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileOriginName, StandardCharsets.UTF_8.displayName()));
            response.setHeader("Content-Length", "" + Files.size(path));
            @Cleanup InputStream inputStream = Files.newInputStream(path);
            @Cleanup ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);

        } catch (IOException e) {
            log.error("下载文件时发生io异常", e);
            throw new BusinessException(Code.DISK_DOWNLOAD_ERROR);
        }
    }

    @Override
    public void previewFile(String fileKey, String fileOriginName, Long byteSize, HttpServletResponse response) {
        try {
            Path path = Paths.get(basePath, fileKey);
            if (Files.notExists(path)) {
                log.error("下载文件时, 文件id不存在, id为:[{}]", fileKey);
                throw new BusinessException(Code.FILE_NOT_EXIST);
            }
            setHeader(response, fileOriginName, byteSize);
            @Cleanup InputStream inputStream = Files.newInputStream(path);
            @Cleanup ServletOutputStream outputStream = response.getOutputStream();
            IOUtils.copy(inputStream, outputStream);
        } catch (IOException e) {
            log.error("下载文件时发生io异常", e);
            throw new BusinessException(Code.DISK_DOWNLOAD_ERROR);
        }
    }
}