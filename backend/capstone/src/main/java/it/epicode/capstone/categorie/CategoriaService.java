package it.epicode.capstone.categorie;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public List<Categoria> getAllCategoria(){
        return categoriaRepository.findAll();
    }

    public CategoriaResponseDTO getCategoriaById(Long id){
        if(!categoriaRepository.existsById(id)){
            throw new EntityNotFoundException("Categoria non trovata");
        }
        Categoria entity = categoriaRepository.findById(id).get();
        CategoriaResponseDTO response = new CategoriaResponseDTO();
        BeanUtils.copyProperties(entity, response);
        return response;
    }

    public CategoriaResponseDTO create(CategoriaRequestDTO request){
        Categoria entity = new Categoria();
        BeanUtils.copyProperties(request, entity);
        Categoria savedEntity = categoriaRepository.save(entity);
        CategoriaResponseDTO response = new CategoriaResponseDTO();
        BeanUtils.copyProperties(savedEntity, response);
        return response;
    }

    public CategoriaResponseDTO modify(Long id, CategoriaRequestDTO request){
        if(!categoriaRepository.existsById(id)){
            throw new EntityNotFoundException("Categoria non trovata");
        }
        Categoria entity = categoriaRepository.findById(id).get();
        BeanUtils.copyProperties(request, entity);
        Categoria savedEntity = categoriaRepository.save(entity);
        CategoriaResponseDTO response = new CategoriaResponseDTO();
        BeanUtils.copyProperties(savedEntity, response);
        return response;
    }

    public String delete(Long id){
        if(!categoriaRepository.existsById(id)){
            throw new EntityNotFoundException("Categoria non trovata");
        }
        categoriaRepository.deleteById(id);
        return "Categoria eliminata";
    }
}
