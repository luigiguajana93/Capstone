package it.epicode.capstone.noleggi;

import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.utenti.Utente;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class NoleggioRequestDTO {

    private Long utenteId;
    private List<Long> prodottiId;
    private LocalDate dataInizioNoleggio;
    private LocalDate dataFineNoleggio;
    private CittaNoleggio cittaNoleggio;
}
