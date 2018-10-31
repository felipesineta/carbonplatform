package com.fv.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "TOPICO_INTERESSE")
public class TopicoInteresse extends BaseObject implements Serializable {


	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "topicoInteresseId")
	@SequenceGenerator(name = "topicoInteresseId", sequenceName = "topicoInteresseId", allocationSize = 1)
	Long id;
	
	@Column(name = "NOME", length = 128)
	private String nome = null;

	@ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "topicosInteresse")
	@JsonIgnoreProperties({"topicosInteresse", "habilidades", "projetos"})
	private List<Usuario> usuarios = new ArrayList<Usuario>();

	@ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "topicosInteresse")
	@JsonIgnoreProperties({"topicosInteresse", "habilidades", "criador", "participantes", "conversa"})
	private List<Projeto> projetos = new ArrayList<Projeto>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}
}
