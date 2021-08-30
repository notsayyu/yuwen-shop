package com.notsay.yuwenshop.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/29 15:29
 */
@Getter
@AllArgsConstructor
public enum FileNameSuffixEnum {
    /**
     * PDF
     */
    PDF("pdf", "application/pdf"),

    /**
     * jpg
     */
    JPG("jpg", "image/jpeg"),

    /**
     * jpeg
     */
    JPEG("jpeg", "image/jpeg"),

    /**
     * png
     */
    PNG("png", "image/png");
    private final String suffix;
    private final String contentType;

    private static final Map<String, FileNameSuffixEnum> SUFFIX_MAP = new HashMap<>();

    static {
        for (FileNameSuffixEnum fileNameSuffixEnum : FileNameSuffixEnum.values()) {
            SUFFIX_MAP.put(fileNameSuffixEnum.getSuffix(), fileNameSuffixEnum);
        }
    }

    public static boolean containSuffix(String suffix) {
        return SUFFIX_MAP.containsKey(suffix);
    }

    public static FileNameSuffixEnum getEnumBySuffix(String suffix) {
        return SUFFIX_MAP.get(suffix);
    }
}