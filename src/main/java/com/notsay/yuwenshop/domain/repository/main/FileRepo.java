package com.notsay.yuwenshop.domain.repository.main;

import com.notsay.yuwenshop.domain.entity.main.FileEntity;
import org.springframework.stereotype.Repository;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/14 11:22
 */
@Repository
public interface FileRepo extends BaseRepo<FileEntity, Integer> {
    FileEntity findByFileKey(String fileKey);
}