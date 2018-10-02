package com.fv.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fv.models.Habilidade;
import com.fv.models.Projeto;
import com.fv.models.TopicoInteresse;
import com.fv.models.Usuario;
import com.fv.repositories.HabilidadeRepository;
import com.fv.repositories.ProjetoRepository;
import com.fv.repositories.TopicoInteresseRepository;
import com.fv.repositories.UsuarioRepository;

@RestController
@RequestMapping("api/projeto")
public class ProjetoController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	TopicoInteresseRepository topicoInteresseRepository;
	
	@Autowired
	HabilidadeRepository habilidadeRepository;

	@Autowired
	ProjetoRepository projetoRepository;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Iterable<Projeto> getAll() throws Exception {
		return projetoRepository.findAll();
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Projeto criaNovo(HttpServletRequest request, @RequestBody Projeto reqProjeto) throws Exception {
		Projeto projeto = reqProjeto;
		
		List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();
		for (TopicoInteresse t : reqProjeto.topicosInteresse) {
			topicosInteresse.add(topicoInteresseRepository.findOneByNome(t.nome).get());
		}
		projeto.topicosInteresse = topicosInteresse;
	
		List<Habilidade> habilidades = new ArrayList<Habilidade>();
		for (Habilidade h : reqProjeto.habilidades) {
			habilidades.add(habilidadeRepository.findOneByNome(h.nome).get());
		}
		projeto.habilidades = habilidades;
		
		List<Usuario> participantes = new ArrayList<Usuario>();
		for (Usuario p : reqProjeto.participantes) {
			Usuario part = usuarioRepository.findByEmail(p.email).get();
			part.projetos.clear();
			participantes.add(part);
		}
		projeto.participantes = participantes;

		projetoRepository.save(projeto);
		
		Usuario cria = projeto.criador;
		List<Usuario> parts = projeto.participantes;
		
		projeto.criador = null;
		projeto.participantes.clear();
		
		cria.projetos.add(projeto);
		cria = usuarioRepository.findById(cria.id).get();
		usuarioRepository.save(cria);
		
		for (Usuario u : parts) {
			u = usuarioRepository.findById(u.id).get();
			u.projetos.add(projeto);
			usuarioRepository.save(u);
		}
		
		
		projeto.criador = usuarioRepository.findById(projeto.criador.id).get();
		return projeto;
	}
}
