package it.epicode.capstone.noleggi;


import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.prodotti.ProdottoResponseDTO;

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
public class NoleggioResponseDTO {

    private Long id;
    private Long utenteId;
    private LocalDate dataInizioNoleggio;
    private LocalDate dataFineNoleggio;
    private Double costoNoleggioTotale;
    private CittaNoleggio cittaNoleggio;
    private List<Prodotto> prodottiNoleggiati;

}
