package com.example.study_project.repository;

import com.example.study_project.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<MemberEntity, Long> {
    MemberEntity findByMemberEmail(String email);
    MemberEntity deleteByMemberId(Long id);
}
