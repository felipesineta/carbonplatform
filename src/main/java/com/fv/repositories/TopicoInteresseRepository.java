package com.fv.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.fv.models.TopicoInteresse;

public interface TopicoInteresseRepository  extends CrudRepository<TopicoInteresse, Long> {

	Page<TopicoInteresse> findAllByNomeContains(String nome, Pageable page);
	Optional<TopicoInteresse> findOneByNome(String nome);
}
