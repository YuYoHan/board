package com.example.study_project.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "board")
@ToString
@Table
@Getter
@NoArgsConstructor
public class BoardEntity extends BaseEntity{
    @Id @GeneratedValue
    @Column(name = "board_id")
    private Long boardId;
    private String title;
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL)
    // 댓글 정렬
    @OrderBy("id asc")
    private List<CommentEntity> comments = new ArrayList<>();

    @Builder
    public BoardEntity(Long boardId,
                       String title,
                       String contents,
                       MemberEntity member,
                       List<CommentEntity> comments) {
        this.boardId = boardId;
        this.title = title;
        this.contents = contents;
        this.member = member;
        this.comments = comments;
    }
}
