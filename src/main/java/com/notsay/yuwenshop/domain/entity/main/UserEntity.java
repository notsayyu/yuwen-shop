package com.notsay.yuwenshop.domain.entity.main;

import com.notsay.yuwenshop.common.enums.UserRoleEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/14 10:45
 */
@Entity
@Table(name = "users")
@Data
@EqualsAndHashCode(callSuper = true)
public class UserEntity extends BaseEntity {
    @Column(name = "username", nullable = false, columnDefinition = "varchar(20) comment '用户名'")
    private String username;

    @Column(name = "mobile", nullable = false, columnDefinition = "varchar(11) comment '手机号'")
    private String mobile;

    @Column(name = "pwd", nullable = false, columnDefinition = "varchar(255) comment '密码'")
    private String pwd;

    /**
     * {@link UserRoleEnum}
     */
    @Column(name = "role", nullable = false, columnDefinition = "int(1) comment '角色'")
    private Integer role;

}