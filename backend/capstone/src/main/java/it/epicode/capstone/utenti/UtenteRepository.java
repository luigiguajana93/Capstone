package it.epicode.capstone.utenti;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UtenteRepository extends JpaRepository<Utente,Long> {
    public boolean existsByUsername(String username);

    public List<UtenteResponsePrj> findAllBy();

    Optional<Utente> findOneByUsername(String username);
    boolean existsByEmail(String email);

 }
