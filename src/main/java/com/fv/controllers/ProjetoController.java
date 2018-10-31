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
		for (TopicoInteresse t : reqProjeto.getTopicosInteresse()) {
			topicosInteresse.add(topicoInteresseRepository.findOneByNome(t.getNome()).get());
		}
		projeto.setTopicosInteresse(topicosInteresse);

		// lê as habilidades por nome e coloca o id
		List<Habilidade> habilidades = new ArrayList<Habilidade>();
		for (Habilidade h : reqProjeto.getHabilidades()) {
			habilidades.add(habilidadeRepository.findOneByNome(h.getNome()).get());
		}
		projeto.setHabilidades(habilidades);

		// lê os participantes por email e coloca o id
		List<Usuario> participantes = new ArrayList<Usuario>();
		for (Usuario p : reqProjeto.getParticipantes()) {
			Usuario part = usuarioRepository.findByEmail(p.getEmail()).get();
			part.getProjetos().clear();
			participantes.add(part);
		}
		projeto.setParticipantes(participantes);
		
		projetoRepository.save(projeto);

		return projeto;
	}
	

	
	@RequestMapping(value = "/update", method = RequestMethod.PUT)
	public Projeto atualiza(HttpServletRequest request, @RequestBody Projeto reqProjeto) throws Exception {
		Projeto projeto = reqProjeto;
		
		// lê os tópicos de interesse por nome e coloca o id
		List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();
		for (TopicoInteresse t : reqProjeto.getTopicosInteresse()) {
			topicosInteresse.add(topicoInteresseRepository.findOneByNome(t.getNome()).get());
		}
		projeto.setTopicosInteresse(topicosInteresse);

		// lê as habilidades por nome e coloca o id
		List<Habilidade> habilidades = new ArrayList<Habilidade>();
		for (Habilidade h : reqProjeto.getHabilidades()) {
			habilidades.add(habilidadeRepository.findOneByNome(h.getNome()).get());
		}
		projeto.setHabilidades(habilidades);

		// lê os participantes por email e coloca o id
		List<Usuario> participantes = new ArrayList<Usuario>();
		for (Usuario p : reqProjeto.getParticipantes()) {
			Usuario part = usuarioRepository.findByEmail(p.getEmail()).get();
			part.getProjetos().clear();
			participantes.add(part);
		}
		projeto.setParticipantes(participantes);
		
		Conversa conversa = conversaRepository.findOneByProjetoId(projeto.getId()).get();
		projeto.setConversa(conversa);
		
		projetoRepository.save(projeto);

		return projeto;
	}
	
	public void updateConversa(Long projetoId, Conversa conversa) {
		Projeto projeto = projetoRepository.findById(projetoId).get();
		projeto.setConversa(conversa);
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
	
	@RequestMapping(value = "/c={userId}", method = RequestMethod.GET)
	public Iterable<Projeto> getFromCriador(@PathVariable("userId") Long userId) throws Exception {
		Iterable<Projeto> dono = projetoRepository.findAllByCriadorId(userId);
		
		return dono;
	}
	
	@RequestMapping(value = "/p={projetoId}", method = RequestMethod.GET)
	public Projeto getOne(@PathVariable("projetoId") Long projetoId) throws Exception {
		Projeto projeto = projetoRepository.findById(projetoId).get();
				
		return projeto;
	}
	
	@RequestMapping(value = "/busca/u={usuarioId}", method = RequestMethod.GET)
	public Iterable<Projeto> searchByUser(@PathVariable("usuarioId") Long usuarioId) throws Exception {
		Usuario user = usuarioRepository.findById(usuarioId).get();

		List<Habilidade> habilidadesUsuario = user.getHabilidades();
		List<TopicoInteresse> topicosUsuario = user.getTopicosInteresse();
		
		List<Projeto> listaFull = new ArrayList<Projeto>();
		
		for (Habilidade h : habilidadesUsuario) {
			Iterable<Projeto> projetosHab = projetoRepository.findAllByHabilidadesIdContains(h.getId());
			for (Projeto p : projetosHab) {
				if (!listaFull.contains(p) && user.getId() != p.getCriador().getId()) {
					listaFull.add(p);
				}
			}
		}
		
		for (TopicoInteresse t : topicosUsuario) {
			Iterable<Projeto> projetosTop = projetoRepository.findAllByTopicosInteresseIdContains(t.getId());
			for (Projeto p : projetosTop) {
				if (!listaFull.contains(p) && user.getId() != p.getCriador().getId()) {
					listaFull.add(p);
				}
			}
		}
		
		return listaFull;
	}
}
