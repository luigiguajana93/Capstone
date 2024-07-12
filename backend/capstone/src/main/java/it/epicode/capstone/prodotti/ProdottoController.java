package it.epicode.capstone.prodotti;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prodotti")
@RequiredArgsConstructor
public class ProdottoController {

    private final ProdottoService prodottoService;

    @GetMapping
    public ResponseEntity<List<ProdottoResponseDTO>> getAllProdotti() {
        List<ProdottoResponseDTO> prodotti = prodottoService.getAllProdotti();
        return ResponseEntity.ok(prodotti);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdottoResponseDTO> getProdottoById(@PathVariable Long id) {
        ProdottoResponseDTO prodotto = prodottoService.getProdottoById(id);
        return ResponseEntity.ok(prodotto);
    }

    @GetMapping("/nome")
    public ResponseEntity<List<ProdottoResponseDTO>> getProdottiByNome(@RequestParam String nomeProdotto) {
        List<ProdottoResponseDTO> prodotti = prodottoService.getProdottiByNome(nomeProdotto);
        return ResponseEntity.ok(prodotti);
    }

    @GetMapping("/categoria")
    public ResponseEntity<List<ProdottoResponseDTO>> getProdottiByCategoria(@RequestParam Long categoriaId) {
        List<ProdottoResponseDTO> prodotti = prodottoService.getProdottiByCategoria(categoriaId);
        return ResponseEntity.ok(prodotti);
    }

    @PostMapping
    public ResponseEntity<ProdottoResponseDTO> createProdotto(@RequestBody @Valid ProdottoRequestDTO requestDTO) {
        ProdottoResponseDTO prodotto = prodottoService.createProdotto(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(prodotto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdottoResponseDTO> updateProdotto(@PathVariable Long id, @RequestBody @Valid ProdottoRequestDTO requestDTO) {
        ProdottoResponseDTO prodotto = prodottoService.updateProdotto(id, requestDTO);
        return ResponseEntity.ok(prodotto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProdotto(@PathVariable Long id) {
        String result = prodottoService.deleteProdotto(id);
        return ResponseEntity.ok(result);
    }

    //aggiunta immagine prodotto

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            String imageUrl = prodottoService.uploadImage(file);
            Map<String, String> response = new HashMap<>();
            response.put("url", imageUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
