package it.epicode.capstone.ordini;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.utenti.Utente;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name="ordini")
public class Ordine {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn( name ="utente_id")
    @JsonIgnore
    private Utente utente;

    @ManyToMany
    private List<Prodotto> prodottiOrdinati;

    private LocalDate dataOrdine;

    private Double costoTotale;

}
