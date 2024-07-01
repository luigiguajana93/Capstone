package it.epicode.capstone.security;

public class FileSizeExceededException extends RuntimeException{
    public FileSizeExceededException(String message) {
        super(message);
    }
}
