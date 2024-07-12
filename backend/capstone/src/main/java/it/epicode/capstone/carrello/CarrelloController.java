package it.epicode.capstone.carrello;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrelli")
public class CarrelloController {

    @Autowired
    private CarrelloService carrelloService;

    @GetMapping
    public List<Carrello> getAllCarrelli() {
        return carrelloService.getAllCarrelli();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Carrello> getCarrelloById(@PathVariable Long id) {
        return carrelloService.getCarrelloById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Carrello> createCarrello(@RequestBody CarrelloRequestDTO carrelloRequestDTO) {
        try {
            Carrello carrello = carrelloService.createCarrello(carrelloRequestDTO);
            return new ResponseEntity<>(carrello, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/utenti/{userId}/prodotti/{prodottoId}")
    public ResponseEntity<Carrello> addProdottoToCarrello(@PathVariable Long userId, @PathVariable Long prodottoId, @RequestParam int quantita) {
        try {
            Carrello carrello = carrelloService.addProdottoToCarrello(userId, prodottoId, quantita);
            return new ResponseEntity<>(carrello, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/confermaAcquisto")
    public ResponseEntity<Carrello> confermaAcquisto(@RequestBody ConfermaAcquistoRequestoDTO request) {
        try {
            System.out.println("confermaAcquisto endpoint called with userId: " + request.getUserId() + " and prodottiId: " + request.getProdottiId());
            Carrello carrello = carrelloService.confermaAcquisto(request.getUserId(), request.getProdottiId());
            return new ResponseEntity<>(carrello, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{carrelloId}/remove/{carrelloItemId}")
    public ResponseEntity<Carrello> removeProdottoFromCarrello(@PathVariable Long carrelloId, @PathVariable Long carrelloItemId) {
        try {
            Carrello carrello = carrelloService.removeProdottoFromCarrello(carrelloId, carrelloItemId);
            return new ResponseEntity<>(carrello, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{carrelloId}/update/{carrelloItemId}")
    public ResponseEntity<Carrello> updateQuantitaProdotto(@PathVariable Long carrelloId, @PathVariable Long carrelloItemId, @RequestParam int nuovaQuantita) {
        try {
            Carrello carrello = carrelloService.updateQuantitaProdotto(carrelloId, carrelloItemId, nuovaQuantita);
            return new ResponseEntity<>(carrello, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{carrelloId}/svuota")
    public ResponseEntity<Carrello> svuotaCarrello(@PathVariable Long carrelloId) {
        try {
            Carrello carrello = carrelloService.svuotaCarrello(carrelloId);
            return new ResponseEntity<>(carrello, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCarrello(@PathVariable Long id) {
        try {
            carrelloService.deleteCarrello(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
}