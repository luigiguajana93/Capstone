package it.epicode.capstone.categorie;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class CategoriaRunner implements ApplicationRunner {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private CategoriaService categoriaService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (categoriaRepository.count() == 0) {
            List<CategoriaRequestDTO> categorie = Arrays.asList(
                    new CategoriaRequestDTO("Corda"),
                    new CategoriaRequestDTO("Arco"),
                    new CategoriaRequestDTO("Fiato"),
                    new CategoriaRequestDTO("Percussioni"),
                    new CategoriaRequestDTO("Tastiera"),
                    new CategoriaRequestDTO("Elettronici"),
                    new CategoriaRequestDTO("Accessori")

            );

            categorie.forEach(request -> categoriaService.create(request));
            System.out.println("--- Categorie inserite ---");
        } else {
            System.out.println("--- Categorie già inserite ---");
        }
    }
}
