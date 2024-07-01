package it.epicode.capstone.prodotti;

import it.epicode.capstone.categorie.Categoria;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class ProdottoResponseDTO {

    private Long id;
    private String nomeProdotto;
    private String descrizione;
    private Double prezzo;
    private String imageUrl;
    private boolean isDisponibile;
    private Categoria categoria;
}
