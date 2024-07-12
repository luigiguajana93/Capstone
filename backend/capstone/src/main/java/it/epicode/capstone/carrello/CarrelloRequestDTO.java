package it.epicode.capstone.carrello;

import it.epicode.capstone.prodotti.Prodotto;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Data
@NoArgsConstructor
public class CarrelloRequestDTO {

    private Long userId;
    private List<Prodotto> prodotti;

}
