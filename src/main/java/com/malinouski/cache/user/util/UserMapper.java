package com.malinouski.cache.user.util;

import com.malinouski.cache.user.dto.UserRequest;
import com.malinouski.cache.user.dto.UserResponse;
import com.malinouski.cache.user.model.User;
import org.springframework.stereotype.Service;

@Service
public class UserMapper {
    public User toUser(UserRequest request) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .build();
    }

    public UserResponse toUserResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
