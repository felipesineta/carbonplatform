package com.fv.controllers;

import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import com.fv.models.Usuario;
import com.fv.repositories.UsuarioRepository;
import com.fv.services.SendMailService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/cadastro")
public class CadastroController {
	
	@Autowired
	UsuarioRepository usuarioRepository;
	
	@Autowired
	SendMailService sendMailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

	@RequestMapping(value = "/novo", method = RequestMethod.POST)
	public Usuario cadastro(HttpServletRequest request, @RequestBody Usuario reqUser) throws Exception {
		Optional<Usuario> optUser = usuarioRepository.findByUsername(reqUser.getUsername());
		
		if (optUser.isPresent()) {
			Exception e = new Exception("Nome de usuário já em uso.");
			throw e;
		} else {
			optUser = usuarioRepository.findByEmail(reqUser.getEmail());
			
			if (optUser.isPresent()) {
				Exception e = new Exception("E-mail já cadastrado.");
				throw e;
			} else {
				String unencodedPassword = reqUser.getPassword();
				reqUser.setPassword( passwordEncoder.encode(reqUser.getPassword()));
				reqUser.createdBy = "system";
				usuarioRepository.save(reqUser);
				
				sendMailService.sendMailDados(reqUser, unencodedPassword);
			}
			
			return reqUser;
		}
	}
}
