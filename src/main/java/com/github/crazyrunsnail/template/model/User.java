package com.github.crazyrunsnail.template.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;

    private String username;

    private String name;

    @JsonIgnore
    private String password;

    @JsonIgnore
    private String rolesArrayJson;

    private LocalDateTime loggedInAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}