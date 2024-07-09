package it.epicode.capstone.carrello;

import lombok.Data;

import java.util.List;

@Data
public class ConfermaAcquistoRequestoDTO {
    private Long userId;
    private List<Long> prodottiId;
}
