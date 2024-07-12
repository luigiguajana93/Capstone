package it.epicode.capstone.carrello;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.capstone.utenti.Utente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
    @JsonIgnoreProperties({"carrello"})
    @ToString.Exclude
    private Utente utente;

    @OneToMany(mappedBy = "carrello", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties({"carrello",})
    @ToString.Exclude
    private List<CarrelloItem> carrelloItems= new ArrayList<>();

    private double totaleAcquisto;

}
