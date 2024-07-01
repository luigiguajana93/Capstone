package it.epicode.capstone.carrelloitem;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class CarrelloItemResponseDTO {

    private Long id;
    private Long carrelloId;
    private Long prodottoId;
    private int quantita;
    private double prezzo;

}
