package it.epicode.capstone.carrello;

import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.utenti.Utente;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CarrelloResponseDTO {

    private Long id;
    private Utente utente;
    private List<Prodotto> prodotti;
}
