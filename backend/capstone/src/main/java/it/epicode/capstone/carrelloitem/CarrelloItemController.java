package it.epicode.capstone.carrelloitem;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/carrelloItems")
public class CarrelloItemController {

    @Autowired
    private CarrelloItemService carrelloItemService;

    @GetMapping("/{id}")
    public ResponseEntity<CarrelloItemResponseDTO> findById(@PathVariable Long id){
        return ResponseEntity.ok(carrelloItemService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<CarrelloItemResponseDTO>> findAll(){
        return ResponseEntity.ok(carrelloItemService.findAll());
    }

    @PostMapping
    public ResponseEntity<CarrelloItemResponseDTO> create(@RequestBody CarrelloItemRequestDTO request){
        return ResponseEntity.ok(carrelloItemService.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CarrelloItemResponseDTO> modify(@PathVariable Long id, @RequestBody CarrelloItemRequestDTO request){
        return ResponseEntity.ok(carrelloItemService.update(id, request));
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        carrelloItemService.delete(id);
        return ResponseEntity.ok().build();
    }
}
