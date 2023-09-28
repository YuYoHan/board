package com.example.study_project.service;

import com.example.study_project.config.jwt.JwtProvider;
import com.example.study_project.domain.MemberDTO;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
@Log4j2
@RequiredArgsConstructor
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final TokenRepository tokenRepository;

    // 회원가입
    public ResponseEntity<?> signUp(MemberDTO member) throws Exception {
        try {
            MemberEntity findMember = memberRepository.findByMemberEmail(member.getMemberEmail());

            if(findMember == null) {
                MemberEntity memberEntity = MemberEntity.builder()
                        .memberEmail(member.getMemberEmail())
                        .password(passwordEncoder.encode(member.getPassword()))
                        .userName(member.getUserName())
                        .role(member.getRole())
                        .build();

                memberRepository.save(memberEntity);
                MemberDTO memberDTO = MemberDTO.toMemberDTO(memberEntity);
                log.info("member : " + memberDTO);
                return ResponseEntity.ok().body(memberDTO);
            } else {
                log.info("이미 가입된 회원입니다.");
                return ResponseEntity.badRequest().body("이미 가입된 회원입니다.");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 아이디 조회
    public ResponseEntity<MemberDTO> search(Long memberId){
        MemberEntity findMember = memberRepository.findById(memberId)
                .orElseThrow(EntityNotFoundException::new);

        MemberDTO memberDTO = MemberDTO.toMemberDTO(findMember);
        return ResponseEntity.ok().body(memberDTO);
    }

    // 아이디 삭제
    public ResponseEntity<?> remove(Long memberId) throws Exception {
        try {
            ResponseEntity<MemberDTO> search = search(memberId);

            if(search != null) {
                memberRepository.deleteByMemberId(memberId);
                log.info("삭제되었습니다.");
                return ResponseEntity.ok().body("회원 탈퇴 완료");
            } else {
                throw new Exception();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
        }
    }

    // 로그인
    public ResponseEntity<?> login(String memberEmail, String password) throws Exception {
        try {
            MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);
            log.info("member : " + findMember);

            if(findMember != null) {
                if(passwordEncoder.matches(password, findMember.getPassword())) {
                    Authentication authentication = new UsernamePasswordAuthenticationToken(memberEmail, password);
                    log.info("authentication : " + authentication);

                    List<GrantedAuthority> authoritiesForUser = getAuthoritiesForUser(findMember);
                    TokenDTO token = jwtProvider.createToken(authentication, authoritiesForUser);

                    TokenEntity findToken = tokenRepository.findByMemberEmail(token.getMemberEmail());

                    if(findToken != null) {
                        log.info("이미 발급한 토큰이 있습니다.");
                        token = TokenDTO.builder()
                                .id(findToken.getId())
                                .grantType(token.getGrantType())
                                .accessToken(token.getAccessToken())
                                .refreshToken(token.getRefreshToken())
                                .memberEmail(token.getMemberEmail())
                                .build();
                        log.info("token : " + token);

                        TokenEntity tokenEntity = TokenEntity.toTokenEntity(token);
                        tokenRepository.save(tokenEntity);
                    } else {
                        log.info("신규 발급입니다.");
                        TokenEntity tokenEntity = TokenEntity.toTokenEntity(token);
                        tokenRepository.save(tokenEntity);
                    }
                    return ResponseEntity.ok().body(token);
                } else {
                    log.info("비밀번호가 일치 하지 않습니다.");
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치 하지 않습니다.");
                }
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            throw new Exception();
        }
    }
    private List<GrantedAuthority> getAuthoritiesForUser(MemberEntity member) {
        // 예시: 데이터베이스에서 사용자의 권한 정보를 조회하는 로직을 구현
        // member 객체를 이용하여 데이터베이스에서 사용자의 권한 정보를 조회하는 예시로 대체합니다.
        Role role = member.getRole();  // 사용자의 권한 정보를 가져오는 로직 (예시)

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" +role.name()));
        log.info("role in MemberService : " + role.name());
        log.info("authorities in MemberService : " + authorities);
        return authorities;
    }

    // 회원 정보 수정
    public ResponseEntity<MemberDTO> update(MemberDTO member, String memberEmail) throws Exception{
        try {
            MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);

            if(findMember != null) {
                findMember = MemberEntity.builder()
                        .memberId(findMember.getMemberId())
                        .memberEmail(findMember.getMemberEmail())
                        .password(passwordEncoder.encode(member.getPassword()))
                        .userName(member.getUserName())
                        .build();

                memberRepository.save(findMember);
                MemberDTO memberDTO = MemberDTO.toMemberDTO(findMember);
                log.info("member : " + memberDTO);
                return ResponseEntity.ok().body(memberDTO);
            } else {
                log.error("수정할 수 있는 회원이 없습니다.");
                throw new Exception();
            }
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 이메일 중복 체크
    public String emailCheck(String memberEmail) {
        MemberEntity findUser = memberRepository.findByMemberEmail(memberEmail);

        if(findUser == null) {
            return "회원가입이 가능합니다.";
        } else {
            return "중복된 회원입니다.";
        }
    }
}
