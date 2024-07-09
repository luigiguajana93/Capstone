package it.epicode.capstone.prodotti;

import it.epicode.capstone.categorie.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto,Long> {

    public List<Prodotto> findByNomeProdotto(String nomeProdotto);

    public Boolean existsByNomeProdotto(String nomeProdotto);

    public List<Prodotto> findByCategoria(Categoria categoria);

    List<Prodotto> findByNomeProdottoContaining(@Param("nomeProdotto") String nomeProdotto);



}
