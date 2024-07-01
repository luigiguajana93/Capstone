package it.epicode.capstone.carrello;

import it.epicode.capstone.carrelloitem.CarrelloItem;
import it.epicode.capstone.utenti.Utente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "carrelli")
public class Carrello {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToOne
    @JoinColumn(name = "utente_id")
    private Utente utente;


    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL)
    private List<CarrelloItem> CarrelloItems = new ArrayList<>();

    private double totaleAcquisto;



}
