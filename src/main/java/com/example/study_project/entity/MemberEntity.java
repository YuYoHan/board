package com.example.study_project.entity;

import com.example.study_project.domain.Role;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "member")
@Table
@ToString
@Getter
@NoArgsConstructor
public class MemberEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long memberId;
    private String memberEmail;
    private String userName;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    @Builder
    public MemberEntity(Long memberId, String memberEmail, String userName, String password, Role role) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }
}
