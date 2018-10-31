package com.fv.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "PROJETO")
public class Projeto extends BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "projetoId")
	@SequenceGenerator(name = "projetoId", sequenceName = "projetoId", allocationSize = 1)
	Long id;
	
	@Column(name = "DESCRICAO", length = 128)
	private String nome = null;
	
	@Column(name = "NOME", length = 1024)
	private String descricao = null;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JsonIgnoreProperties({"habilidades", "topicosInteresse", "projetos"})
	private Usuario criador;

	@ManyToMany(cascade = CascadeType.ALL)
	@JsonIgnoreProperties({"habilidades", "topicosInteresse", "projetos"})
	@JoinTable(name = "PROJETO_USUARIO", joinColumns = @JoinColumn(name = "projeto_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"))
	private List<Usuario> participantes = new ArrayList<Usuario>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"usuarios", "projetos"})
	@JoinTable(name = "TOPICO_INTERESSE_PROJETO", joinColumns = @JoinColumn(name = "projeto_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "topicoInteresse_id", referencedColumnName = "id"))
	private List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"usuarios", "projetos"})
	@JoinTable(name = "HABILIDADE_PROJETO", joinColumns = @JoinColumn(name = "projeto_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "habilidade_id", referencedColumnName = "id"))
	private List<Habilidade> habilidades = new ArrayList<Habilidade>();

	@OneToOne(mappedBy = "projeto", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonIgnoreProperties({"projeto"})
	private Conversa conversa;

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

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Usuario getCriador() {
		return criador;
	}

	public void setCriador(Usuario criador) {
		this.criador = criador;
	}

	public List<Usuario> getParticipantes() {
		return participantes;
	}

	public void setParticipantes(List<Usuario> participantes) {
		this.participantes = participantes;
	}

	public List<TopicoInteresse> getTopicosInteresse() {
		return topicosInteresse;
	}

	public void setTopicosInteresse(List<TopicoInteresse> topicosInteresse) {
		this.topicosInteresse = topicosInteresse;
	}

	public List<Habilidade> getHabilidades() {
		return habilidades;
	}

	public void setHabilidades(List<Habilidade> habilidades) {
		this.habilidades = habilidades;
	}

	public Conversa getConversa() {
		return conversa;
	}

	public void setConversa(Conversa conversa) {
		this.conversa = conversa;
	}
}
