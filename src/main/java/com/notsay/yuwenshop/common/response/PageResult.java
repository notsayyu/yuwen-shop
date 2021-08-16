package com.notsay.yuwenshop.common.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.domain.Page;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;


@Data
@EqualsAndHashCode
@AllArgsConstructor
@SuppressWarnings("unchecked")
public class PageResult<T> {

    /**
     * 总条数
     */
    private long total;

    /**
     * 查出的资源条目数
     */
    private int count;

    private List<T> items;

    public PageResult(long total, List items) {
        this.total = total;
        if (items == null) {
            items = Collections.EMPTY_LIST;
        }
        this.items = items;
        count = items.size();
    }

    public PageResult(List<T> list) {
        if (CollectionUtils.isEmpty(list)) {
            items = Collections.EMPTY_LIST;
        } else {
            total = list.size();
            items = list;
            count = list.size();
        }
    }

    public PageResult() {

    }

    public PageResult(Page<T> page) {
        total = page.getTotalElements();
        count = page.getContent().size();
        items = page.getContent();
    }

}

