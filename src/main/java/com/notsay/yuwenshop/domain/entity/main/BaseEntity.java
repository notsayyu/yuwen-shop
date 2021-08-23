package com.notsay.yuwenshop.domain.entity.main;

import lombok.Data;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * @description: 实体基础类
 * @author: dsy
 * @date: 2021/8/13 16:40
 */
@Data
/**
 * @MappedSuperclass注解只能标注在类上
 * 标注为@MappedSuperclass的类将不是一个完整的实体类。它将不会映射到数据库表，但是它的属性都将映射到其子类的数据库表字段中
 * 标注为@MappedSuperclass的类不能再标注@Entity或@Table注解，也无需实现序列化接口。但是如果一个标注为@MappedSuperclass的类继承了
 * 另外一个实体类或者另外一个同样标注了@MappedSuperclass的类的话，他将可以使用@AttributeOverride或@AttributeOverrides注解重定义其父类(无论是否是实体类)的属性映射到数据库表中的字段。
 */
@MappedSuperclass
/**
 *使用Java Persistence API提供的乐观锁定机制。它导致在同一时间对同一数据进行多次更新不会相互干扰。为了使用OptimisticLocking，
 * 我们需要一个实体（Entity），其中包含一个带有@Version注释的属性。在使用它时，每个读取数据的事务都持有version属性的值。
 * 在事务想要进行更新之前，它将再次检查version属性。如果值在此期间发生了更改，则抛出ObjectOptimisticLockingFailureException。
 * 否则，事务提交update并递增version的值。这种机制适用于读操作比更新或删除操作多得多的应用程序
 */
@OptimisticLocking
public abstract class BaseEntity implements Serializable {
    /**
     * @GeneratedValue：
     * @GeneratedValue 用于标注主键的生成策略，通过strategy 属性指定。默认情况下，JPA 自动选择一个最适合底层数据库的主键生成策略：SqlServer对应identity，MySQL 对应 auto increment。
     * 在javax.persistence.GenerationType中定义了以下几种可供选择的策略：
     * –IDENTITY：采用数据库ID自增长的方式来自增主键字段，Oracle 不支持这种方式；
     * –AUTO： JPA自动选择合适的策略，是默认选项；
     * –SEQUENCE：通过序列产生主键，通过@SequenceGenerator 注解指定序列名，MySql不支持这种方式
     * –TABLE：通过表产生主键，框架借由表模拟序列产生主键，使用该策略可以使应用更易于数据库移植。
     */
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 数据库版本信息，Hibernate自动维护
     */
    @Version
    @Column(name = "version", columnDefinition = "int(11) default 0 comment '版本号'")
    private Integer version;

    @Column(name = "created_at", columnDefinition = "bigint comment '创建时间'")
    private Long createdAt;

    @Column(name = "updated_at", columnDefinition = "bigint comment '更新时间'")
    private Long updatedAt;

    @Column(name = "deleted", columnDefinition = "boolean default false comment '是否逻辑删除'")
    private Boolean deleted;

    @PrePersist
    public void beforeInsert() {
        createdAt = Instant.now().toEpochMilli();
        updatedAt = Instant.now().toEpochMilli();
        version = 0;
        deleted = false;
    }

    @PreUpdate
    public void beforeUpdate() {
        updatedAt = Instant.now().toEpochMilli();
    }

}