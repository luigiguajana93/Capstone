package it.epicode.capstone.utenti;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import it.epicode.capstone.categorie.Categoria;
import it.epicode.capstone.categorie.CategoriaResponseDTO;
import it.epicode.capstone.email.EmailService;
import it.epicode.capstone.errors.NonTrovatoException;
import it.epicode.capstone.noleggi.NoleggioResponseDTO;
import it.epicode.capstone.noleggi.RegistroNoleggio;
import it.epicode.capstone.ordini.Ordine;
import it.epicode.capstone.ordini.OrdineResponseDTO;
import it.epicode.capstone.prodotti.Prodotto;
import it.epicode.capstone.prodotti.ProdottoRepository;
import it.epicode.capstone.prodotti.ProdottoResponseDTO;
import it.epicode.capstone.security.*;
import jakarta.persistence.EntityExistsException;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class UtenteService {

    private final ProdottoRepository prodottoRepository;
    private final UtenteRepository utenteRepository;
    private final RolesRepository rolesRepository;
    private final PasswordEncoder encoder;
    private final EmailService emailService;
    private final AuthenticationManager auth;
    private final Cloudinary cloudinaryUrl;

    @Value("${spring.servlet.multipart.max-file-size}")
    private String maxFileSize;

    public List<UtenteResponsePrj> findAllUtenti() {
        return utenteRepository.findAllBy();
    }

    //GET PER ID
    public UtenteResponseDTO findById(Long id) {
        if (!utenteRepository.existsById(id)) {
            throw new EntityNotFoundException("Utente non trovato con id: " + id);
        }
        Utente entity = utenteRepository.findById(id).get();
        UtenteResponseDTO response = new UtenteResponseDTO();
        BeanUtils.copyProperties(entity, response);
        return response;

    }

    public Optional<UtenteResponseDTO> getUserById(Long id) {
        return utenteRepository.findById(id).map(this::convertToResponse);
    }

    private UtenteResponseDTO convertToResponse(Utente user) {
        UtenteResponseDTO dto = UtenteResponseDTO.builder()
                .withId(user.getId())
                .withNome(user.getNome())
                .withCognome(user.getCognome())
                .withUsername(user.getUsername())
                .withEmail(user.getEmail())
                .withCitta(user.getCitta())
                .withTipoIndirizzo(user.getTipoIndirizzo())
                .withIndirizzo(user.getIndirizzo())
                .withCivico(user.getCivico())
                .withCap(user.getCap())
                .withNumeroTelefono(user.getNumeroTelefono())
                .withAvatar(user.getAvatar())
                .withRoles(user.getRoles())
                .build();
        return dto;
    }


    @Transactional
    public UtenteResponseDTO save(UtenteRequestDTO request){
        if(utenteRepository.existsByUsername(request.getUsername())){
            throw new EntityExistsException("Lo user esiste gia' ");
        }
        Utente entity = new Utente();
        BeanUtils.copyProperties(request, entity);
        utenteRepository.save(entity);

        UtenteResponseDTO response = new UtenteResponseDTO();
        BeanUtils.copyProperties(entity, response);
        return response;
    }


    public UtenteResponseDTO convertToDto(Utente utente) {
        return UtenteResponseDTO.builder()
                .withId(utente.getId())
                .withUsername(utente.getUsername())
                .withEmail(utente.getEmail())
                .withNome(utente.getNome())
                .withCognome(utente.getCognome())
                .withCitta(utente.getCitta())
                .withTipoIndirizzo(utente.getTipoIndirizzo())
                .withIndirizzo(utente.getIndirizzo())
                .withCivico(utente.getCivico())
                .withCap(utente.getCap())
                .withNumeroTelefono(utente.getNumeroTelefono())
                .withAvatar(utente.getAvatar())
                .withRoles(utente.getRoles())
                .build();
    }

    private OrdineResponseDTO convertOrdineToDto(Ordine ordine) {
        return OrdineResponseDTO.builder()
                .withId(ordine.getId())
                .withUtenteId(ordine.getUtente().getId())
                .withDataOrdine(ordine.getDataOrdine())
                .withCostoTotale(ordine.getCostoTotale())
                .build();
    }

    private NoleggioResponseDTO convertNoleggioToDto(RegistroNoleggio noleggio) {
        return NoleggioResponseDTO.builder()
                .withId(noleggio.getId())
                .withDataInizioNoleggio(noleggio.getDataInizioNoleggio())
                .withDataFineNoleggio(noleggio.getDataFineNoleggio())
                .withCostoNoleggioTotale(noleggio.getCostoNoleggioTotale())
                .build();
    }

    private ProdottoResponseDTO convertProdottoToDto(Prodotto prodotto) {
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
    private CategoriaResponseDTO convertCategoriaToDto(Categoria categoria) {
        return CategoriaResponseDTO.builder()
                .withId(categoria.getId())
                .withDescrizione(categoria.getDescrizione())
                .build();
    }

    public UtenteResponseDTO update(Long id, UtenteRequestDTO request) {
        if (!utenteRepository.existsById(id)) {
            throw new EntityNotFoundException("Utente non trovato");
        }
        Utente entity = utenteRepository.findById(id).get();

        // Aggiorna solo se la password è fornita e non vuota
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            entity.setPassword(encoder.encode(request.getPassword()));
        }

        // Copia le altre proprietà, eccetto la password
        if (request.getUsername() != null) entity.setUsername(request.getUsername());
        if (request.getEmail() != null) entity.setEmail(request.getEmail());
        if (request.getNome() != null) entity.setNome(request.getNome());
        if (request.getCognome() != null) entity.setCognome(request.getCognome());
        if (request.getCitta() != null) entity.setCitta(request.getCitta());
        if (request.getTipoIndirizzo() != null) entity.setTipoIndirizzo(request.getTipoIndirizzo());
        if (request.getIndirizzo() != null) entity.setIndirizzo(request.getIndirizzo());
        if (request.getCivico() != null) entity.setCivico(request.getCivico());
        if (request.getCap() != null) entity.setCap(request.getCap());
        if (request.getNumeroTelefono() != null) entity.setNumeroTelefono(request.getNumeroTelefono());
        utenteRepository.save(entity);
        UtenteResponseDTO response = new UtenteResponseDTO();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public String delete(Long id) {
        var currentUser = (SecurityUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ADMIN"));

        if (!isAdmin && !currentUser.getUserId().equals(id)) {
            throw new SecurityException("Tu non hai il permesso di eliminare l'account");
        }

        if (!utenteRepository.existsById(id)) {
            throw new EntityNotFoundException("Utente non trovato");
        }
        utenteRepository.deleteById(id);
        return "Utente eliminato";
    }

    @Transactional
    public UtenteResponseDTO registerAdmin(UtenteRequestDTO register){
        if(utenteRepository.existsByUsername(register.getUsername())){
            throw new EntityExistsException("Utente gia' esistente");
        }

        Roles adminRole = rolesRepository.findById(Roles.ROLES_ADMIN)
                .orElseThrow(() -> new EntityNotFoundException("Ruolo ADMIN non trovato"));

        Utente admin = new Utente();
        BeanUtils.copyProperties(register, admin);
        admin.setPassword(encoder.encode(register.getPassword())); // Codifica la password
        admin.setRoles(new ArrayList<>(List.of(adminRole)));

        Utente savedAdmin = utenteRepository.save(admin);
        emailService.sendWelcomeEmail(savedAdmin.getEmail());

        return convertToDto(savedAdmin);
    }

@Transactional
    public UtenteResponseDTO register(UtenteRequestDTO register) {
        if (utenteRepository.existsByUsername(register.getUsername())) {
            throw new EntityExistsException("Utente già esistente");
        }

        Roles userRole = rolesRepository.findById(Roles.ROLES_USER)
                .orElseThrow(() -> new EntityNotFoundException("Ruolo USER non trovato"));

        Utente utente = new Utente();
        BeanUtils.copyProperties(register, utente);
        utente.setPassword(encoder.encode(register.getPassword())); // Codifica la password
    utente.setRoles(new ArrayList<>(List.of(userRole)));

    Utente savedUtente = utenteRepository.save(utente);
    emailService.sendWelcomeEmail(savedUtente.getEmail());

    return convertToDto(savedUtente);
    }

    @Autowired
    private JwtUtils jwtUtils;

    public Optional<LoginResponseDTO> login(String username, String password) {
        try {
            Authentication authentication = auth.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            SecurityUserDetails userPrincipal = (SecurityUserDetails) authentication.getPrincipal();

            LoginResponseDTO dto = LoginResponseDTO.builder()
                    .withUser(buildRegisteredUserDTO(userPrincipal))
                    .build();

            // Genera il token JWT includendo le informazioni dell'utente
            dto.setToken(jwtUtils.generateToken(authentication));

            return Optional.of(dto);
        } catch (NoSuchElementException e) {
            log.error("User not found", e);
            throw new InvalidLoginException(username, password);
        } catch (AuthenticationException e) {
            log.error("Authentication failed", e);
            throw new InvalidLoginException(username, password);
        }


    }

    private RegisteredUserDTO buildRegisteredUserDTO(SecurityUserDetails userDetails) {
        return RegisteredUserDTO.builder()
                .withId(userDetails.getUserId())
                .withEmail(userDetails.getEmail())
                .withRoles(userDetails.getRoles())
                .withUsername(userDetails.getUsername())
                .withNome(userDetails.getNome())
                .withCognome(userDetails.getCognome())
                .build();
    }



    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof SecurityUserDetails) {
            SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();
            return userDetails.getUserId();
        }
        throw new IllegalStateException("Utente non autenticato");
    }

    @Transactional
    public String uploadAvatar(Long id, MultipartFile image) throws IOException {
        long maxFileSize = getMaxFileSizeInBytes();
        if (image.getSize() > maxFileSize) {
            throw new FileSizeExceededException("File size exceeds the maximum allowed size");
        }

        Optional<Utente> optionalUser = utenteRepository.findById(id);
        Utente user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String existingPublicId = user.getAvatar();
        if (existingPublicId != null && !existingPublicId.isEmpty()) {
            cloudinaryUrl.uploader().destroy(existingPublicId, ObjectUtils.emptyMap());
        }

        Map<String, Object> uploadResult = cloudinaryUrl.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
        String publicId = (String) uploadResult.get("public_id");
        String url = (String) uploadResult.get("url");

        user.setAvatar(publicId);
        utenteRepository.save(user);

        return url;
    }


// DELETE delete cloudinary file

    @Transactional
    public String deleteAvatar(Long id) throws IOException {
        Optional<Utente> optionalUser = utenteRepository.findById(id);
        Utente user = optionalUser.orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));

        String publicId = user.getAvatar();
        if (publicId != null && !publicId.isEmpty()) {
            cloudinaryUrl.uploader().destroy(publicId, ObjectUtils.emptyMap());
            user.setAvatar(null);
            utenteRepository.save(user);
            return "Avatar deleted successfully";
        } else {
            return "No avatar found for deletion";
        }
    }


    // PUT update cloudinary file
    @Transactional
    public String updateAvatar(Long id, MultipartFile updatedImage) throws IOException {
        deleteAvatar(id);
        return uploadAvatar(id, updatedImage);
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

