package it.epicode.capstone.carrello;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.catalina.LifecycleState;

import java.util.List;

@Data
@NoArgsConstructor
public class CarrelloRequestDTO {

    private Long utenteId;
    private double totaleAcquisto;

}
