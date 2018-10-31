package com.fv.services;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.util.StringUtils;

import com.fv.models.Usuario;

@Service
public class SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    String remetente = "plataformacarbon@outlook.com";
    
    public void sendMailDados(Usuario usuario, String uncodedPassword) throws MessagingException {
    	Context ctx = new Context();
    	
    	String nomeCompleto = StringUtils.capitalize(usuario.getNome()) + " "
                + StringUtils.capitalize(usuario.getSobrenome());
    	
    	ctx.setVariable("fullName", nomeCompleto);
    	ctx.setVariable("username", usuario.getUsername());
    	ctx.setVariable("password", uncodedPassword);
    	
    	final MimeMessage mimeMessage = this.javaMailSender.createMimeMessage();
        final MimeMessageHelper message = new MimeMessageHelper(mimeMessage, "UTF-8");
        
        message.setSubject(StringUtils.capitalize(usuario.getNome()) + ", cadastro feito na Plataforma Carbon!");
        message.setFrom(remetente);
        message.setTo(usuario.getEmail());
        
     // Create the HTML body using Thymeleaf
        final String htmlContent = this.templateEngine.process("emailAcesso", ctx);
        message.setText(htmlContent, true);

        // Send email
        this.javaMailSender.send(mimeMessage);
    }
}
