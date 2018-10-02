package com.fv.services;

import java.util.HashSet;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fv.models.Usuario;
import com.fv.repositories.UsuarioRepository;

@Service
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private UsuarioRepository usuarioRepository;
	
    @Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<Usuario> user = usuarioRepository.findByUsername(username);
		
		if(!user.isPresent()) {
	        throw new UsernameNotFoundException("Invalid User");
	    }
	    else {
	    	return new org
	                .springframework
	                .security
	                .core
	                .userdetails
	                .User(user.get().email, user.get().password, new HashSet<>());
	    }
	}
}
