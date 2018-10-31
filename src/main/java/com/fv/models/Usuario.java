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
	Long id;
	
	@Column(name = "NOME", length = 128)
	private String nome = null;

	@Column(name = "SOBRENOME", length = 128)
	private String sobrenome = null;

	@Index(name = "IDX_EMAIL")
	@Column(name = "EMAIL", length = 100)
	private String email = null;

	@Index(name = "IDX_USERNAME")
	@Column(name = "USERNAME", length = 100, nullable = false)
	private String username = null;
	
	@Column(name = "PASSWORD", length = 128)
	private String password = "";

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"usuarios", "projetos"})
	@JoinTable(name = "TOPICO_INTERESSE_USUARIO", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "topicoInteresse_id", referencedColumnName = "id"))
	private List<TopicoInteresse> topicosInteresse = new ArrayList<TopicoInteresse>();

	@ManyToMany(fetch = FetchType.EAGER)
	@JsonIgnoreProperties({"usuarios", "projetos"})
	@JoinTable(name = "HABILIDADE_USUARIO", joinColumns = @JoinColumn(name = "usuario_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "habilidade_id", referencedColumnName = "id"))
	private List<Habilidade> habilidades = new ArrayList<Habilidade>();

	@ManyToMany(cascade = { CascadeType.ALL }, mappedBy = "participantes")
	@JsonIgnoreProperties({"topicosInteresse", "habilidades", "criador", "participantes", "conversa"})
	private List<Projeto> projetos = new ArrayList<Projeto>();
	
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

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}
}
