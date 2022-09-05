package com.study.toysns.service;

import com.study.toysns.exception.ErrorCode;
import com.study.toysns.exception.SnsApplicationException;
import com.study.toysns.model.User;
import com.study.toysns.model.entity.UserEntity;
import com.study.toysns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;

    public User join(String userName, String password) {
        userEntityRepository.findByUserName(userName).ifPresent(userEntity -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
        });

        UserEntity savedEntity = userEntityRepository.save(UserEntity.of(userName, password));

        return User.from(savedEntity);
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName).orElseThrow(() -> new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, ""));

        if (!password.equals(userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, "");
        }

        // TODO: create token

        return "";
    }
}
