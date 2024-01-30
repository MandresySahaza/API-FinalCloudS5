package com.pkg.occasion.user;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.pkg.occasion.api.model.Utilisateur;
import com.pkg.occasion.api.repository.UtilisateurRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
    @Autowired
    private UtilisateurRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Utilisateur> user = repository.findByMail(username);
        return new User(user.get().getUsername() , user.get().getPassword(), user.get().getAuthorities());
    }
    
    
}
