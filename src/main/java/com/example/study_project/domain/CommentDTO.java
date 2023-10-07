package com.example.study_project.domain;

import com.example.study_project.entity.CommentEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
public class CommentDTO {
    private Long id;
    private String comments;
    private LocalDateTime regTime;
    private String email;

    @Builder
    public CommentDTO(Long id, String comments, LocalDateTime regTime, String email) {
        this.id = id;
        this.comments = comments;
        this.regTime = regTime;
        this.email = email;
    }

    public static CommentDTO toCommentDTO(CommentEntity commentEntity) {
        return CommentDTO.builder()
                .id(commentEntity.getId())
                .comments(commentEntity.getComment())
                .regTime(LocalDateTime.now())
                .email(commentEntity.getMemberEntity().getMemberEmail())
                .build();
    }
}
