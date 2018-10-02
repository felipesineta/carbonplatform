package com.fv.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.fv.models.Usuario;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

	Page<Usuario> findAll(Pageable page);

	Optional<Usuario> findByUsername(String username);
	Optional<Usuario> findByEmail(String email);
	
	Iterable<Usuario> findAllByEmail(String email);
	Page<Usuario> findAllByNomeContains(String nome, Pageable page);
	Page<Usuario> findAllBySobrenomeContains(String sobrenome, Pageable page);
    Page<Usuario> findAllByEmailContains(String email, Pageable page);
	Page<Usuario> findAllByNomeContainsAndSobrenomeContains(String nome, String sobrenome, Pageable page);
	Page<Usuario> findAllByNomeContainsAndEmailContains(String nome, String email, Pageable page);
	Page<Usuario> findAllBySobrenomeContainsAndEmailContains(String sobrenome, String email, Pageable page);
	Page<Usuario> findAllByNomeContainsAndSobrenomeContainsAndEmailContains(String nome, String sobrenome, String email, Pageable page);

}