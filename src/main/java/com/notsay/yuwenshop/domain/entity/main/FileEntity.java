package com.notsay.yuwenshop.domain.entity.main;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/29 15:06
 */
@Entity
@Table(name = "file", indexes = {
        @Index(name = "idx_file_key", columnList = "file_key", unique = true)
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FileEntity extends BaseEntity {
    @ApiModelProperty("文件编号")
    @Column(name = "file_key", columnDefinition = "varchar(255)")
    private String fileKey;

    @ApiModelProperty("文件原名")
    @Column(name = "file_origin_name", columnDefinition = "varchar(255)")
    private String fileOriginName;

    @ApiModelProperty("上传文件的用户id")
    @Column(name = "user_id", columnDefinition = "int(11)")
    private Integer userId;

    @ApiModelProperty("文件哈希")
    @Column(name = "file_hash", columnDefinition = "varchar(255)")
    private String fileHash;

    @ApiModelProperty("文件字节数")
    @Column(name = "byte_size", columnDefinition = "bigint")
    private Long byteSize;
}