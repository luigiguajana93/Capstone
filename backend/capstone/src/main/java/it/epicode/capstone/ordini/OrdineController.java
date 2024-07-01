package it.epicode.capstone.ordini;

import it.epicode.capstone.errors.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordini")
@RequiredArgsConstructor
public class OrdineController {

    @Autowired
    private OrdineService ordineService;

    @GetMapping
    public List<Ordine> findAllOrdini() {
        return ordineService.findAllOrdini();
    }

    @PostMapping
    public ResponseEntity<OrdineResponseDTO> createOrdine(@RequestBody OrdineRequestDTO ordineRequestDTO) {
        OrdineResponseDTO ordineResponseDTO = ordineService.save(ordineRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(ordineResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrdineResponseDTO> updateOrdine(@PathVariable Long id, @RequestBody OrdineRequestDTO ordineRequestDTO) {
        OrdineResponseDTO ordineResponseDTO = ordineService.update(id, ordineRequestDTO);
        return ResponseEntity.ok(ordineResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteordine(@PathVariable Long id) {
        String result = ordineService.delete(id);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ordine> findordineById(@PathVariable Long id) {
        Ordine ordine = ordineService.findById(id);
        return ResponseEntity.ok(ordine);
    }
}
