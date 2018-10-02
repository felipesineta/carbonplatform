package com.fv.repositories;

import org.springframework.data.repository.CrudRepository;

import com.fv.models.Mensagem;

public interface MensagemRepository extends CrudRepository<Mensagem, Long> {
	
}
