package com.example.study_project.service;

import com.example.study_project.domain.CommentDTO;
import com.example.study_project.entity.BoardEntity;
import com.example.study_project.entity.CommentEntity;
import com.example.study_project.entity.MemberEntity;
import com.example.study_project.repository.BoardRepository;
import com.example.study_project.repository.CommentRepository;
import com.example.study_project.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;

    // 댓글 작성
    public ResponseEntity<?> save(Long boardId,
                                  CommentDTO commentDTO,
                                  String memberEmail) {
        MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);
        BoardEntity findBoard = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);


        List<CommentEntity> commentEntities = new ArrayList<>();
        if(findMember != null) {
            CommentEntity commentEntity = CommentEntity.builder()
                    .comment(commentDTO.getComments())
                    .boardEntity(findBoard)
                    .memberEntity(findMember)
                    .build();
            CommentEntity saveComment = commentRepository.save(commentEntity);
            commentEntities.add(saveComment);

            findBoard = BoardEntity.builder()
                    .boardId(findBoard.getBoardId())
                    .title(findBoard.getTitle())
                    .contents(findBoard.getContents())
                    .member(findMember)
                    .comments(commentEntities)
                    .build();
            boardRepository.save(findBoard);

            CommentDTO commentDTO1 = CommentDTO.toCommentDTO(saveComment);
            return ResponseEntity.ok().body(commentDTO1);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // 댓글 삭제
    public String remove(Long boardId,
                         Long commentId,
                         String memberEmail) {
        BoardEntity findBoard = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        CommentEntity findComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);

        boolean equalsEmail =
                findMember.getMemberEmail().equals(findComment.getMemberEntity().getMemberEmail());
        boolean equalsId = findComment.getBoardEntity().getBoardId().equals(findBoard.getBoardId());

        if(equalsEmail && equalsId) {
            commentRepository.deleteById(findComment.getId());
            return "댓글을 삭제했습니다.";
        } else {
            return "해당 이메일의 댓글이 아닙니다.";
        }
    }

    // 댓글 수정
    public ResponseEntity<?> update(Long boardId,
                                    Long commentId,
                                    CommentDTO commentDTO,
                                    String memberEmail) {
        BoardEntity findBoard = boardRepository.findById(boardId)
                .orElseThrow(EntityNotFoundException::new);

        CommentEntity findComment = commentRepository.findById(commentId)
                .orElseThrow(EntityNotFoundException::new);

        MemberEntity findMember = memberRepository.findByMemberEmail(memberEmail);

        boolean equalsEmail =
                findMember.getMemberEmail().equals(findComment.getMemberEntity().getMemberEmail());
        boolean equalsId =
                findComment.getBoardEntity().getBoardId().equals(findBoard.getBoardId());

        List<CommentEntity> commentEntities = new ArrayList<>();
        if(equalsEmail && equalsId) {
            findComment = CommentEntity.builder()
                    .id(findComment.getId())
                    .comment(commentDTO.getComments())
                    .boardEntity(findBoard)
                    .memberEntity(findMember)
                    .build();
            CommentEntity saveComment = commentRepository.save(findComment);
            commentEntities.add(saveComment);

            findBoard = BoardEntity.builder()
                    .boardId(findBoard.getBoardId())
                    .contents(findBoard.getContents())
                    .title(findBoard.getTitle())
                    .member(findBoard.getMember())
                    .comments(commentEntities)
                    .build();
            boardRepository.save(findBoard);

            CommentDTO returnComment = CommentDTO.toCommentDTO(saveComment);
            return ResponseEntity.ok().body(returnComment);
        } else {
            return ResponseEntity.badRequest().body("일치하지 않습니다.");
        }
    }
}
