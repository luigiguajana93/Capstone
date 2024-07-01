package it.epicode.capstone.ordini;

import it.epicode.capstone.prodotti.Prodotto;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class OrdineRequestDTO {

    private Long utenteId;
    private List<Long> prodottiId;
    private LocalDate dataOrdine;


}
