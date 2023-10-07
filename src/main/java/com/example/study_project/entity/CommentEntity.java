package com.example.study_project.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity(name = "comment")
@Table
@Getter
@NoArgsConstructor
public class CommentEntity extends BaseTimeEnity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;
    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private BoardEntity boardEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity memberEntity;

    @Builder
    public CommentEntity(Long id, String comment, BoardEntity boardEntity, MemberEntity memberEntity) {
        this.id = id;
        this.comment = comment;
        this.boardEntity = boardEntity;
        this.memberEntity = memberEntity;
    }
}
