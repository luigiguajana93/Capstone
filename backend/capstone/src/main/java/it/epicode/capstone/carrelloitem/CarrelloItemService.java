package it.epicode.capstone.carrelloitem;


import it.epicode.capstone.carrello.*;
import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.prodotti.ProdottoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CarrelloItemService {
    @Autowired
    private CarrelloItemRepository carrelloItemRepository;

    @Autowired
    private CarrelloRepository carrelloRepository;

    @Autowired
    private ProdottoRepository prodottoRepository;

    @Autowired
    private CarrelloService carrelloService;

    public CarrelloItemResponseDTO findById(Long cartItemId) {
        CarrelloItem cartItem = carrelloItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CarrelloItem non trovato con id: " + cartItemId));
        return mapCartItemToResponseDTO(cartItem);
    }

    public CarrelloItemResponseDTO save(CarrelloItemRequestDTO requestDTO) {
        CarrelloItem cartItem = mapRequestDTOToCartItem(requestDTO);
        CarrelloItem savedCartItem = carrelloItemRepository.save(cartItem);
        updateCartTotalAmount(savedCartItem.getCarrello());
        return mapCartItemToResponseDTO(savedCartItem);
    }

    public CarrelloItemResponseDTO update(Long cartItemId, CarrelloItemRequestDTO requestDTO) {
        CarrelloItem existingCartItem = carrelloItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));

        // Update quantity
        existingCartItem.setQuantita(requestDTO.getQuantita());

        // Recalculate total price
        double totalPrice = existingCartItem.getProdotto().getPrezzo() * requestDTO.getQuantita();
        existingCartItem.setPrezzo(totalPrice);

        // Save updated CartItem
        CarrelloItem updatedCartItem = carrelloItemRepository.save(existingCartItem);
        updateCartTotalAmount(updatedCartItem.getCarrello());
        return mapCartItemToResponseDTO(updatedCartItem);
    }


    public void delete(Long cartItemId) {
        CarrelloItem cartItem = carrelloItemRepository.findById(cartItemId)
                .orElseThrow(() -> new EntityNotFoundException("CartItem not found with id: " + cartItemId));
        carrelloItemRepository.delete(cartItem);


        updateCartTotalAmount(cartItem.getCarrello());
    }


    public List<CarrelloItemResponseDTO> findAll() {
        List<CarrelloItem> cartItems = carrelloItemRepository.findAll();
        return cartItems.stream()
                .map(this::mapCartItemToResponseDTO)
                .collect(Collectors.toList());
    }

    private CarrelloItemResponseDTO mapCartItemToResponseDTO(CarrelloItem cartItem) {
        CarrelloItemResponseDTO responseDTO = new CarrelloItemResponseDTO();
        responseDTO.setId(cartItem.getId());
        responseDTO.setCarrelloId(cartItem.getCarrello().getId());
        responseDTO.setProdottoId(cartItem.getProdotto().getId());
        responseDTO.setQuantita(cartItem.getQuantita());
        responseDTO.setPrezzo(cartItem.getPrezzo());  // Ensure price is set in the response
        return responseDTO;
    }

    private CarrelloItem mapRequestDTOToCartItem(CarrelloItemRequestDTO requestDTO) {
        CarrelloItem cartItem = new CarrelloItem();
        cartItem.setCarrello(fetchCartById(requestDTO.getCarrelloId()));
        cartItem.setProdotto(fetchProductById(requestDTO.getProdottoId()));
        cartItem.setQuantita(requestDTO.getQuantita());
        // Initialize price based on product price and quantity
        double price = cartItem.getProdotto().getPrezzo() * requestDTO.getQuantita();
        cartItem.setPrezzo(price);
        return cartItem;
    }

    private Carrello fetchCartById(Long cartId) {
        return carrelloRepository.findById(cartId)
                .orElseThrow(() -> new EntityNotFoundException("Cart not found with id: " + cartId));
    }

    private Prodotto fetchProductById(Long productId) {
        return prodottoRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found with id: " + productId));
    }

    public void updateCartTotalAmount(Carrello cart) {
        double totalAmount = carrelloService.calcoloTotaleAcquisto(cart);
        cart.setTotaleAcquisto(totalAmount);
        carrelloRepository.save(cart);
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
}
