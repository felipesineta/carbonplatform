package com.fv.controllers;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fv.models.Conversa;
import com.fv.repositories.ConversaRepository;
import com.fv.repositories.MensagemRepository;

@RestController
@RequestMapping("api/conversa")
public class ConversaController {
	
	@Autowired
	ConversaRepository conversaRepository;
	
	@Autowired
	ProjetoController projetoController;
	
	@Autowired
	MensagemRepository mensagemRepository;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Iterable<Conversa> getAll() throws Exception {
		return conversaRepository.findAll();
	}
	
	@RequestMapping(value = "/c={conversaId}", method = RequestMethod.GET)
	public Conversa getById(HttpServletRequest request, @PathVariable("conversaId") Long conversaId) throws Exception {
		return conversaRepository.findById(conversaId).get();
	}
	
	@RequestMapping(value = "/p={projetoId}", method = RequestMethod.GET)
	public Conversa getByProjetoId(HttpServletRequest request, @PathVariable("projetoId") Long projetoId) throws Exception {
		return conversaRepository.findOneByProjetoId(projetoId).get();
	}

	@RequestMapping(value = "/users/u1={user1Id}&&u2={user2Id}&&{novo}", method = RequestMethod.GET)
	public Conversa between(HttpServletRequest request,
			@PathVariable("user1Id") Long user1Id, @PathVariable("user2Id") Long user2Id, @PathVariable("novo") boolean novo) throws Exception {

		Optional<Conversa> c1 = conversaRepository.findOneByUser1IdAndUser2Id(user1Id, user2Id);
		Optional<Conversa> c2 = conversaRepository.findOneByUser1IdAndUser2Id(user2Id, user1Id);
		
		if (c1.isPresent()) {
			return c1.get();
		} else if (c2.isPresent()) {
			return c2.get();
		} else {
			if (novo) {
				return null;
			} else {
				Exception e = new Exception("Conversa entre usuários não encontrada!");
				throw e;
			}
		}
	}
	
	@RequestMapping(value = "/users/u={userId}", method = RequestMethod.GET)
	public List<Iterable<Conversa>> allFrom(HttpServletRequest request, @PathVariable("userId") Long userId) throws Exception {

		Iterable<Conversa> c1 = conversaRepository.findAllByUser1Id(userId);
		Iterable<Conversa> c2 = conversaRepository.findAllByUser2Id(userId);
		
		List<Iterable<Conversa>> conversas = new ArrayList<Iterable<Conversa>>();
		
		conversas.add(c1);
		conversas.add(c2);
		
		return conversas;
	}

	@RequestMapping(value = "/novou", method = RequestMethod.POST)
	public Conversa createU(HttpServletRequest request, @RequestBody Conversa conversa) throws Exception {
		
		Conversa existente = between(request, conversa.user1.id, conversa.user2.id, true);
			
		if (existente != null) {
			Exception e = new Exception("Conversa entre usuários já existente!");
			throw e;
		}
		else {
			conversaRepository.save(conversa);
			return conversa;
		}
	}

	@RequestMapping(value = "/novop", method = RequestMethod.POST)
	public Conversa createP(HttpServletRequest request, @RequestBody Conversa conversa) throws Exception {
		
		Optional<Conversa> existente = conversaRepository.findOneByProjetoId(conversa.projeto.id);
		
		if (existente.isPresent()) {
			Exception e = new Exception("Conversa para o projeto já existente!");
			throw e;
		}
		else {
			conversaRepository.save(conversa);
			
			projetoController.updateConversa(conversa.projeto.id, conversa);
			return conversa;
		}
	}
}
