package com.example.study_project.service;

import com.example.study_project.domain.BoardDTO;
import com.example.study_project.domain.CommentDTO;
import com.example.study_project.entity.BoardEntity;
import com.example.study_project.entity.MemberEntity;
import com.example.study_project.repository.BoardRepository;
import com.example.study_project.repository.CommentRepository;
import com.example.study_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Log4j2
@Transactional
public class BoardService {
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final CommentRepository commentRepository;

    // 게시글 등록
    public ResponseEntity<?> create(BoardDTO boardDTO, String memberEmail) throws Exception {
        try {
            MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);

            if (findMember != null) {
                BoardEntity boardEntity = BoardEntity.builder()
                        .title(boardDTO.getTitle())
                        .contents(boardDTO.getContents())
                        .member(findMember)
                        .build();
                BoardEntity save = boardRepository.save(boardEntity);
                BoardDTO boardDTO1 = BoardDTO.toBoardDTO(save);
                return ResponseEntity.ok().body(boardDTO1);
            } else {
                log.error("회원이 없습니다.");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 게시글 상세 정보
    @Transactional(readOnly = true)
    public ResponseEntity<BoardDTO> getBoard(Long boardId) throws Exception {
        try {
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);

            List<CommentDTO> comments = findBoard.getComments()
                    .stream()
                    .map(CommentDTO::toCommentDTO)
                    .collect(Collectors.toList());

            BoardDTO boardDTO = BoardDTO.builder()
                    .boardId(boardId)
                    .title(findBoard.getTitle())
                    .contents(findBoard.getContents())
                    .writer(findBoard.getCreateBy())
                    .regTime(findBoard.getRegTime())
                    .updateTime(findBoard.getUpdateTime())
                    .commentDTOS(comments)
                    .build();

            log.info("board : " + boardDTO);
            return ResponseEntity.ok().body(boardDTO);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 게시글 수정
    public ResponseEntity<?> update(Long boardId,
                                    BoardDTO boardDTO,
                                    String memberEmail) throws Exception {
        try {
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);
            log.info("time : " + findBoard.getRegTime());

            MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);

            if (findMember.getMemberEmail().equals(findBoard.getMember().getMemberEmail())) {
                BoardEntity boardEntity = BoardEntity.builder()
                        .boardId(findBoard.getBoardId())
                        .title(boardDTO.getTitle())
                        .contents(boardDTO.getContents())
                        .member(findMember)
                        .comments(findBoard.getComments())
                        .build();
                BoardEntity save = boardRepository.save(boardEntity);
                log.info("saveTime : " + save.getUpdateTime());

//                List<CommentDTO> comments = findBoard.getComments()
//                        .stream()
//                        .map(CommentDTO::toCommentDTO)
//                        .collect(Collectors.toList());
//
//                BoardDTO boardDTO1 = BoardDTO.builder()
//                        .boardId(findBoard.getBoardId())
//                        .title(save.getTitle())
//                        .contents(save.getContents())
//                        .writer(memberEmail)
//                        .updateTime(LocalDateTime.now())
//                        .commentDTOS(comments)
//                        .build();

                BoardDTO returnBoard = BoardDTO.toBoardDTO(save);

                return ResponseEntity.ok().body(returnBoard);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("회원이 없습니다.");
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // 게시글 삭제
    public String remove(Long boardId, String memberEmail) throws Exception{
        try {
            BoardEntity findBoard = boardRepository.findById(boardId)
                    .orElseThrow(EntityNotFoundException::new);

            MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);
            if(findMember.getMemberEmail().equals(findBoard.getMember().getMemberEmail())) {
                boardRepository.deleteByBoardId(boardId);
                return "게시물을 삭제했습니다.";
            } else {
                return "해당 유저의 게시글이 아닙니다.";
            }
        } catch (EntityNotFoundException e) {
            return "게시물이 없습니다.";
        }
    }

    // 전체 게시글 보여주기
    public Page<BoardDTO> getBoards(Pageable pageable) {
        Page<BoardEntity> all = boardRepository.findAll(pageable);
        return all.map(BoardDTO::toBoardDTO);
    }

    // 검색했을 때 게시물 보여주기
    public Page<BoardDTO> getSearchBoards(Pageable pageable, String searchKeyword) {
        Page<BoardEntity> searchBoards = boardRepository.findByTitleContaining(searchKeyword, pageable);
        return searchBoards.map(BoardDTO::toBoardDTO);
    }

}
