package it.epicode.capstone.utenti;

import com.cloudinary.Cloudinary;
import it.epicode.capstone.security.LoginModel;
import it.epicode.capstone.security.LoginResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

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

    @Autowired
    private UtenteRepository utenteRepository;

    @Autowired
    private Cloudinary cloudinary;

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

    @PostMapping("/{username}/avatar")
    public ResponseEntity<String> uploadAvatar(@PathVariable String username, @RequestParam("file") MultipartFile file) {
        try {
            // Carica l'immagine su Cloudinary
            var uploadResult = cloudinary.uploader().upload(file.getBytes(),
                    com.cloudinary.utils.ObjectUtils.asMap("public_id", username + "_avatar"));

            // Recupera l'URL dell'immagine
            String url = uploadResult.get("url").toString();

            // Aggiorna l'utente con l'URL dell'immagine avatar
            Optional<Utente> userOptional = utenteRepository.findOneByUsername(username);
            if (userOptional.isPresent()) {
                Utente user = userOptional.get();
                user.setAvatar(url);
                utenteRepository.save(user);
                return ResponseEntity.ok(url);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload avatar");
        }
    }
}
