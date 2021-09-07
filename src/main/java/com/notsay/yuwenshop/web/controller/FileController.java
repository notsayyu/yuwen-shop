package com.notsay.yuwenshop.web.controller;

import com.notsay.yuwenshop.biz.dto.FileStoreDTO;
import com.notsay.yuwenshop.biz.service.FileService;
import com.notsay.yuwenshop.biz.service.FileStoreService;
import com.notsay.yuwenshop.common.config.security.SecureUserInfo;
import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.response.BaseResponse;
import com.notsay.yuwenshop.domain.entity.main.FileEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/24 15:26
 */
@Api(tags = "文件相关")
@RestController
@RequestMapping("/api/v1/file")
@Slf4j
public class FileController {
    final FileService fileService;
    final FileStoreService fileStoreService;

    public FileController(FileService fileService, FileStoreService fileStoreService) {
        this.fileService = fileService;
        this.fileStoreService = fileStoreService;
    }

    @ApiOperation("上传文件")
    @PostMapping("/upload")
    public BaseResponse<FileStoreDTO> uploadFile(
            @ApiParam("文件,使用表单形式,名称为file") @RequestParam("file") MultipartFile inputFile,
            @ApiIgnore @AuthenticationPrincipal SecureUserInfo userInfo) {
        FileStoreDTO fileStoreDTO = fileStoreService.saveFile(inputFile);
        fileService.uploadFile(fileStoreDTO);

        return BaseResponse.with(Code.SUCCESS, fileStoreDTO);
    }

    @ApiOperation("下载文件")
    @GetMapping("/download")
    public void downloadFile(@ApiParam("文件key") @NotNull @Length(max = 100) @RequestParam String fileKey,
                             @ApiIgnore @AuthenticationPrincipal SecureUserInfo secureUser,
                             @ApiIgnore HttpServletResponse response) {


        //todo 权限验证
        FileEntity fileEntity = fileService.getFileEntity(fileKey);
        fileStoreService.downloadFile(fileKey, fileEntity.getFileOriginName(), response);
    }

    @ApiOperation("预览文件")
    @GetMapping("/preview")
    public void previewFile(@ApiParam("文件key") @NotNull @Length(max = 100) @RequestParam String fileKey,
                            @ApiIgnore @AuthenticationPrincipal SecureUserInfo secureUser,
                            @ApiIgnore HttpServletResponse response) {


        //todo 权限验证
        FileEntity fileEntity = fileService.getFileEntity(fileKey);
        fileStoreService.previewFile(fileKey, fileEntity.getFileOriginName(), fileEntity.getByteSize(), response);
    }

    @ApiOperation("删除文件")
    @DeleteMapping("/delete")
    public BaseResponse deleteFile(@ApiParam("文件key") @NotNull @Length(max = 100) @RequestParam String fileKey,
                                   @ApiIgnore @AuthenticationPrincipal SecureUserInfo secureUser) {
        fileStoreService.deleteFile(fileKey);
        return BaseResponse.with(Code.SUCCESS);
    }
}
