package com.example.study_project.domain;

import com.example.study_project.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Getter
@NoArgsConstructor
public class MemberDTO {
    private Long memberId;
    private String memberEmail;
    private String userName;
    private String password;
    private Role role;

    @Builder
    public MemberDTO(Long memberId, String memberEmail, String userName, String password, Role role) {
        this.memberId = memberId;
        this.memberEmail = memberEmail;
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public static MemberDTO toMemberDTO(MemberEntity memberEntity) {
        return MemberDTO.builder()
                .memberId(memberEntity.getMemberId())
                .memberEmail(memberEntity.getMemberEmail())
                .userName(memberEntity.getUserName())
                .password(memberEntity.getPassword())
                .role(memberEntity.getRole())
                .build();
    }
}
