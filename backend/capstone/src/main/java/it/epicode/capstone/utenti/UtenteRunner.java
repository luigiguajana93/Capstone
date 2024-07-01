package it.epicode.capstone.utenti;

import it.epicode.capstone.security.Roles;
import it.epicode.capstone.security.RolesRepository;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class UtenteRunner {
    private final UtenteService utenteService;
    private final RolesRepository rolesRepository;

    @Bean
    public ApplicationRunner runner() {
        return args -> {
            Roles adminRole = rolesRepository.findById(Roles.ROLES_ADMIN)
                    .orElseGet(() ->{
                        log.info("Creazione ruolo ADMIN");
                        return rolesRepository.save(new Roles(Roles.ROLES_ADMIN));
                    });
            Roles userRole = rolesRepository.findById(Roles.ROLES_USER)
                    .orElseGet(() -> {
                        log.info("Creazione ruolo USER");
                        return rolesRepository.save(new Roles(Roles.ROLES_USER));
                    });

            List<UtenteRequestDTO> utenti = Arrays.asList(
                    UtenteRequestDTO.builder()
                            .withNome("Mario")
                            .withCognome("Rossi")
                            .withUsername("mario.rossi")
                            .withEmail("mario.rossi@example.com")
                            .withPassword("password1")
                            .withCitta("Roma")
                            .withTipoIndirizzo("Via")
                            .withIndirizzo("Filiberto")
                            .withCivico("1")
                            .withCap("00100")
                            .withNumeroTelefono("1234567890")
                            .build(),
                    UtenteRequestDTO.builder()
                            .withNome("Luigi")
                            .withCognome("Verdi")
                            .withUsername("luigi.verdi")
                            .withEmail("luigi.verdi@example.com")
                            .withPassword("password2")
                            .withCitta("Milano")
                            .withTipoIndirizzo("via")
                            .withIndirizzo("Milano")
                            .withCivico("2")
                            .withCap("20100")
                            .withNumeroTelefono("0987654321")
                            .build(),
                    UtenteRequestDTO.builder()
                            .withNome("Giulia")
                            .withCognome("Bianchi")
                            .withUsername("giulia.bianchi")
                            .withEmail("giulia.bianchi@example.com")
                            .withPassword("password3")
                            .withCitta("Napoli")
                            .withTipoIndirizzo("via")
                            .withIndirizzo("Napoli")
                            .withCivico("3")
                            .withCap("80100")
                            .withNumeroTelefono("1231231234")
                            .build(),
                    UtenteRequestDTO.builder()
                            .withNome("Marco")
                            .withCognome("Neri")
                            .withUsername("marco.neri")
                            .withEmail("marco.neri@example.com")
                            .withPassword("password4")
                            .withCitta("Torino")
                            .withTipoIndirizzo("via")
                            .withIndirizzo("Torino")
                            .withCivico("4")
                            .withCap("10100")
                            .withNumeroTelefono("3213213210")
                            .build(),
                    UtenteRequestDTO.builder()
                            .withNome("Anna")
                            .withCognome("Gialli")
                            .withUsername("anna.gialli")
                            .withEmail("anna.gialli@example.com")
                            .withPassword("password5")
                            .withCitta("Palermo")
                            .withTipoIndirizzo("via")
                            .withIndirizzo("Palermo")
                            .withCivico("5")
                            .withCap("90100")
                            .withNumeroTelefono("4564564567")
                            .build(),
                    UtenteRequestDTO.builder()
                            .withNome("Luca")
                            .withCognome("Azzurri")
                            .withUsername("luca.azzurri")
                            .withEmail("luca.azzurri@example.com")
                            .withPassword("password6")
                            .withCitta("Genova")
                            .withTipoIndirizzo("via")
                            .withIndirizzo("Genova")
                            .withCivico("6")
                            .withCap("16100")
                            .withNumeroTelefono("7897897890")
                            .build(),
                    UtenteRequestDTO.builder()
                            .withNome("Elena")
                            .withCognome("Viola")
                            .withUsername("elena.viola")
                            .withEmail("elena.viola@example.com")
                            .withPassword("password7")
                            .withCitta("Bologna")
                            .withTipoIndirizzo("via")
                            .withIndirizzo("Bologna")
                            .withCivico("7")
                            .withCap("40100")
                            .withNumeroTelefono("9879879870")
                            .build()
            );

            utenti.forEach(utente -> {
                log.info("Creazione utente: {}", utente.getUsername());
                try {
                    utenteService.save(utente);
                } catch (EntityExistsException e) {
                    log.info("Utente già esistente: {}", utente.getUsername());
                }
            });

            // Creazione di un amministratore di esempio
            UtenteRequestDTO admin = UtenteRequestDTO.builder()
                    .withNome("Luigi")
                    .withCognome("Guajana")
                    .withUsername("luigi.guajana")
                    .withEmail("luigi.guajana@gmail.com")
                    .withPassword("adminpassword")
                    .withCitta("Palermo")
                    .withTipoIndirizzo("Corso")
                    .withIndirizzo("Re Ruggero")
                    .withCivico("10")
                    .withCap("90100")
                    .withNumeroTelefono("0000000000")
                    .build();

            log.info("Creazione admin: {}", admin.getUsername());
            try {
                utenteService.registerAdmin(admin);
            } catch (EntityExistsException e) {
                log.info("Admin già esistente: {}", admin.getUsername());
            }
        };
    }
}
