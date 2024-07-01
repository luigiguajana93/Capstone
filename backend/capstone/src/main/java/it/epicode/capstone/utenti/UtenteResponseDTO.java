package it.epicode.capstone.utenti;

import it.epicode.capstone.noleggi.NoleggioResponseDTO;
import it.epicode.capstone.ordini.Ordine;
import it.epicode.capstone.ordini.OrdineResponseDTO;
import it.epicode.capstone.prodotti.ProdottoResponseDTO;
import it.epicode.capstone.security.Roles;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
@Data
public class UtenteResponseDTO {

    private Long id;
    private String username;
    private String email;
    private String nome;
    private String cognome;
    private String citta;
    private String tipoIndirizzo;
    private String indirizzo;
    private String civico;
    private String cap;
    private String numeroTelefono;
    private String avatar;
    private List<Roles> roles;


}
