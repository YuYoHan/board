package com.example.study_project.controller;

import com.example.study_project.domain.BoardDTO;
import com.example.study_project.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@Log4j2
@RequestMapping("/api/v1/boards")
public class BoardController {
    private final BoardService boardService;

    // 게시물 등록
    @PostMapping("")
    public ResponseEntity<?> create(@Validated @RequestBody BoardDTO boardDTO,
                                    BindingResult result,
                                    @AuthenticationPrincipal UserDetails userDetails) throws Exception {
        try {
            if(result.hasErrors()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getClass().getSimpleName());
            }

            String email = userDetails.getUsername();
            ResponseEntity<?> responseEntity = boardService.create(boardDTO, email);
            return ResponseEntity.ok().body(responseEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // 게시글 상세 정보
    @GetMapping("/{boardId}")
    public ResponseEntity<BoardDTO> getItem(@PathVariable Long boardId) throws Exception{
        try {
            ResponseEntity<BoardDTO> board = boardService.getBoard(boardId);
            return board;
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 전체 게시글 보기
    @GetMapping("")
    public ResponseEntity<?> getItems(
            @PageableDefault(sort = "boardId", direction = Sort.Direction.DESC)Pageable pageable
            ) {
        Page<BoardDTO> board = boardService.getBoards(pageable);
        Map<String, Object> boards = new HashMap<>();
        boards.put("boards", board.getContent());
        boards.put("nowPageNumber", board.getNumber());
        boards.put("totalPage", board.getTotalPages());
        boards.put("pageSize",board.getSize());
        boards.put("hasNextPage", board.hasNext());
        boards.put("hasPreviousPage", board.hasPrevious());
        boards.put("isFirstPage", board.isFirst());
        boards.put("isLastPage", board.isLast());
        return  ResponseEntity.ok().body(boards);
    }

    // 게시글 수정
    @PutMapping("/{boardId}")
    public ResponseEntity<?> update(@PathVariable Long boardId,
                                    @RequestBody BoardDTO boardDTO,
                                    @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        try {
            String email = userDetails.getUsername();
            ResponseEntity<?> update = boardService.update(boardId, boardDTO, email);
            return ResponseEntity.ok().body(update);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    // 게시글 삭제
    @DeleteMapping("/{boardId}")
    public String delete(@PathVariable Long boardId,
                                    @AuthenticationPrincipal UserDetails userDetails) throws Exception{
        try {
            String email = userDetails.getUsername();
            String remove = boardService.remove(boardId, email);
            return "삭제했습니다.";
        } catch (Exception e) {
            return "삭제 실패";
        }
    }
}
