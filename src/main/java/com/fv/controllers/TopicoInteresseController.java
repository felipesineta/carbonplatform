package com.fv.controllers;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fv.models.TopicoInteresse;
import com.fv.repositories.TopicoInteresseRepository;

@RestController
@RequestMapping("api/topicointeresse")
public class TopicoInteresseController {

	@Autowired
	TopicoInteresseRepository topicoInteresseRepository;
	
	@RequestMapping	(value = "/all", method = RequestMethod.GET)
	public Iterable<TopicoInteresse> getAll() {
		return topicoInteresseRepository.findAll();
	}
	
	@RequestMapping	(value = "/novo", method = RequestMethod.POST)
	public Iterable<TopicoInteresse> create(HttpServletRequest request, @RequestBody TopicoInteresse topicoInteresse) throws Exception {
		topicoInteresseRepository.save(topicoInteresse);
		return topicoInteresseRepository.findAll();
	}
}
