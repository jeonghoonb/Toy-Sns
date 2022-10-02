package com.study.toysns.repository;

import com.study.toysns.model.entity.PostEntity;
import com.study.toysns.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostEntityRepository extends JpaRepository<PostEntity, Long> {

    Page<PostEntity> findAllByUser(UserEntity user, Pageable pageable);

}
