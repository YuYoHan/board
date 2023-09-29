package com.example.study_project.domain;

import com.example.study_project.entity.BoardEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class BoardDTO {
    private Long boardId;
    @NotNull(message = "제목은 필수 입력입니다.")
    private String title;
    private String contents;
    @NotNull(message = "작성자는 필수입니다.")
    private String writer;
    private LocalDateTime regTime;
    private LocalDateTime updateTime;

    @Builder
    public BoardDTO(Long boardId,
                    String title,
                    String contents,
                    String writer,
                    LocalDateTime regTime,
                    LocalDateTime updateTime) {
        this.boardId = boardId;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.regTime = regTime;
        this.updateTime = updateTime;
    }

    public static BoardDTO toBoardDTO(BoardEntity board) {
        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .contents(board.getContents())
                .writer(board.getCreateBy())
                .regTime(board.getRegTime())
                .updateTime(board.getUpdateTime())
                .build();
    }
}
