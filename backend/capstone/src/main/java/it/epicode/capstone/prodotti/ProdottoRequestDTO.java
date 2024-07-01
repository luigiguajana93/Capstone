package it.epicode.capstone.prodotti;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdottoRequestDTO {
    private String nomeProdotto;
    private String descrizione;
    private String imageUrl;
    private Long categoriaId;
    private double prezzo;
}
