package com.fv.repositories;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.fv.models.Conversa;

public interface ConversaRepository extends CrudRepository<Conversa, Long> {

	Optional<Conversa> findOneByUser1IdAndUser2Id(Long user1Id, Long user2Id);
	Optional<Conversa> findOneByProjetoId(Long projetoId);
	Iterable<Conversa> findAllByUser1Id(Long user1Id);
	Iterable<Conversa> findAllByUser2Id(Long user2Id);
}
