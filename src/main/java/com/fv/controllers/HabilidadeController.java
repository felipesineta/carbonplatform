package com.fv.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fv.models.Habilidade;
import com.fv.repositories.HabilidadeRepository;

@RestController
@RequestMapping("api/habilidade")
public class HabilidadeController {

	@Autowired
	HabilidadeRepository habilidadeRepository;
	
	@RequestMapping	(value = "/all", method = RequestMethod.GET)
	public Iterable<Habilidade> getAll() {
		return habilidadeRepository.findAll();
	}
	
	@RequestMapping	(value = "/novo", method = RequestMethod.POST)
	public Iterable<Habilidade> create(HttpServletRequest request, @RequestBody Habilidade habilidade) throws Exception {
		habilidadeRepository.save(habilidade);
		return habilidadeRepository.findAll();
	}
}
