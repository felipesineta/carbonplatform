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
@Table(name = "HABILIDADE")
public class Habilidade extends BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "habilidadeId")
	@SequenceGenerator(name = "habilidadeId", sequenceName = "habilidadeId", allocationSize = 1)
	public Long id;
	
	@Column(name = "NOME", length = 128)
	public String nome = null;

	@ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "habilidades")
	@JsonIgnoreProperties("habilidades")
	public List<Usuario> usuarios = new ArrayList<Usuario>();

	@ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "habilidades")
	@JsonIgnoreProperties("habilidades")
	public List<Projeto> projetos = new ArrayList<Projeto>();
}