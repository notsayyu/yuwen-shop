package com.notsay.yuwenshop.domain.entity.main;

import com.notsay.yuwenshop.common.enums.UserRoleEnum;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @description:
 * @author: dsy
 * @date: 2021/8/14 10:45
 */
@Entity
@Table(name = "users", indexes = {
        @Index(name = "idx_user_code", columnList = "user_code", unique = true),
        @Index(name = "idx_username", columnList = "username", unique = true),
        @Index(name = "idx_mobile", columnList = "mobile", unique = true),
})
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends BaseEntity {
    @Column(name = "user_code", nullable = false, columnDefinition = "varchar(20) comment '用户编码'")
    private String userCode;

    @Column(name = "username", nullable = false, columnDefinition = "varchar(20) comment '用户名'")
    private String username;

    @Column(name = "mobile", nullable = false, columnDefinition = "varchar(11) comment '手机号'")
    private String mobile;

    @Column(name = "pwd", nullable = false, columnDefinition = "varchar(64) comment '密码'")
    private String pwd;

    /**
     * {@link UserRoleEnum}
     */
    @Column(name = "role", nullable = false, columnDefinition = "int(1) comment '角色'")
    private Integer role;

}