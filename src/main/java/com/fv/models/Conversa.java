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
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "CONVERSA")
public class Conversa extends BaseObject implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "conversaId")
	@SequenceGenerator(name = "conversaId", sequenceName = "conversaId", allocationSize = 1)
	public Long id;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Usuario user1;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Usuario user2;
//	Projeto destinoP;

	@OneToMany(cascade = { CascadeType.ALL }, fetch = FetchType.EAGER, mappedBy = "conversa", orphanRemoval = true)
	public List<Mensagem> mensagens = new ArrayList<Mensagem>();
}
