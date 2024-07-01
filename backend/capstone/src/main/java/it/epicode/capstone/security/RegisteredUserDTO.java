package it.epicode.capstone.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class RegisteredUserDTO {
    Long id;
    String nome;
    String cognome;
    String username;
    String email;
    private List<Roles> roles;

    }
