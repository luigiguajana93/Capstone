package it.epicode.capstone.carrello;

import it.epicode.capstone.carrelloitem.CarrelloItemResponseDTO;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CarrelloResponseDTO {

    private Long id;
    private Long utenteId;
    private double totaleAcquisto;
    private List<CarrelloItemResponseDTO> carrelloItems;
}
