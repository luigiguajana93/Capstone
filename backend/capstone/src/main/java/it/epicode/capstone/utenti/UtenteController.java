package it.epicode.capstone.utenti;

import it.epicode.capstone.security.ApiValidationException;
import it.epicode.capstone.security.LoginModel;
import it.epicode.capstone.security.LoginResponseDTO;
import it.epicode.capstone.security.SecurityUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/utenti")
@RequiredArgsConstructor
public class UtenteController {
    private final UtenteService utenteService;

    @GetMapping
    public ResponseEntity<List<UtenteResponsePrj>> findAll() {
        List<UtenteResponsePrj> utenti = utenteService.findAllUtenti();
        return ResponseEntity.ok(utenti);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UtenteResponseDTO> findById(@PathVariable Long id) {
        UtenteResponseDTO utente = utenteService.findById(id);
        return ResponseEntity.ok(utente);
    }

    @PostMapping
    public ResponseEntity<UtenteResponseDTO> create(@RequestBody @Valid UtenteRequestDTO request) {
        UtenteResponseDTO nuovoUtente = utenteService.save(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoUtente);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UtenteResponseDTO> update(@PathVariable Long id, @RequestBody @Valid UtenteRequestDTO request) {
        UtenteResponseDTO utenteAggiornato = utenteService.update(id, request);
        return ResponseEntity.ok(utenteAggiornato);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        String result = utenteService.delete(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/register")
    public ResponseEntity<UtenteResponseDTO> register(@RequestBody @Valid UtenteRequestDTO register) {
        UtenteResponseDTO nuovoUtente = utenteService.register(register);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuovoUtente);
    }

    @PostMapping("/registerAdmin")
    public ResponseEntity<UtenteResponseDTO> registerAdmin(@RequestBody UtenteRequestDTO registerUser){
        return ResponseEntity.ok(utenteService.registerAdmin(registerUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginModel loginModel) {
        Optional<LoginResponseDTO> loginResponse = utenteService.login(loginModel.username(), loginModel.password());
        return loginResponse.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
    }

    @PatchMapping("/{id}/avatar")
    public Utente uploadAvatar(@RequestParam("avatar") MultipartFile file, @PathVariable Long id) {
        try {
            return utenteService.saveAvatar(id, file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
