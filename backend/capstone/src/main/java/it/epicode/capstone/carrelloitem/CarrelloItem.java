package it.epicode.capstone.carrelloitem;

import it.epicode.capstone.carrello.Carrello;
import it.epicode.capstone.prodotti.Prodotto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class CarrelloItem {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrello_id")
    private Carrello carrello;

    @ManyToOne
    @JoinColumn(name = "prodotto_id")
    private Prodotto prodotto;

    private int quantita;

    private double prezzo;


}
