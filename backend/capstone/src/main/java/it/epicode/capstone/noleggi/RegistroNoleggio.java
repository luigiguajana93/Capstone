package it.epicode.capstone.noleggi;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.utenti.Utente;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name="noleggi")
public class RegistroNoleggio {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "utente_id")
    @JsonIgnore
    private Utente utente;

    private LocalDate dataInizioNoleggio;

    private LocalDate dataFineNoleggio;

    @ManyToMany
    private List<Prodotto> prodottiNoleggiati;


    private Double costoNoleggioTotale;
}