package it.epicode.capstone.noleggi;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoleggioRepository extends JpaRepository<RegistroNoleggio,Long> {
    @Transactional
    List<RegistroNoleggio> findByProdottiNoleggiati_Id(Long prodottoId);

}
