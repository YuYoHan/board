package com.example.study_project.entity;

import com.example.study_project.domain.TokenDTO;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Entity(name = "token")
@Table
@NoArgsConstructor
@Getter
@ToString
public class TokenEntity {
    @Id @GeneratedValue
    @Column(name = "token_id")
    private Long id;

    private String grantType;
    private String accessToken;
    private String refreshToken;
    private String memberEmail;

    @Builder
    public TokenEntity(Long id, String grantType, String accessToken, String refreshToken, String memberEmail) {
        this.id = id;
        this.grantType = grantType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.memberEmail = memberEmail;
    }

    public static TokenEntity toTokenEntity(TokenDTO tokenDTO) {
        return TokenEntity.builder()
                .id(tokenDTO.getId())
                .grantType(tokenDTO.getGrantType())
                .accessToken(tokenDTO.getAccessToken())
                .refreshToken(tokenDTO.getRefreshToken())
                .memberEmail(tokenDTO.getMemberEmail())
                .build();
    }
}
