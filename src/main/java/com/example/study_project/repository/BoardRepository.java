package com.example.study_project.repository;

import com.example.study_project.entity.BoardEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity, Long> {
    BoardEntity deleteByBoardId(Long boardId);
    Page<BoardEntity> findAll(Pageable pageable);
    Page<BoardEntity> findByTitleContaining(String searchKeyword, Pageable pageable);
}
