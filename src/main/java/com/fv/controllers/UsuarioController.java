package com.fv.controllers;

import java.security.Principal;
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

import com.fv.models.Habilidade;
import com.fv.models.Projeto;
import com.fv.models.TopicoInteresse;
import com.fv.models.Usuario;
import com.fv.repositories.HabilidadeRepository;
import com.fv.repositories.ProjetoRepository;
import com.fv.repositories.TopicoInteresseRepository;
import com.fv.repositories.UsuarioRepository;

@RestController
@RequestMapping("api/usuario")
public class UsuarioController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	ProjetoRepository projetoRepository;
	
	@Autowired
	TopicoInteresseRepository topicoInteresseRepository;
	
	@Autowired
	HabilidadeRepository habilidadeRepository;
	
	@RequestMapping(value = "/all", method = RequestMethod.GET)
	public Iterable<Usuario> getAll() throws Exception {
		Iterable<Usuario> lis = usuarioRepository.findAll();
		return lis;
	}
	
	@RequestMapping(value = "/username{username}", method = RequestMethod.GET)
	public Usuario getByUsername(@PathVariable("username") String username) throws Exception {
		Optional<Usuario> opt = usuarioRepository.findByUsername(username);
		
		if (opt.isPresent()) {
			return opt.get();
		} else {
			Exception e = new Exception("Usuário não encontrado!");
			throw e;
		}
	}
	
	@RequestMapping(value = "/email{email}", method = RequestMethod.GET)
	public Usuario getByEmail(@PathVariable("email") String email) throws Exception {
		Optional<Usuario> opt = usuarioRepository.findByEmail(email);
		
		if (opt.isPresent()) {
			return opt.get();
		} else {
			Exception e = new Exception("Usuário não encontrado!");
			throw e;
		}
	}
	
	@RequestMapping(value = "/logado", method = RequestMethod.GET)
	public Usuario usuarioLogado(Principal principal) throws Exception {
		return usuarioRepository.findByEmail(principal.getName()).get();
	}
	
	@RequestMapping(value = "/atualiza", method = RequestMethod.PUT)
	public Usuario atualizaUsuario(HttpServletRequest request, @RequestBody Usuario reqUser) throws Exception {
		Usuario user = reqUser;
		
		List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();
		for (TopicoInteresse t : reqUser.getTopicosInteresse()) {
			topicosInteresse.add(topicoInteresseRepository.findOneByNome(t.getNome()).get());
		}
		user.setTopicosInteresse(topicosInteresse);
		
		List<Habilidade> habilidades = new ArrayList<Habilidade>();
		for (Habilidade h : reqUser.getHabilidades()) {
			habilidades.add(habilidadeRepository.findOneByNome(h.getNome()).get());
		}
		user.setHabilidades(habilidades);
		
		
		usuarioRepository.save(user);
		return user;
	}
	
	@RequestMapping(value = "/busca/p={projetoId}", method = RequestMethod.GET)
	public Iterable<Usuario> searchByProjeto(@PathVariable("projetoId") Long projetoId) throws Exception {
		Projeto projeto = projetoRepository.findById(projetoId).get();

		List<Habilidade> habilidadesProjeto = projeto.getHabilidades();
		List<TopicoInteresse> topicosProjeto = projeto.getTopicosInteresse();
		
		List<Usuario> listaFull = new ArrayList<Usuario>();
		
		for (Habilidade h : habilidadesProjeto) {
			Iterable<Usuario> usuariosHab = usuarioRepository.findAllByHabilidadesIdContains(h.getId());
			for (Usuario u : usuariosHab) {
				if (!listaFull.contains(u) && projeto.getCriador().getId() != u.getId()) {
					listaFull.add(u);
				}
			}
		}
		
		for (TopicoInteresse t : topicosProjeto) {
			Iterable<Usuario> usuariosTop = usuarioRepository.findAllByTopicosInteresseIdContains(t.getId());
			for (Usuario u : usuariosTop) {
				if (!listaFull.contains(u) && projeto.getCriador().getId() != u.getId()) {
					listaFull.add(u);
				}
			}
		}
		
		return listaFull;
	}
}
