package com.example.study_project.controller;

import com.example.study_project.domain.CommentDTO;
import com.example.study_project.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boards")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/{boardId}/comments")
    public ResponseEntity<?> save(@PathVariable Long boardId,
                                  @RequestBody CommentDTO commentDTO,
                                  @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ResponseEntity<?> save = commentService.save(boardId, commentDTO, email);
        return ResponseEntity.ok().body(save);
    }

    // 댓글 삭제
    @DeleteMapping("/{boardId}/{commentId}")
    public String remove(@PathVariable Long boardId,
                         @PathVariable Long commentId,
                         @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        String remove = commentService.remove(boardId, commentId, email);
        return remove;
    }

    // 댓글 수정
    @PutMapping("/{boardId}/{commentId}")
    public ResponseEntity<?> update(@PathVariable Long boardId,
                                    @PathVariable Long commentId,
                                    @RequestBody CommentDTO commentDTO,
                                    @AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername();
        ResponseEntity<?> update = commentService.update(boardId, commentId, commentDTO, email);
        return ResponseEntity.ok().body(update);
    }
}
