package com.malinouski.cache.user.dto;

public record UserResponse(
        Long id,
        String name,
        String email
) {
}
