package it.epicode.capstone.carrello;

import it.epicode.capstone.carrelloitem.CarrelloItem;
import it.epicode.capstone.carrelloitem.CarrelloItemRepository;
import it.epicode.capstone.carrelloitem.CarrelloItemResponseDTO;
import it.epicode.capstone.errors.ResourceNotFoundException;
import it.epicode.capstone.security.SecurityUserDetails;
import it.epicode.capstone.utenti.Utente;
import it.epicode.capstone.utenti.UtenteRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CarrelloService {
    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private UtenteRepository utenteRepository;

    public CarrelloResponseDTO findById(Long cartId) {
        Carrello cart = carrelloRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Carrello non trovato con id : " + cartId));
        return mapCartToResponseDTO(cart);
    }

    public CarrelloResponseDTO save(CarrelloRequestDTO requestDTO) {
        Carrello cart = mapRequestDTOToCart(requestDTO);
        Carrello savedCart = carrelloRepository.save(cart);
        return mapCartToResponseDTO(savedCart);
    }

    @Transactional
    public CarrelloResponseDTO update(Long cartId, CarrelloRequestDTO requestDTO) {
        Carrello existingCart = carrelloRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Carrello non trovato con id: " + cartId));


        Utente utente = utenteRepository.findById(requestDTO.getUtenteId())
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id " + requestDTO.getUtenteId()));

        existingCart.setUtente(utente);
        existingCart.setTotaleAcquisto(calcoloTotaleAcquisto(existingCart));

        Carrello updatedCart = carrelloRepository.save(existingCart);
        return mapCartToResponseDTO(updatedCart);
    }

    @Transactional
    public String delete(Long cartId) {
        if (!carrelloRepository.existsById(cartId)) {
            throw new ResourceNotFoundException("Prodotto non trovato con id: " + cartId);
        }
        carrelloRepository.deleteById(cartId);
        return "Prodotto eliminato";
    }

    public List<CarrelloResponseDTO> findAll() {
        List<Carrello> carts = carrelloRepository.findAll();
        return carts.stream()
                .map(this::mapCartToResponseDTO)
                .collect(Collectors.toList());
    }

    private CarrelloResponseDTO mapCartToResponseDTO(Carrello cart) {
        CarrelloResponseDTO responseDTO = new CarrelloResponseDTO();
        responseDTO.setId(cart.getId());
        responseDTO.setUtenteId(cart.getUtente().getId());
        responseDTO.setTotaleAcquisto(cart.getTotaleAcquisto());
        List<CarrelloItemResponseDTO> cartItems = cart.getCarrelloItems().stream()
                .map(this::mapCartItemToResponseDTO)
                .collect(Collectors.toList());
        responseDTO.setCarrelloItems(cartItems);
        return responseDTO;
    }

    private CarrelloItemResponseDTO mapCartItemToResponseDTO(CarrelloItem cartItem) {
        CarrelloItemResponseDTO responseDTO = new CarrelloItemResponseDTO();
        responseDTO.setId(cartItem.getId());
        responseDTO.setCarrelloId(cartItem.getCarrello().getId());
        responseDTO.setProdottoId(cartItem.getProdotto().getId());
        responseDTO.setQuantita(cartItem.getQuantita());
        responseDTO.setPrezzo(cartItem.getPrezzo());

        return responseDTO;
    }

    private Carrello mapRequestDTOToCart(CarrelloRequestDTO requestDTO) {
        Carrello cart = new Carrello();

        // Fetch user from repository based on userId in requestDTO
        Utente utente = utenteRepository.findById(requestDTO.getUtenteId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDTO.getUtenteId()));

        cart.setUtente(utente);
        return cart;
    }

    public double calcoloTotaleAcquisto(Carrello cart) {
        log.info("calcoloTotaleAcquisto avviata");
        double tempt= cart.getCarrelloItems().stream()
                .mapToDouble(CarrelloItem::getPrezzo)
                .sum();
        log.info("calcolo totale: " + tempt);
        return tempt;
    }

    // Metodo per ottenere l'ID dell'utente dal contesto di sicurezza
    private Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDetails userDetails) {
            return userDetails.getUserId();
        }
        throw new IllegalStateException("Utente non autenticato");
    }
}
