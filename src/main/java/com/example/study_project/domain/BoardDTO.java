package com.example.study_project.domain;

import com.example.study_project.entity.BoardEntity;
import com.example.study_project.entity.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
    private List<CommentDTO> commentDTOS = new ArrayList<>();

    @Builder
    public BoardDTO(Long boardId,
                    String title,
                    String contents,
                    String writer,
                    LocalDateTime regTime,
                    LocalDateTime updateTime,
                    List<CommentDTO> commentDTOS) {
        this.boardId = boardId;
        this.title = title;
        this.contents = contents;
        this.writer = writer;
        this.regTime = regTime;
        this.updateTime = updateTime;
        this.commentDTOS = commentDTOS;
    }

    public static BoardDTO toBoardDTO(BoardEntity board) {

        List<CommentEntity> comments = board.getComments();
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for(CommentEntity commentEntity : comments) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity);
            commentDTOList.add(commentDTO);
        }

        return BoardDTO.builder()
                .boardId(board.getBoardId())
                .title(board.getTitle())
                .contents(board.getContents())
                .writer(board.getCreateBy())
                .regTime(board.getRegTime())
                .updateTime(board.getUpdateTime())
                .commentDTOS(commentDTOList)
                .build();
    }
}
