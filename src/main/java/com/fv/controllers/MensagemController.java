package com.fv.controllers;

import java.util.List;

//import java.util.ArrayList;
//import java.util.Collections;
//import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fv.models.Conversa;
//import com.fv.helper.SortByTimestamp;
import com.fv.models.Mensagem;
import com.fv.repositories.ConversaRepository;
import com.fv.repositories.MensagemRepository;

@RestController
@RequestMapping("api/mensagem")
public class MensagemController {
	
	@Autowired
	MensagemRepository mensagemRepository;
	
	@Autowired
	ConversaRepository conversaRepository;
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Mensagem create(HttpServletRequest request, @RequestBody Mensagem mensagem) throws Exception {
		mensagemRepository.save(mensagem);
		
		Conversa conversa = conversaRepository.findById(mensagem.getConversa().getId()).get();
		
		List<Mensagem> mensagens = conversa.getMensagens();
		mensagens.add(mensagem);
		conversa.setMensagens(mensagens);
		
		conversaRepository.save(conversa);
		
		return mensagemRepository.findById(mensagem.getId()).get();
	}

}
