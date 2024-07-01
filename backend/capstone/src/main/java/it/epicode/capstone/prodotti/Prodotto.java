package it.epicode.capstone.prodotti;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import it.epicode.capstone.categorie.Categoria;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="prodotti")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Prodotto {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String nomeProdotto;
    private String descrizione;
    private Double prezzo;
    private String imageUrl;
    private boolean isDisponibile = true;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonManagedReference
    private Categoria categoria;





}
