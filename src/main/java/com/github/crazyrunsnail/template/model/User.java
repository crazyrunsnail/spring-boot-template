package com.github.crazyrunsnail.template.model;

import lombok.*;

import java.time.LocalDateTime;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;

    private String username;

    private String name;

    private String password;

    private String rolesArrayJson;

    private LocalDateTime loggedInAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}