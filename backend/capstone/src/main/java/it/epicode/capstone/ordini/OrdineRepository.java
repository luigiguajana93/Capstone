package it.epicode.capstone.ordini;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdineRepository extends JpaRepository<Ordine,Long> {

}
