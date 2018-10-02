package com.fv.repositories;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

import com.fv.models.Habilidade;

public interface HabilidadeRepository   extends CrudRepository<Habilidade, Long> {

	Page<Habilidade> findAllByNomeContains(String nome, Pageable page);
	Optional<Habilidade> findOneByNome(String nome);
}