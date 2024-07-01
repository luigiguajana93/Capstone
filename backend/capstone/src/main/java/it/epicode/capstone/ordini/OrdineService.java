package it.epicode.capstone.ordini;

import it.epicode.capstone.errors.ResourceNotFoundException;
import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.prodotti.ProdottoRepository;
import it.epicode.capstone.utenti.Utente;
import it.epicode.capstone.utenti.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class OrdineService {
    @Autowired
    private OrdineRepository ordineRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    public List<Ordine> findAllOrdini() {
        return ordineRepository.findAll();
    }

    public Ordine findById(Long ordineId) {
        return ordineRepository.findById(ordineId)
                .orElseThrow(() -> new EntityNotFoundException("Ordine non trovato con ID: " + ordineId));
    }

    public OrdineResponseDTO save(OrdineRequestDTO ordineRequestDTO) {
        Utente utente = utenteRepository.findById(ordineRequestDTO.getUtenteId())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + ordineRequestDTO.getUtenteId()));

        List<Prodotto> prodotti = prodottoRepository.findAllById(ordineRequestDTO.getProdottiId());
        if (prodotti.size() != ordineRequestDTO.getProdottiId().size()) {
            throw new EntityNotFoundException("Uno o piu prodotti non trovati");
        }

        Double costoTotale = calcolaCostoTotale(ordineRequestDTO.getProdottiId());

        Ordine ordine = new Ordine();
        BeanUtils.copyProperties(ordineRequestDTO, ordine);
        ordine.setUtente(utente);
        ordine.setProdottiOrdinati(prodotti);
        ordine.setCostoTotale(costoTotale);
        Ordine ordineSalvato = ordineRepository.save(ordine);

        OrdineResponseDTO response = new OrdineResponseDTO();
        BeanUtils.copyProperties(ordineSalvato, response);
        response.setUtenteId(ordineSalvato.getUtente().getId());
        return response;
    }

    public OrdineResponseDTO update(Long id, OrdineRequestDTO ordineRequestDTO) {
        if (!ordineRepository.existsById(id)) {
            throw new EntityNotFoundException("Ordine non trovato con id: " + id);
        }

        Ordine ordine = ordineRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Ordine non trovato con id: " + id));
        Utente utente = utenteRepository.findById(ordineRequestDTO.getUtenteId())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id: " + ordineRequestDTO.getUtenteId()));

        List<Prodotto> prodotti = prodottoRepository.findAllById(ordineRequestDTO.getProdottiId());
        if (prodotti.size() != ordineRequestDTO.getProdottiId().size()) {
            throw new EntityNotFoundException("Uno o più prodotti non trovati");
        }

        Double costoTotale = calcolaCostoTotale(ordineRequestDTO.getProdottiId());

        BeanUtils.copyProperties(ordineRequestDTO, ordine);
        ordine.setUtente(utente);
        ordine.setProdottiOrdinati(prodotti);
        ordine.setCostoTotale(costoTotale);

        Ordine ordineModificato = ordineRepository.save(ordine);

        OrdineResponseDTO response = new OrdineResponseDTO();
        BeanUtils.copyProperties(ordineModificato, response);
        response.setUtenteId(ordineModificato.getUtente().getId());
        return response;
    }

    public String delete(Long ordineId) {
        if (!ordineRepository.existsById(ordineId)) {
            throw new ResourceNotFoundException("Ordine non trovato con id: " + ordineId);
        }
        ordineRepository.deleteById(ordineId);
        return "Ordine eliminato";
    }

    private Double calcolaCostoTotale(List<Long> prodottiId) {
        return prodottoRepository.findAllById(prodottiId).stream()
                .mapToDouble(Prodotto::getPrezzo)
                .sum();
    }
}
