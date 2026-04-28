package com.scholarship.platform.dto;

import com.scholarship.platform.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String address;
    private String institution;
    private String major;
    private Double gpa;
    private String bio;
    private User.Role role;
    private User.UserStatus status;
    private LocalDateTime createdAt;

    public static UserDto from(User user) {
        return UserDto.builder()
            .id(user.getId())
            .firstName(user.getFirstName())
            .lastName(user.getLastName())
            .email(user.getEmail())
            .phone(user.getPhone())
            .address(user.getAddress())
            .institution(user.getInstitution())
            .major(user.getMajor())
            .gpa(user.getGpa())
            .bio(user.getBio())
            .role(user.getRole())
            .status(user.getStatus())
            .createdAt(user.getCreatedAt())
            .build();
    }
}
