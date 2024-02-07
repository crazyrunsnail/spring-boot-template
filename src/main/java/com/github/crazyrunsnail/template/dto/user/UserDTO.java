package com.github.crazyrunsnail.template.dto.user;

import lombok.Builder;
import lombok.Data;

import java.util.List;


@Data
@Builder
public class UserDTO {

    private Long id;

    private String username;

    private String name;

    private List<String> roles;
}
