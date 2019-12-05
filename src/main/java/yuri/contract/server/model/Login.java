package yuri.contract.server.model;

import lombok.Data;

@Data
public class Login {
    private String username;

    private String token;
}
