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
	public Long id;

	@ManyToOne(fetch = FetchType.EAGER)
	public Usuario user1;

	@ManyToOne(fetch = FetchType.EAGER)
	public Usuario user2;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "projeto_id")
	@JsonIgnoreProperties({"conversa"})
	public Projeto projeto;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY, mappedBy = "conversa", orphanRemoval = true)
	public List<Mensagem> mensagens = new ArrayList<Mensagem>();
}
