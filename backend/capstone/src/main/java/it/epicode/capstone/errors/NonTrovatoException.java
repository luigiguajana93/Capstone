package it.epicode.capstone.errors;

import java.io.Serial;

public class NonTrovatoException extends RuntimeException{
    @Serial
    private static final long serialVersionUID = 1L;

    public final Long idNotFound;

    public NonTrovatoException(Long id, String message){
        super(message);
        this.idNotFound = id;
    }

    public NonTrovatoException(Long id){
        this(id, "Element not found");
    }

    public NonTrovatoException(){
        super("Element not found");
        this.idNotFound = null;
    }
}
