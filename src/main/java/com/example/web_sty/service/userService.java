package com.example.web_sty.service;

import com.example.web_sty.dto.UserDTO;
import com.example.web_sty.entity.UserEntity;
import com.example.web_sty.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class userService {

    private final UserRepository userRepository;
    public void save(UserDTO userDTO) {
        // 1. dto → entity 변환
        // 2. repository의 save 메서드 호출
        UserEntity userEntity = UserEntity.toUserEntity(userDTO);
        userRepository.save(userEntity);
        // repository의 save 매서드 호출 (조건 : entity객체를 넘겨줘야 함)

    }

    public UserDTO login(UserDTO userDTO) {
        /*
        *   1. 회원이 입력한 이메일로 DB에서 조회를 함
        *   2. DB에서 조회한 비밀번호와 사용자가 입력한 비밀번호가 일치하는지 판단
        * */
        // Optional로 감싸준다.
        Optional<UserEntity> byUserEmail = userRepository.findByUserEmail(userDTO.getUserEmail());
        if(byUserEmail.isPresent()) {
            // 조회 결과가 있다.(해당 이메일을 가진 회원 정보가 있다)
            // Optional로 감싸준것을 까준다.
            UserEntity userEntity = byUserEmail.get();
            if(userEntity.getUserPw().equals(userDTO.getUserPw())) {
                // 비밀번호 일치
                // entity → dto 변환후 리턴
                UserDTO dto = UserDTO.toUserDTO(userEntity);
                return dto;
            } else {
                // 비밀번호 불일치(로그인 실패)
                return null;
            }
        } else {
            // 조회 결과가 없다(해당 이메일을 가진 회원이 없다)
            return null;
        }
    }

    public List<UserDTO> findAll() {
        List<UserEntity> userEntityList = userRepository.findAll();
        List<UserDTO> userDTOList = new ArrayList<>();

        for (UserEntity userEntity: userEntityList
             ) {
            userDTOList.add(UserDTO.toUserDTO(userEntity));
        }
        return userDTOList;
    }

    public UserDTO findById(Long id) {
        // 한개를 조회할때는 Optional로 감싸준다.
        Optional<UserEntity> byId = userRepository.findById(id);
        if(byId.isPresent()){
//            UserEntity userEntity = byId.get();
//            UserDTO userDTO = UserDTO.toUserDTO(userEntity);
//            return userDTO;
            return UserDTO.toUserDTO(byId.get());
        } else {
            return null;
        }
    }

    public UserDTO updateForm(String myEmail) {
        Optional<UserEntity> byUserEmail = userRepository.findByUserEmail(myEmail);
        if(byUserEmail.isPresent()) {
            return UserDTO.toUserDTO(byUserEmail.get());
        } else {
            return null;
        }
    }

    public void update(UserDTO userDTO) {
        userRepository.save(UserEntity.toUserEntity(userDTO));
    }

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    public String emailCheck(String userEmail) {
        Optional<UserEntity> byUserEmail = userRepository.findByUserEmail(userEmail);
        if(byUserEmail.isPresent()) {
            // 조회결과가 있다 → 사용x
            return null;
        } else {
            // 조회결과가 없다 → 사용o
            return "ok";
        }
    }
}
