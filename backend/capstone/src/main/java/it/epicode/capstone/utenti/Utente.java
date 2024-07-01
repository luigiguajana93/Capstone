package it.epicode.capstone.utenti;

import it.epicode.capstone.noleggi.RegistroNoleggio;
import it.epicode.capstone.ordini.Ordine;
import it.epicode.capstone.security.Roles;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utenti")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(setterPrefix = "with")
public class Utente {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String username;
    private String email;
    private String nome;
    private String cognome;
    private String citta;
    private String tipoIndirizzo;
    private String indirizzo;
    private String civico;
    private String cap;
    private String numeroTelefono;

    @Column(length = 125, nullable = false)
    private String password;
    private String avatar;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Roles> roles = new ArrayList<>();

    @OneToMany(mappedBy ="utente", fetch = FetchType.LAZY)
    private List<Ordine> ordini = new ArrayList<>();

    @OneToMany(mappedBy ="utente", fetch = FetchType.LAZY)
    private List<RegistroNoleggio> noleggi = new ArrayList<>();
}
