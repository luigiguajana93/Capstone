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

    @NotBlank(message = "La citta non può contenere solo spazi vuoti")
    @Size(max = 125, message = "La citta è troppo lunga, max 125 caratteri")
    private String citta;

    @NotBlank(message = "Il tipo di indirizzo non può contenere solo spazi vuoti")
    @Size(max = 125, message = "Il tipo di indirizzo è troppo lunga, max 125 caratteri")
    private String tipoIndirizzo;

    @NotBlank(message = "L' indirizzo non può contenere solo spazi vuoti")
    @Size(max = 125, message = "L'indirizzo è troppo lungo, max 125 caratteri")
    private String indirizzo;

    @NotBlank(message = "Il civico non può contenere solo spazi vuoti")
    @Size(max = 10, message = "Il civico è troppo lungo, max 10 caratteri")
    private String civico;

    @NotBlank(message = "La Codice Postale non può contenere solo spazi vuoti")
    @Size(max = 10, message = "il Codice Postale è troppo lungo, max 10 caratteri")
    private String cap;

    @NotBlank(message = "La numero di Telefono non può contenere solo spazi vuoti")
    @Size(max = 15, message = "Il numero di Telefono è troppo lungo, max 15 caratteri")
    private String numeroTelefono;




}
