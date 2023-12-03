package com.example.study_project.controller;

import com.example.study_project.domain.MemberDTO;
import com.example.study_project.domain.TokenDTO;
import com.example.study_project.service.MemberService;
import com.example.study_project.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/v1/users")
public class MemberController {

    private final MemberService memberService;
    private final TokenService tokenService;

    // 회원가입
    @PostMapping("")
    public ResponseEntity<?> join(@Validated @RequestBody MemberDTO memberDTO,
                                  BindingResult result) throws Exception{

        // 입력값 검증 예외가 발생하면 예외 메시지를 응답한다.
        if(result.hasErrors()) {
            log.info("BindingResult error : " + result.hasErrors());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getClass().getSimpleName());
        }

        try {
            ResponseEntity<?> responseEntity = memberService.signUp(memberDTO);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            log.error("예외 : " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // 회원 조회
    @GetMapping("/{memberId}")
    public ResponseEntity<MemberDTO> search(@PathVariable Long memberId) {
        ResponseEntity<MemberDTO> search = memberService.search(memberId);
        return search;
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody MemberDTO memberDTO) throws Exception {
        log.info("member : " + memberDTO);
        try {
            log.info("-----------------");

            ResponseEntity<?> login =
                    memberService.login(memberDTO.getMemberEmail(), memberDTO.getPassword());
            log.info("login : " + login);

            return ResponseEntity.ok().body(login);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("문제가 있습니다");
        }
    }

    // accessToken 받기
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String token) throws Exception {
        try {
            if(token != null) {
                ResponseEntity<?> accessToken = tokenService.createAccessToken(token);
                return ResponseEntity.ok().body(accessToken);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 로그아웃
    @GetMapping("/logOut")
    public String logOut(HttpServletRequest request,
                         HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request,
                response,
                SecurityContextHolder.getContext().getAuthentication());
        return "로그아웃하셨습니다.";
    }

    // 회원정보 수정
    @PutMapping("/api/v1/users")
    public ResponseEntity<?> update(@RequestBody MemberDTO memberDTO,
                                    @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        try {
            // 검증과 유효성이 끝난 토큰을 SecurityContext 에 저장하면
            // @AuthenticationPrincipal UserDetails userDetails 으로 받아오고 사용
            // zxzz45@naver.com 이런식으로 된다.
            String userEmail = userDetails.getUsername();
            ResponseEntity<MemberDTO> update = memberService.update(memberDTO, userEmail);
            return ResponseEntity.ok().body(update);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("잘못된 요청");
        }
    }

    // 회원 탈퇴
    @DeleteMapping("/api/v1/users/{userId}")
    public ResponseEntity<?> remove(@PathVariable Long userId) throws Exception {
        ResponseEntity<?> remove = memberService.remove(userId);
        return ResponseEntity.ok().body(remove);
    }

    @PostMapping("/email-check/{userEmail}")
    public String emailCheck(@PathVariable String userEmail) {
        log.info("userEmail : " + userEmail);
        String result = memberService.emailCheck(userEmail);
        return result;
    }

}
