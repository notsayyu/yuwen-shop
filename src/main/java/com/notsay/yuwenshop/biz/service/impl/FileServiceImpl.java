package com.notsay.yuwenshop.biz.service.impl;

import com.notsay.yuwenshop.biz.dto.FileStoreDTO;
import com.notsay.yuwenshop.biz.service.FileService;
import com.notsay.yuwenshop.biz.service.FileStoreService;
import com.notsay.yuwenshop.common.enums.Code;
import com.notsay.yuwenshop.common.excepion.BusinessException;
import com.notsay.yuwenshop.domain.entity.main.FileEntity;
import com.notsay.yuwenshop.domain.repository.main.FileRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/25 16:55
 */
@Service
@Slf4j
public class FileServiceImpl implements FileService {
    final FileStoreService fileStoreService;
    final FileRepo fileRepo;

    public FileServiceImpl(FileStoreService fileStoreService, FileRepo fileRepo) {
        this.fileStoreService = fileStoreService;
        this.fileRepo = fileRepo;
    }

    @Override
    public void uploadFile(FileStoreDTO fileStoreDTO) {
        FileEntity fileEntity = FileEntity.builder()
                .fileKey(fileStoreDTO.getFileKey())
                .fileOriginName(fileStoreDTO.getOriginName())
                .byteSize(fileStoreDTO.getByteSize())
                .build();
        fileRepo.save(fileEntity);
    }

    @Override
    public FileEntity getFileEntity(String fileKey) {
        FileEntity fileEntity = fileRepo.findByFileKey(fileKey);
        if (Objects.isNull(fileEntity)) {
            throw new BusinessException(Code.FILE_NOT_EXIST);
        }
        return fileEntity;
    }


}