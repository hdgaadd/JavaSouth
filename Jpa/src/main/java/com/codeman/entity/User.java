package com.codeman.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author hdgaadd
 * Created on 2022/01/31
 */
@Data
@Entity
@Table(name = "store_source_bind")
@EntityListeners(AuditingEntityListener.class)
public class User {
    @Id
    Long id;

    @Column(name = "user_name")
    String username;

    @Column(name = "create_time")
    @CreatedDate
    Date createTime;
}
