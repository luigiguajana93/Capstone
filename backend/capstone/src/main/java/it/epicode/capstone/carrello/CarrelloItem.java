package it.epicode.capstone.carrello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.capstone.prodotti.Prodotto;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor
@Table(name="carrello_items")
public class CarrelloItem {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "carrello_id")
    @JsonIgnoreProperties("carrelloItems")
    @ToString.Exclude
    private Carrello carrello;

    @ManyToOne
    @JoinColumn(name = "prodotto_id")
    @JsonIgnoreProperties({"carrelloItems"})
    @ToString.Exclude
    private Prodotto prodotto;

    private int quantita;
    private double prezzo;


}
