package com.fv.repositories;

import org.springframework.data.repository.CrudRepository;

import com.fv.models.Projeto;

public interface ProjetoRepository extends CrudRepository<Projeto, Long> {

	Iterable<Projeto> findAllByCriadorId(Long id);
	Iterable<Projeto> findAllByParticipantesIdContains(Long id);

	Iterable<Projeto> findAllByHabilidadesIdContains(Long id);
	Iterable<Projeto> findAllByTopicosInteresseIdContains(Long id);
}
