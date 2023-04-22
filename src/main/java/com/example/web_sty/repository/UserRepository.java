package com.example.web_sty.repository;

import com.example.web_sty.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
    // 이메일로 회원 정보 조회 (select * from user where user_email=?)
    // Optional : null 방지, null이여도 에러가 발생하지 않는다.
    Optional<UserEntity> findByUserEmail(String userEmail);

}
