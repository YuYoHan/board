package com.example.study_project.domain;

import com.example.study_project.entity.MemberEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@ToString
@Getter
@NoArgsConstructor
public class MemberDTO {
    private Long memberId;
    @NotNull(message = "이메일은 필수 입력입니다.")
    @Pattern(regexp = "^(?:\\w+\\.?)*\\w+@(?:\\w+\\.)+\\w+$", message = "이메일 형식이 올바르지 않습니다.")
    @Email(message = "이메일 형식에 맞지 않습니다.")
    private String memberEmail;
    @NotNull(message = "이름은 필수입력입니다.")
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
