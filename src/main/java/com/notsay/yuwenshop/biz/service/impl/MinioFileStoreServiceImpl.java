package com.notsay.yuwenshop.biz.service.impl;

import com.notsay.yuwenshop.biz.dto.FileStoreDTO;
import com.notsay.yuwenshop.biz.service.FileStoreService;
import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.excepion.BusinessException;
import com.notsay.yuwenshop.common.properties.FileStoreProperties;
import com.notsay.yuwenshop.common.utils.FileUtil;
import com.notsay.yuwenshop.common.utils.UuidUtil;
import com.notsay.yuwenshop.domain.entity.main.FileEntity;
import com.notsay.yuwenshop.domain.repository.main.FileRepo;
import io.minio.*;
import io.minio.errors.*;
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
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/31 15:48
 */
@Service
@Slf4j
@ConditionalOnProperty(prefix = "file-store", name = "type", havingValue = "minio")
public class MinioFileStoreServiceImpl implements FileStoreService {

    private final MinioClient minioClient;
    private final String defaultBucketName;
    private final long maxFileSize;
    private final FileRepo fileRepo;

    public MinioFileStoreServiceImpl(FileStoreProperties fileStoreProperties, FileStoreProperties.MinioFileStoreProperties minioFileStoreProperties, FileRepo fileRepo) {
        this.fileRepo = fileRepo;
        log.info("文件存储采用minio的方式");
        MinioClient.Builder builder = new MinioClient.Builder();
        builder.endpoint(minioFileStoreProperties.getMinioHost(), minioFileStoreProperties.getMinioPort(), false)
                .credentials(minioFileStoreProperties.getMinioKey(), minioFileStoreProperties.getMinioSecret());
        minioClient = builder.build();
        defaultBucketName = minioFileStoreProperties.getMinioBucketName();

        try {
            BucketExistsArgs bucketExistsArgs = new BucketExistsArgs.Builder().bucket(defaultBucketName)
                    .build();
            if (!minioClient.bucketExists(bucketExistsArgs)) {
                log.info("minio中bucket不存在, 尝试创建");
                MakeBucketArgs makeBucketArgs = new MakeBucketArgs.Builder()
                        .bucket(defaultBucketName)
                        .build();
                minioClient.makeBucket(makeBucketArgs);
            }
        } catch (ErrorResponseException | io.minio.errors.InsufficientDataException | io.minio.errors.InternalException
                | InvalidKeyException | InvalidResponseException | IOException
                | NoSuchAlgorithmException | io.minio.errors.ServerException | XmlParserException e) {
            log.error("初始化minio, 检查并创建bucket失败", e);
            throw new BusinessException(Code.INIT_ERROR, "初始化minio失败");
        }
        String maxFileSizeStr = fileStoreProperties.getMaxFileSize();
        maxFileSize = DataSize.parse(maxFileSizeStr).toBytes();
        log.info("文件存储采用minio存储的方式, 单文件最大大小为:[{}]", maxFileSizeStr);

    }

    @Override
    public FileStoreDTO saveFile(MultipartFile inputFile) {
        String extension = FileUtil.getExtension(inputFile.getOriginalFilename());
        String fileId = UuidUtil.getUUID() + extension;
        try {
            @Cleanup InputStream inputStream = inputFile.getInputStream();
            PutObjectArgs putObjectArgs = new PutObjectArgs.Builder()
                    .bucket(defaultBucketName)
                    .object(fileId)
                    .stream(inputStream, inputFile.getSize(), -1)
                    .build();
            minioClient.putObject(putObjectArgs);
        } catch (ErrorResponseException | io.minio.errors.InsufficientDataException | InternalException
                | InvalidKeyException | InvalidResponseException | IOException
                | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("minio上传文件失败", e);
            throw new BusinessException(Code.MINIO_UPLOAD_ERROR);
        }
        return new FileStoreDTO(fileId, inputFile.getOriginalFilename(), inputFile.getSize());
    }

    @Override
    public void downloadFile(String fileKey, String fileOriginName, HttpServletResponse response) {
        StatObjectArgs statObjectArgs = new StatObjectArgs.Builder().bucket(defaultBucketName).object(fileKey).build();
        GetObjectArgs getObjectArgs = new GetObjectArgs.Builder().bucket(defaultBucketName).object(fileKey).build();
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(statObjectArgs);
            long fileSize = statObjectResponse.size();
            @Cleanup InputStream inputStream = minioClient.getObject(getObjectArgs);
            ServletOutputStream outputStream = response.getOutputStream();
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileOriginName, StandardCharsets.UTF_8.displayName()));
            response.setHeader("Content-Length", "" + fileSize);
            IOUtils.copy(inputStream, outputStream);
        } catch (ErrorResponseException | InsufficientDataException | InternalException
                | InvalidKeyException | InvalidResponseException | IOException
                | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("文件下载失败", e);
        }

    }

    @Override
    public void previewFile(String fileKey, String fileOriginName, Long byteSize, HttpServletResponse response) {
        StatObjectArgs statObjectArgs = new StatObjectArgs.Builder().bucket(defaultBucketName).object(fileKey).build();
        GetObjectArgs getObjectArgs = new GetObjectArgs.Builder().bucket(defaultBucketName).object(fileKey).build();
        try {
            StatObjectResponse statObjectResponse = minioClient.statObject(statObjectArgs);
            long fileSize = statObjectResponse.size();
            @Cleanup InputStream inputStream = minioClient.getObject(getObjectArgs);
            ServletOutputStream outputStream = response.getOutputStream();
            setHeader(response, fileOriginName, fileSize);
            IOUtils.copy(inputStream, outputStream);
        } catch (ErrorResponseException | InsufficientDataException | InternalException
                | InvalidKeyException | InvalidResponseException | IOException
                | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            log.error("文件下载失败", e);
        }
    }

    @Override
    public void deleteFile(String fileKey) {
        try {
            minioClient.removeObject(RemoveObjectArgs.builder().bucket(defaultBucketName).object(fileKey).build());
            FileEntity fileEntity = fileRepo.findByFileKey(fileKey);
            if (Objects.isNull(fileEntity)) {
                throw new BusinessException(Code.FILE_NOT_EXIST, "文件记录再数据库中不存在");
            }
            fileRepo.delete(fileEntity);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            throw new BusinessException(Code.MINIO_DELETE_ERROR);
        }
    }
}