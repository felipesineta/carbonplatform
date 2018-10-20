package com.fv.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fv.models.Conversa;
import com.fv.models.Habilidade;
import com.fv.models.Projeto;
import com.fv.models.TopicoInteresse;
import com.fv.models.Usuario;
import com.fv.repositories.ConversaRepository;
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
	ConversaRepository conversaRepository;

	@Autowired
	ProjetoRepository projetoRepository;

	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Iterable<Projeto> getAll() throws Exception {
		return projetoRepository.findAll();
	}
	
	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Projeto criaNovo(HttpServletRequest request, @RequestBody Projeto reqProjeto) throws Exception {
		Projeto projeto = reqProjeto;
		
		// lê os tópicos de interesse por nome e coloca o id
		List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();
		for (TopicoInteresse t : reqProjeto.topicosInteresse) {
			topicosInteresse.add(topicoInteresseRepository.findOneByNome(t.nome).get());
		}
		projeto.topicosInteresse = topicosInteresse;

		// lê as habilidades por nome e coloca o id
		List<Habilidade> habilidades = new ArrayList<Habilidade>();
		for (Habilidade h : reqProjeto.habilidades) {
			habilidades.add(habilidadeRepository.findOneByNome(h.nome).get());
		}
		projeto.habilidades = habilidades;

		// lê os participantes por email e coloca o id
		List<Usuario> participantes = new ArrayList<Usuario>();
		for (Usuario p : reqProjeto.participantes) {
			Usuario part = usuarioRepository.findByEmail(p.email).get();
			part.projetos.clear();
			participantes.add(part);
		}
		projeto.participantes = participantes;
		
		projetoRepository.save(projeto);

//		if (projeto.conversa == null) {
//			Conversa conversa = new Conversa();
//			conversa.projeto = projeto;
//			projeto.conversa = conversa;
//			projetoRepository.save(projeto);
////			conversaRepository.save(conversa);
//		}
		
		return projeto;
	}
	

	
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public Projeto atualiza(HttpServletRequest request, @RequestBody Projeto reqProjeto) throws Exception {
		Projeto projeto = reqProjeto;
		
		// lê os tópicos de interesse por nome e coloca o id
		List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();
		for (TopicoInteresse t : reqProjeto.topicosInteresse) {
			topicosInteresse.add(topicoInteresseRepository.findOneByNome(t.nome).get());
		}
		projeto.topicosInteresse = topicosInteresse;

		// lê as habilidades por nome e coloca o id
		List<Habilidade> habilidades = new ArrayList<Habilidade>();
		for (Habilidade h : reqProjeto.habilidades) {
			habilidades.add(habilidadeRepository.findOneByNome(h.nome).get());
		}
		projeto.habilidades = habilidades;

		// lê os participantes por email e coloca o id
		List<Usuario> participantes = new ArrayList<Usuario>();
		for (Usuario p : reqProjeto.participantes) {
			Usuario part = usuarioRepository.findByEmail(p.email).get();
			part.projetos.clear();
			participantes.add(part);
		}
		projeto.participantes = participantes;
		
		Conversa conversa = conversaRepository.findOneByProjetoId(projeto.id).get();
		projeto.conversa = conversa;
		
		projetoRepository.save(projeto);

//		if (projeto.conversa == null) {
//			Conversa conversa = new Conversa();
//			conversa.projeto = projeto;
//			projeto.conversa = conversa;
//			projetoRepository.save(projeto);
////			conversaRepository.save(conversa);
//		}
		
		return projeto;
	}
	
	public void updateConversa(Long projetoId, Conversa conversa) {
		Projeto projeto = projetoRepository.findById(projetoId).get();
//		conversa.projeto = null;
		projeto.conversa = conversa;
		projetoRepository.save(projeto);
	}
	
	@RequestMapping(value = "/u={userId}", method = RequestMethod.GET)
	public Iterable<Projeto> getFromUser(@PathVariable("userId") Long userId) throws Exception {
		Iterable<Projeto> dono = projetoRepository.findAllByCriadorId(userId);
		Iterable<Projeto> participante = projetoRepository.findAllByParticipantesIdContains(userId);
		
		List<Projeto> todos = (List<Projeto>) dono;
		for (Projeto p : participante) {
			todos.add(p);
		}
		
		return todos;
	}
	
	@RequestMapping(value = "/p={projetoId}", method = RequestMethod.GET)
	public Projeto getOne(@PathVariable("projetoId") Long projetoId) throws Exception {
		Projeto projeto = projetoRepository.findById(projetoId).get();
				
		return projeto;
	}
}
