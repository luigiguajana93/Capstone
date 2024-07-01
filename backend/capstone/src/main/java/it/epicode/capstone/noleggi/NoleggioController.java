package it.epicode.capstone.noleggi;


import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/noleggi")
@RequiredArgsConstructor
public class NoleggioController {

    private final NoleggioService noleggioService;

    @GetMapping
    public List<RegistroNoleggio>getAllNoleggi(){
        return noleggioService.findAllNoleggi();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RegistroNoleggio> findNoleggioById(@PathVariable Long id) {
        RegistroNoleggio noleggio = noleggioService.findById(id);
        return ResponseEntity.ok(noleggio);
    }

    @PostMapping
    public ResponseEntity<NoleggioResponseDTO> createNoleggio(@RequestBody NoleggioRequestDTO noleggioRequestDTO) {
        NoleggioResponseDTO noleggioResponseDTO = noleggioService.saveNoleggio(noleggioRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(noleggioResponseDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoleggioResponseDTO> updateNoleggio(@PathVariable Long id, @RequestBody NoleggioRequestDTO noleggioRequestDTO) {
        NoleggioResponseDTO noleggioResponseDTO = noleggioService.updateNoleggio(id, noleggioRequestDTO);
        return ResponseEntity.ok(noleggioResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNoleggio(@PathVariable Long id) {
        String result = noleggioService.delete(id);
        return ResponseEntity.ok(result);
    }
}
