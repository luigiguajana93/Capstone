package it.epicode.capstone.noleggi;

import it.epicode.capstone.errors.ResourceNotFoundException;
import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.prodotti.ProdottoRepository;
import it.epicode.capstone.utenti.Utente;
import it.epicode.capstone.utenti.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor

public class NoleggioService {

    private final NoleggioRepository noleggioRepository;
    private final ProdottoRepository prodottoRepository;
    private final UtenteRepository utenteRepository;

    public List<RegistroNoleggio> findAllNoleggi() {
        return noleggioRepository.findAll();
    }

    public RegistroNoleggio findById(Long noleggioId) {
        return noleggioRepository.findById(noleggioId)
                .orElseThrow(() -> new EntityNotFoundException("Noleggio non trovato con ID: " + noleggioId));
    }

    public NoleggioResponseDTO saveNoleggio(NoleggioRequestDTO noleggioRequestDTO) {
        Utente utente = utenteRepository.findById(noleggioRequestDTO.getUtenteId())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + noleggioRequestDTO.getUtenteId()));

        List<Prodotto> prodotti = prodottoRepository.findAllById(noleggioRequestDTO.getProdottiId());
        if (prodotti.size() != noleggioRequestDTO.getProdottiId().size()) {
            throw new EntityNotFoundException("Uno o più prodotti non trovati");
        }

        Double costoNoleggioTotale = calcolaCostoNoleggioTotale(
                noleggioRequestDTO.getProdottiId(),
                noleggioRequestDTO.getDataInizioNoleggio(),
                noleggioRequestDTO.getDataFineNoleggio()
        );

        RegistroNoleggio noleggio = new RegistroNoleggio();
        noleggio.setUtente(utente);
        noleggio.setProdottiNoleggiati(prodotti);
        noleggio.setCostoNoleggioTotale(costoNoleggioTotale);
        noleggio.setDataInizioNoleggio(noleggioRequestDTO.getDataInizioNoleggio());
        noleggio.setDataFineNoleggio(noleggioRequestDTO.getDataFineNoleggio());

        RegistroNoleggio noleggioSalvato = noleggioRepository.save(noleggio);

        return convertNoleggioToDto(noleggioSalvato);
    }

    public NoleggioResponseDTO updateNoleggio(Long id, NoleggioRequestDTO noleggioRequestDTO) {
        if (!noleggioRepository.existsById(id)) {
            throw new EntityNotFoundException("Noleggio non trovato con id: " + id);
        }

        RegistroNoleggio noleggio = noleggioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Noleggio non trovato con id: " + id));
        Utente utente = utenteRepository.findById(noleggioRequestDTO.getUtenteId())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + noleggioRequestDTO.getUtenteId()));

        List<Prodotto> prodotti = prodottoRepository.findAllById(noleggioRequestDTO.getProdottiId());
        if (prodotti.size() != noleggioRequestDTO.getProdottiId().size()) {
            throw new EntityNotFoundException("Uno o più prodotti non trovati");
        }

        Double costoNoleggioTotale = calcolaCostoNoleggioTotale(
                noleggioRequestDTO.getProdottiId(),
                noleggioRequestDTO.getDataInizioNoleggio(),
                noleggioRequestDTO.getDataFineNoleggio()
        );

        noleggio.setUtente(utente);
        noleggio.setProdottiNoleggiati(prodotti);
        noleggio.setCostoNoleggioTotale(costoNoleggioTotale);
        noleggio.setDataInizioNoleggio(noleggioRequestDTO.getDataInizioNoleggio());
        noleggio.setDataFineNoleggio(noleggioRequestDTO.getDataFineNoleggio());

        RegistroNoleggio noleggioModificato = noleggioRepository.save(noleggio);

        return convertNoleggioToDto(noleggioModificato);
    }

    private Double calcolaCostoNoleggioTotale(List<Long> prodottiId, LocalDate dataInizioNoleggio, LocalDate dataFineNoleggio) {
        long numeroGiorni = ChronoUnit.DAYS.between(dataInizioNoleggio, dataFineNoleggio);
        return prodottoRepository.findAllById(prodottiId).stream()
                .mapToDouble(prodotto -> prodotto.getPrezzo() * 0.10 * numeroGiorni)
                .sum();
    }

    public String delete(Long noleggioId) {
        if (!noleggioRepository.existsById(noleggioId)) {
            throw new ResourceNotFoundException("Ordine non trovato con id: " + noleggioId);
        }
        noleggioRepository.deleteById(noleggioId);
        return "Noleggio eliminato";
    }

    private NoleggioResponseDTO convertNoleggioToDto(RegistroNoleggio noleggio) {
        return NoleggioResponseDTO.builder()
                .withId(noleggio.getId())
                .withUtenteId(noleggio.getUtente().getId())
                .withDataInizioNoleggio(noleggio.getDataInizioNoleggio())
                .withDataFineNoleggio(noleggio.getDataFineNoleggio())
                .withCostoNoleggioTotale(noleggio.getCostoNoleggioTotale())
                .build();
    }
}
