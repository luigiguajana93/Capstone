package it.epicode.capstone.prodotti;

import it.epicode.capstone.categorie.Categoria;
import it.epicode.capstone.categorie.CategoriaRepository;

import it.epicode.capstone.errors.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final CategoriaRepository categoriaRepository;

    public List<ProdottoResponseDTO> getAllProdotti() {
        return prodottoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ProdottoResponseDTO getProdottoById(Long id) {
        return prodottoRepository.findById(id)
                .map(this::convertToDto)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con id: " + id));
    }

    public List<ProdottoResponseDTO> getProdottiByNome(String nomeProdotto) {
        return prodottoRepository.findByNomeProdotto(nomeProdotto).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ProdottoResponseDTO> getProdottiByCategoria(Long categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria non trovata con id: " + categoriaId));
        return prodottoRepository.findByCategoria(categoria).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public ProdottoResponseDTO createProdotto(ProdottoRequestDTO requestDTO) {
        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria non trovata con id: " + requestDTO.getCategoriaId()));

        Prodotto prodotto = Prodotto.builder()
                .withNomeProdotto(requestDTO.getNomeProdotto())
                .withDescrizione(requestDTO.getDescrizione())
                .withPrezzo(requestDTO.getPrezzo())
                .withImageUrl(requestDTO.getImageUrl())
                .withCategoria(categoria)
                .withIsDisponibile(true)
                .build();

        Prodotto savedProdotto = prodottoRepository.save(prodotto);
        return convertToDto(savedProdotto);
    }

    @Transactional
    public ProdottoResponseDTO updateProdotto(Long id, ProdottoRequestDTO requestDTO) {
        Prodotto prodotto = prodottoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Prodotto non trovato con id: " + id));

        Categoria categoria = categoriaRepository.findById(requestDTO.getCategoriaId())
                .orElseThrow(() -> new ResourceNotFoundException("Categoria non trovata con id: " + requestDTO.getCategoriaId()));

        prodotto.setNomeProdotto(requestDTO.getNomeProdotto());
        prodotto.setDescrizione(requestDTO.getDescrizione());
        prodotto.setPrezzo(requestDTO.getPrezzo());
        prodotto.setImageUrl(requestDTO.getImageUrl());
        prodotto.setCategoria(categoria);

        Prodotto updatedProdotto = prodottoRepository.save(prodotto);
        return convertToDto(updatedProdotto);
    }

    @Transactional
    public String deleteProdotto(Long id) {
        if (!prodottoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Prodotto non trovato con id: " + id);
        }
        prodottoRepository.deleteById(id);
        return "Prodotto eliminato";
    }

    private ProdottoResponseDTO convertToDto(Prodotto prodotto) {
        return ProdottoResponseDTO.builder()
                .withId(prodotto.getId())
                .withNomeProdotto(prodotto.getNomeProdotto())
                .withDescrizione(prodotto.getDescrizione())
                .withPrezzo(prodotto.getPrezzo())
                .withImageUrl(prodotto.getImageUrl())
                .withIsDisponibile(prodotto.isDisponibile())
                .withCategoria(prodotto.getCategoria())
                .build();
    }
}
