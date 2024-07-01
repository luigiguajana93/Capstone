package it.epicode.capstone.security;

import it.epicode.capstone.utenti.Utente;
import it.epicode.capstone.utenti.UtenteRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApplicationUserDetailsService implements UserDetailsService {

    @Autowired
    UtenteRepository user;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var userEntity = user.findOneByUsername(username).orElseThrow();
        return SecurityUserDetails.build(userEntity);
    }


}
