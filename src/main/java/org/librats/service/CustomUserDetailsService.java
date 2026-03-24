package org.librats.service;

import org.librats.model.User;
import org.librats.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o usuário no banco
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        // Retorna um objeto "UserDetails" que o Spring Security entende
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(), // Esta senha já deve estar criptografada no banco!
                new ArrayList<>()   // Aqui ficariam as permissões (ex: ROLE_USER, ROLE_ADMIN)
        );
    }
}