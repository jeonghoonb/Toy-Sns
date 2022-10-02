package com.study.toysns.fixture;

import com.study.toysns.model.entity.PostEntity;
import com.study.toysns.model.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String userName, Long userId, Long postId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setUserName(userName);

        PostEntity entity = new PostEntity();
        entity.setUser(user);
        entity.setId(postId);

        return entity;
    }
}
