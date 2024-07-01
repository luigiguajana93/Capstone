package it.epicode.capstone.carrello;

import it.epicode.capstone.carrelloitem.CarrelloItem;
import it.epicode.capstone.carrelloitem.CarrelloItemRepository;
import it.epicode.capstone.carrelloitem.CarrelloItemResponseDTO;
import it.epicode.capstone.errors.ResourceNotFoundException;
import it.epicode.capstone.prodotti.ProdottoRepository;
import it.epicode.capstone.security.SecurityUserDetails;
import it.epicode.capstone.utenti.Utente;
import it.epicode.capstone.utenti.UtenteRepository;
import it.epicode.capstone.utenti.UtenteService;
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

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private CarrelloItemRepository carrelloItemRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;


    public CarrelloResponseDTO findById(Long cartId) {
        Carrello cart = carrelloRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Carrello non trovato con id : " + cartId));
        return mapCartToResponseDTO(cart);
    }

    public CarrelloResponseDTO save(CarrelloRequestDTO requestDTO) {
        Long userId = utenteService.getCurrentUserId(); // Ottieni l'ID utente dal JWT
        Utente utente = utenteRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utente non trovato"));

        Carrello cart = new Carrello();
        cart.setUtente(utente);
        cart.setTotaleAcquisto(requestDTO.getTotaleAcquisto());

        Carrello savedCart = carrelloRepository.save(cart);
        return mapCartToResponseDTO(savedCart);
    }

    @Transactional
    public CarrelloResponseDTO update(Long cartId, CarrelloRequestDTO requestDTO) {
        Carrello existingCart = carrelloRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Carrello non trovato con id: " + cartId));

        Long utenteId = utenteService.getCurrentUserId();
        Utente utente = utenteRepository.findById(utenteId)
                .orElseThrow(() -> new EntityNotFoundException("Utente non trovato con id " + utenteId));

        existingCart.setUtente(utente);
        existingCart.setTotaleAcquisto(requestDTO.getTotaleAcquisto());

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

   // private Carrello mapRequestDTOToCart(CarrelloRequestDTO requestDTO) {
     //   Carrello cart = new Carrello();
//
        // Fetch user from repository based on userId in requestDTO
     //   Utente utente = utenteRepository.findById(requestDTO.getUtenteId())
     //           .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + requestDTO.getUtenteId()));

    //    cart.setUtente(utente);
    //    return cart;
   // }

    public double calcoloTotaleAcquisto(Carrello cart) {
        log.info("calcoloTotaleAcquisto avviata");
        double tempt= cart.getCarrelloItems().stream()
                .mapToDouble(CarrelloItem::getPrezzo)
                .sum();
        log.info("calcolo totale: {}", tempt);
        return tempt;
    }

    @Transactional
    public void clearCart(Long cartId) {
        Carrello cart = carrelloRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));

        List<CarrelloItem> cartItems = cart.getCarrelloItems();
        for (CarrelloItem cartItem : cartItems) {
            carrelloItemRepository.delete(cartItem);
        }

        cart.getCarrelloItems().clear();
        cart.setTotaleAcquisto(0.0);
        carrelloRepository.save(cart);
    }
}
