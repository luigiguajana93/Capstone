package it.epicode.capstone.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class RegisterUserDTO {
    String nome;
    String cognome;
    String username;
    String email;
    String password;
    String avatar;
}
