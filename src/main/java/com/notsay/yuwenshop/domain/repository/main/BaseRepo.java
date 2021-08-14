package com.notsay.yuwenshop.domain.repository.main;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/14 11:07
 */

/**
 *所有对象类接口都会继承此接口 为了告诉JPA不要创建对应接口的bean对象 就在类上加注解@NoRepositoryBean
 * 这样spring容器中就不会有BaseReposytory接口的bean对象
 */
@NoRepositoryBean
public interface BaseRepo<T, ID> extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {
}