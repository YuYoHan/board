package com.example.study_project.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

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

    @Builder
    public BoardEntity(Long boardId, String title, String contents, MemberEntity member) {
        this.boardId = boardId;
        this.title = title;
        this.contents = contents;
        this.member = member;
    }
}
