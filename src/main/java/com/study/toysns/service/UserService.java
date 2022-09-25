package com.study.toysns.service;

import com.study.toysns.exception.ErrorCode;
import com.study.toysns.exception.SnsApplicationException;
import com.study.toysns.model.User;
import com.study.toysns.model.entity.UserEntity;
import com.study.toysns.repository.UserEntityRepository;
import com.study.toysns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms} ")
    private Long expiredTimeMs;

    public User loadUserByUserName(String userName) {
        return userEntityRepository.findByUserName(userName).map(User::from)
                .orElseThrow(() ->
                        new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName))
                );
    }

    @Transactional
    public User join(String userName, String password) {
        userEntityRepository.findByUserName(userName)
                .ifPresent(userEntity -> {
                    throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("%s is duplicated", userName));
                });

        UserEntity savedEntity = userEntityRepository.save(UserEntity.of(userName, encoder.encode(password)));

        return User.from(savedEntity);
    }

    public String login(String userName, String password) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", userName)));

        if (!encoder.matches(password, userEntity.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        String token = JwtTokenUtils.generateToken(userName, secretKey, expiredTimeMs);

        return token;
    }
}
