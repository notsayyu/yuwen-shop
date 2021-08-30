package com.notsay.yuwenshop.biz.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/25 16:52
 */
@ApiModel("文件存储返回DTO, 可拓展")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FileStoreDTO {
    @ApiModelProperty("文件的唯一标识")
    private String fileKey;

    @ApiModelProperty("文件原名(下载时替换为原名)")
    private String originName;

    @ApiModelProperty("字节数")
    private Long byteSize;
}