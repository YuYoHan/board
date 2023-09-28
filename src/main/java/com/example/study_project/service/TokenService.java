package com.example.study_project.service;

import com.example.study_project.config.jwt.JwtProvider;
import com.example.study_project.domain.Role;
import com.example.study_project.domain.TokenDTO;
import com.example.study_project.entity.MemberEntity;
import com.example.study_project.entity.TokenEntity;
import com.example.study_project.repository.MemberRepository;
import com.example.study_project.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Log4j2
public class TokenService {
    private final TokenRepository tokenRepository;
    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    public ResponseEntity<?> createAccessToken(String refreshToken) {
        if(jwtProvider.validateToken(refreshToken)) {
            TokenEntity findToken = tokenRepository.findByRefreshToken(refreshToken);

            String memberEmail = findToken.getMemberEmail();
            MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);
            List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findMember);
            TokenDTO accessToken = jwtProvider.createAccessToken(memberEmail, authoritiesForUser);
            log.info("accessToken : " + accessToken);

            TokenEntity tokenEntity = TokenEntity.toTokenEntity(accessToken);
            tokenRepository.save(tokenEntity);

            return ResponseEntity.ok().body(accessToken);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("유효한 토큰이 아닙니다.");
        }
    }

    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity byUserEmail) {
        // 예시: 데이터베이스에서 사용자의 권한 정보를 조회하는 로직을 구현
        // member 객체를 이용하여 데이터베이스에서 사용자의 권한 정보를 조회하는 예시로 대체합니다.
        Role role = byUserEmail.getRole();  // 사용자의 권한 정보를 가져오는 로직 (예시)

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" +role.name()));
        log.info("role : " + role.name());
        log.info("authorities : " + authorities);
        return authorities;
    }
}
