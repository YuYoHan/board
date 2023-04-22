package com.example.web_sty.entity;

import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;


// 시간정보를 다루는 곳


@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
public class BaseEntity {
    // 생성된 시간
    @CreationTimestamp
    // 수정시에는 관여하지 않음
    @Column(updatable = false)
    private LocalDateTime createdTime;

    // 업데이트가 발생했을 때 시간
    @UpdateTimestamp
    // 입력시에는 관여하지 않음
    @Column(insertable = false)
    private LocalDateTime updatedTime;
}
