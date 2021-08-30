package com.notsay.yuwenshop.common.properties;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/25 17:21
 */
@ApiModel("文件存储的相关配置")
@Component
@Getter
public class FileStoreProperties {
    @Value("${file-store.enable:false}")
    @ApiModelProperty("是否开启文件持久化")
    private boolean enable;

    @Value("${file-store.maxFileSize:5MB}")
    @ApiModelProperty("单文件大小")
    private String maxFileSize;

    @ApiModel("持久化到本地磁盘配置")
    @Component
    @Getter
    public static class DiskFileStoreProperties {

        @Value("${file-store.disk.basePath:''}")
        @ApiModelProperty("磁盘持久化的文件夹")
        private String diskStoreBasePath;
    }

    @ApiModel("持久化到minio的配置")
    @Component
    @Getter
    public static class MinioFileStoreProperties {
        @Value("${file-store.minio.host:}")
        @ApiModelProperty("minio服务的host")
        private String minioHost;

        @Value("${file-store.minio.port:9000}")
        @ApiModelProperty("minio服务的端口, 默认9000")
        private Integer minioPort;

        @Value("${file-store.minio.accessKey:}")
        @ApiModelProperty("minio服务的key")
        private String minioKey;

        @Value("${file-store.minio.accessSecret:}")
        @ApiModelProperty("minio服务的secret")
        private String minioSecret;

        @Value("${file-store.minio.bucketName:}")
        @ApiModelProperty("minio服务的bucket名称")
        private String minioBucketName;
    }

}