package it.epicode.capstone.carrello;

import it.epicode.capstone.email.EmailService;
import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.prodotti.ProdottoRepository;
import it.epicode.capstone.utenti.Utente;
import it.epicode.capstone.utenti.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CarrelloService {

    @Autowired
    private CarrelloRepository carrelloRepository;
    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    private ProdottoRepository prodottoRepository;
    @Autowired
    private EmailService emailService;

    public List<Carrello> getAllCarrelli() {
        return carrelloRepository.findAll();
    }

    @Transactional
    public Carrello createCarrello(CarrelloRequestDTO carrelloRequestDTO) {
        Utente utente = utenteRepository.findById(carrelloRequestDTO.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User non trovato con ID: " + carrelloRequestDTO.getUserId()));

        Carrello carrello = new Carrello();
        carrello.setUtente(utente);
        carrello.setCarrelloItems(carrelloRequestDTO.getProdotti().stream().map(prodotto -> {
            Prodotto p = prodottoRepository.findById(prodotto.getId())
                    .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con ID: " + prodotto.getId()));
            CarrelloItem item = new CarrelloItem();
            item.setProdotto(p);
            item.setQuantita(1);
            item.setPrezzo(p.getPrezzo());
            item.setCarrello(carrello);
            return item;
        }).collect(Collectors.toList()));

        carrello.setTotaleAcquisto(carrello.getCarrelloItems().stream().mapToDouble(CarrelloItem::getPrezzo).sum());
        return carrelloRepository.save(carrello);
    }

    @Transactional
    public Carrello addProdottoToCarrello(Long userId, Long prodottoId, int quantita) {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User non trovato con ID: " + userId));

        Prodotto prodotto = prodottoRepository.findById(prodottoId)
                .orElseThrow(() -> new EntityNotFoundException("Prodotto non trovato con ID: " + prodottoId));

        Carrello carrello = carrelloRepository.findByUtenteId(userId)
                .orElseGet(() -> {
                    Carrello nuovoCarrello = new Carrello();
                    nuovoCarrello.setUtente(utente);
                    return carrelloRepository.save(nuovoCarrello);
                });

        CarrelloItem carrelloItem = carrello.getCarrelloItems().stream()
                .filter(item -> item.getProdotto().getId().equals(prodottoId))
                .findFirst()
                .orElse(null);

        if (carrelloItem == null) {
            carrelloItem = new CarrelloItem();
            carrelloItem.setCarrello(carrello);
            carrelloItem.setProdotto(prodotto);
            carrelloItem.setQuantita(quantita);
            carrelloItem.setPrezzo(prodotto.getPrezzo() * quantita);
            carrello.getCarrelloItems().add(carrelloItem);
        } else {
            carrelloItem.setQuantita(carrelloItem.getQuantita() + quantita);
            carrelloItem.setPrezzo(carrelloItem.getQuantita() * prodotto.getPrezzo());
        }

        carrello.setTotaleAcquisto(carrello.getCarrelloItems().stream().mapToDouble(CarrelloItem::getPrezzo).sum());

        return carrelloRepository.save(carrello);
    }

    @Transactional
    public Carrello removeProdottoFromCarrello(Long carrelloId, Long carrelloItemId) {
        Carrello carrello = carrelloRepository.findById(carrelloId)
                .orElseThrow(() -> new EntityNotFoundException("Carrello non trovato con ID: " + carrelloId));

        CarrelloItem carrelloItem = carrello.getCarrelloItems().stream()
                .filter(item -> item.getId().equals(carrelloItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Carrello Item non trovata con ID: " + carrelloItemId));

        carrello.getCarrelloItems().remove(carrelloItem);
        carrello.setTotaleAcquisto(carrello.getCarrelloItems().stream().mapToDouble(CarrelloItem::getPrezzo).sum());

        return carrelloRepository.save(carrello);
    }

    @Transactional
    public Carrello updateQuantitaProdotto(Long carrelloId, Long carrelloItemId, int nuovaQuantita) {
        Carrello carrello = carrelloRepository.findById(carrelloId)
                .orElseThrow(() -> new EntityNotFoundException("Carrello non trovato con ID: " + carrelloId));

        CarrelloItem carrelloItem = carrello.getCarrelloItems().stream()
                .filter(item -> item.getId().equals(carrelloItemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException("Carrello Item non trovata con ID: " + carrelloItemId));

        carrelloItem.setQuantita(nuovaQuantita);
        carrelloItem.setPrezzo(carrelloItem.getProdotto().getPrezzo() * nuovaQuantita);
        carrello.setTotaleAcquisto(carrello.getCarrelloItems().stream().mapToDouble(CarrelloItem::getPrezzo).sum());

        return carrelloRepository.save(carrello);
    }

    @Transactional
    public Carrello svuotaCarrello(Long carrelloId) {
        Carrello carrello = carrelloRepository.findById(carrelloId)
                .orElseThrow(() -> new EntityNotFoundException("Carrello non trovato con ID: " + carrelloId));

        carrello.getCarrelloItems().clear();
        carrello.setTotaleAcquisto(0.0);
        Carrello savedCarrello = carrelloRepository.save(carrello);

        // Ottenere l'email dell'utente
        Utente utente = carrello.getUtente();
        String email = utente.getEmail();

        // Inviare email di conferma acquisto
        emailService.sendPurchaseConfirmationEmail(email);

        return savedCarrello;
    }

    @Transactional
    public Carrello confermaAcquisto(Long userId, List<Long> prodottiId) {
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User non trovato con ID: " + userId));

        List<Prodotto> prodotti = prodottoRepository.findAllById(prodottiId);
        if (prodotti.size() != prodottiId.size()) {
            throw new EntityNotFoundException("Uno o più prodotti non trovati");
        }

        Carrello carrello = carrelloRepository.findByUtenteId(userId)
                .orElseGet(() -> {
                    Carrello nuovoCarrello = new Carrello();
                    nuovoCarrello.setUtente(utente);
                    return carrelloRepository.save(nuovoCarrello);
                });

        List<CarrelloItem> carrelloItems = prodotti.stream().map(prodotto -> {
            CarrelloItem item = new CarrelloItem();
            item.setProdotto(prodotto);
            item.setQuantita(1); // Imposta la quantità a 1 o qualunque logica desiderata
            item.setPrezzo(prodotto.getPrezzo());
            item.setCarrello(carrello);
            return item;
        }).collect(Collectors.toList());

        updateCarrelloItems(carrello, carrelloItems);

        carrello.setTotaleAcquisto(carrelloItems.stream().mapToDouble(CarrelloItem::getPrezzo).sum());
        Carrello carrelloSalvato = carrelloRepository.save(carrello);

        // Ottenere l'email dell'utente
        String email = utente.getEmail();

        // Inviare email di conferma acquisto
        emailService.sendPurchaseConfirmationEmail(email);

        return carrelloSalvato;
    }


    private void updateCarrelloItems(Carrello carrello, List<CarrelloItem> newItems) {
        carrello.getCarrelloItems().clear();
        carrello.getCarrelloItems().addAll(newItems);
        carrelloRepository.save(carrello);
    }


    public void deleteCarrello(Long id) {
        if (!carrelloRepository.existsById(id)) {
            throw new EntityNotFoundException("Carrello non trovato con ID: " + id);
        }
        carrelloRepository.deleteById(id);
    }
}