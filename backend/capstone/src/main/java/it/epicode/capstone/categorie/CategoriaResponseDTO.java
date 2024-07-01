package it.epicode.capstone.categorie;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(setterPrefix = "with")
public class CategoriaResponseDTO {
    private Long id;
    private String descrizione;
}
