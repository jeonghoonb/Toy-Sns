package com.study.toysns.model;

import com.study.toysns.model.entity.UserEntity;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class User {

    private Long id;
    private String userName;
    private String password;
    private UserRole role;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static User from(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getUserName(),
                entity.getPassword(),
                entity.getRole(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
