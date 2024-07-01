package it.epicode.capstone.carrello;

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

    @GetMapping("/{id}")
    public ResponseEntity<CarrelloResponseDTO> getCarrelloById(@PathVariable Long id) {
        CarrelloResponseDTO carrelloResponseDTO = carrelloService.findById(id);
        return ResponseEntity.ok(carrelloResponseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CarrelloResponseDTO>> getAllCarrelli() {
        List<CarrelloResponseDTO> carrelli = carrelloService.findAll();
        return ResponseEntity.ok(carrelli);
    }

    @PostMapping
    public ResponseEntity<CarrelloResponseDTO> createCarrello(@RequestBody CarrelloRequestDTO carrelloRequestDTO) {
        CarrelloResponseDTO response = carrelloService.save(carrelloRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarrelloResponseDTO> updateCarrello(@PathVariable Long id, @RequestBody CarrelloRequestDTO carrelloRequestDTO) {
        CarrelloResponseDTO carrelloResponseDTO = carrelloService.update(id, carrelloRequestDTO);
        return ResponseEntity.ok(carrelloResponseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCarrello(@PathVariable Long id) {
        String result = carrelloService.delete(id);
        return ResponseEntity.ok(result);
    }

}
