package it.epicode.capstone.utenti;

import it.epicode.capstone.security.Roles;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class UtenteRequestDTO {

    @NotBlank(message = "Il tuo nome non può essere vuoto")
    private String nome;

    @NotBlank(message = "Il tuo cognome non può essere vuoto")
    private String cognome;

    @NotBlank(message = "Lo username non può contenere solo spazi vuoti")
    @Size(max = 50, message = "Il tuo username è troppo lungo, max 50 caratteri")
    private String username;

    @Email(message = "Inserisci una email valida")
    private String email;

    @NotBlank(message = "La password non può contenere solo spazi vuoti")
    @Size(max = 125, message = "La password è troppo lunga, max 125 caratteri")
    private String password;

    private String citta;
    private String tipoIndirizzo;
    private String indirizzo;
    private String civico;
    private String cap;
    private String numeroTelefono;
    //private List<Roles> roles;





}
