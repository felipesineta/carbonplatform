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
	public Long id;

	@Column(name = "TEXTO", length = 1024)
	public String texto;

	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	public Usuario remetente;
	
	@ManyToOne(fetch = FetchType.EAGER, optional = false)
	@JsonIgnoreProperties({"mensagens"})
    public Conversa conversa;
}
