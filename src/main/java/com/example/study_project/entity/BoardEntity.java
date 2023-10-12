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
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "board_id")
    private Long boardId;
    private String title;
    private String contents;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private MemberEntity member;

    //  @OneToMany 관계에서 orphanRemoval = true를 사용하면,
    //  부모 엔티티(게시글)를 삭제할 때 자동으로 관련된 자식 엔티티(댓글)도 삭제됩니다.
    // 즉, 게시글을 삭제하면 해당 게시글과 관련된 모든 댓글이 자동으로 데이터베이스에서 제거됩니다.
    @OneToMany(mappedBy = "boardEntity", cascade = CascadeType.ALL, orphanRemoval = true)
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
