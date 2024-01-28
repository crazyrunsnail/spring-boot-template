package com.github.crazyrunsnail.template.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@EqualsAndHashCode
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