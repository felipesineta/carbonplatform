package com.fv.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "CONVERSA")
public class Conversa extends BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "conversaId")
	@SequenceGenerator(name = "conversaId", sequenceName = "conversaId", allocationSize = 1)
	Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario user1;

	@ManyToOne(fetch = FetchType.EAGER)
	private Usuario user2;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projeto_id")
	@JsonIgnoreProperties({"conversa"})
	private Projeto projeto;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "conversa", orphanRemoval = true)
	private List<Mensagem> mensagens = new ArrayList<Mensagem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUser1() {
		return user1;
	}

	public void setUser1(Usuario user1) {
		this.user1 = user1;
	}

	public Usuario getUser2() {
		return user2;
	}

	public void setUser2(Usuario user2) {
		this.user2 = user2;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public List<Mensagem> getMensagens() {
		return mensagens;
	}

	public void setMensagens(List<Mensagem> mensagens) {
		this.mensagens = mensagens;
	}
}
