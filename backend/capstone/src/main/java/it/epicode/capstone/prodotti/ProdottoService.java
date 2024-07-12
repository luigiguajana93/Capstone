package it.epicode.capstone.prodotti;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.capstone.categorie.Categoria;
import it.epicode.capstone.categorie.CategoriaRepository;

import it.epicode.capstone.errors.ResourceNotFoundException;
import it.epicode.capstone.noleggi.NoleggioRepository;
import it.epicode.capstone.noleggi.RegistroNoleggio;
import it.epicode.capstone.security.FileSizeExceededException;
import it.epicode.capstone.utenti.Utente;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ProdottoService {

    private final ProdottoRepository prodottoRepository;
    private final CategoriaRepository categoriaRepository;
    private final NoleggioRepository noleggioRepository;
    private final Cloudinary cloudinary;


    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

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
        return prodottoRepository.findByNomeProdottoContaining(nomeProdotto).stream()
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
        List<RegistroNoleggio> noleggiConProdotto = noleggioRepository.findByProdottiNoleggiati_Id(id);

        for (RegistroNoleggio noleggio : noleggiConProdotto) {
            noleggio.getProdottiNoleggiati().removeIf(prodotto -> prodotto.getId().equals(id));
            noleggioRepository.save(noleggio);
        }

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



    //aggiunta img prodotto

    public String uploadImage(MultipartFile file) throws IOException {
        long maxFileSize = getMaxFileSizeInBytes();
        if (file.getSize() > maxFileSize) {
            throw new FileSizeExceededException("File size exceeds the maximum allowed size");
        }

        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }

    public long getMaxFileSizeInBytes() {
        String[] parts = maxFileSize.split("(?i)(?<=[0-9])(?=[a-z])");
        long size = Long.parseLong(parts[0]);
        String unit = parts[1].toUpperCase();
        switch (unit) {
            case "KB":
                size *= 1024;
                break;
            case "MB":
                size *= 1024 * 1024;
                break;
            case "GB":
                size *= 1024 * 1024 * 1024;
                break;
        }
        return size;
    }
}
