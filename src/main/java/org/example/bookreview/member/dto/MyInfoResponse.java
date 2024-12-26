package org.example.bookreview.member.dto;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.example.bookreview.member.domain.Role;

@Getter
@AllArgsConstructor
@Builder
public class MyInfoResponse {

    private Long id;
    private String name;
    private String email;
    private Set<Role> roles;
    private String provider;
}