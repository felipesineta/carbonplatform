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
import javax.persistence.JoinColumn;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.eclipse.persistence.annotations.Index;

//import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "USUARIO")
public class Usuario extends BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "usuarioId")
	@SequenceGenerator(name = "usuarioId", sequenceName = "usuarioId", allocationSize = 1)
	public Long id;
	
	@Column(name = "NOME", length = 128)
	public String nome = null;

	@Column(name = "SOBRENOME", length = 128)
	public String sobrenome = null;

	@Index(name = "IDX_EMAIL")
	@Column(name = "EMAIL", length = 100)
	public String email = null;

	@Index(name = "IDX_USERNAME")
	@Column(name = "USERNAME", length = 100, nullable = false)
	public String username = null;
	
	@Column(name = "PASSWORD", length = 128)
	public String password = "";

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("usuarios")
	@JoinTable(name = "TOPICO_INTERESSE_USUARIO", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "topicoInteresse_id", referencedColumnName = "id"))
	public List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("usuarios")
	@JoinTable(name = "HABILIDADE_USUARIO", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "habilidade_id", referencedColumnName = "id"))
	public List<Habilidade> habilidades = new ArrayList<Habilidade>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties("participantes")
	@JoinTable(name = "PROJETO_USUARIO", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "projeto_id", referencedColumnName = "id"))
	public List<Projeto> projetos = new ArrayList<Projeto>();
	
	public Usuario() {
		
	}
	
	public Usuario(Usuario user) {
		this.id = user.id;
		this.nome = user.nome;
		this.sobrenome = user.sobrenome;
		this.email = user.email;
		this.username = user.username;
		this.password = user.password;
		this.topicosInteresse = user.topicosInteresse;
		this.habilidades = user.habilidades;
		this.projetos = user.projetos;
	}
}
