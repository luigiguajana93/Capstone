package it.epicode.capstone.ordini;

import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.prodotti.ProdottoResponseDTO;
import it.epicode.capstone.utenti.Utente;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder(setterPrefix = "with")
@NoArgsConstructor
@AllArgsConstructor
public class OrdineResponseDTO {

    private Long id;
    private Long utenteId;
    private LocalDate dataOrdine;
    private Double costoTotale;
    //private List<ProdottoResponseDTO> prodottiOrdinati;
}
