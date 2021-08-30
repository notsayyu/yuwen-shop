package com.notsay.yuwenshop.biz.service;

import com.notsay.yuwenshop.biz.dto.FileStoreDTO;
import com.notsay.yuwenshop.domain.entity.main.FileEntity;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/25 16:54
 */
public interface FileService {

    void uploadFile(FileStoreDTO fileStoreDTO);

    FileEntity getFileEntity(String fileKey);


}