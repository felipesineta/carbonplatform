package com.fv.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "MENSAGEM")
public class Mensagem extends BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "mensagemId")
	@SequenceGenerator(name = "mensagemId", sequenceName = "mensagemId", allocationSize = 1)
	Long id;

	@Column(name = "TEXTO", length = 1024)
	private String texto;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	private Usuario remetente;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JsonIgnoreProperties({"mensagens"})
	private Conversa conversa;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Usuario getRemetente() {
		return remetente;
	}

	public void setRemetente(Usuario remetente) {
		this.remetente = remetente;
	}

	public Conversa getConversa() {
		return conversa;
	}

	public void setConversa(Conversa conversa) {
		this.conversa = conversa;
	}
}
