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
	public Long id;
	
	@Column(name = "DESCRICAO", length = 128)
	public String nome = null;
	
	@Column(name = "NOME", length = 1024)
	public String descricao = null;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JsonIgnoreProperties("projetos")
	public Usuario criador;

	@ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "projetos")
	@JsonIgnoreProperties("projetos")
	public List<Usuario> participantes = new ArrayList<Usuario>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("projetos")
	@JoinTable(name = "TOPICO_INTERESSE_PROJETO", joinColumns = @JoinColumn(name = "projeto_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "topicoInteresse_id", referencedColumnName = "id"))
	public List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("projetos")
	@JoinTable(name = "HABILIDADE_PROJETO", joinColumns = @JoinColumn(name = "projeto_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "habilidade_id", referencedColumnName = "id"))
	public List<Habilidade> habilidades = new ArrayList<Habilidade>();
}
