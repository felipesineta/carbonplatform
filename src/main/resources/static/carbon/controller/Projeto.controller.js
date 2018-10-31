sap.ui.define([
	'carbon/controller/BaseController',
	'carbon/formatter',
], (BaseController, Formatter) => BaseController.extend("carbon.controller.Projeto", {

	formatter: Formatter,

	onInit() {
		const oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		oRouter.getRoute("toNovoProjeto").attachPatternMatched(this.create, this);
		oRouter.getRoute("toShowProjeto").attachPatternMatched(this.show, this);
		oRouter.getRoute("toShowProjetoPublic").attachPatternMatched(this.showPublic, this);
	},
	
	create() {
		jQuery.sap.require("sap.m.MessageBox");
		
		this.getView().byId("abaComentarios").setVisible(false);

		this.getView().byId("nomeProjeto").setValue("");
		this.getView().byId("nomeProjeto").setEnabled(true);
		this.getView().byId("descricaoProjeto").setValue("");
		this.getView().byId("descricaoProjeto").setEnabled(true);

		this.getView().byId("inputTopicos").setValue("");
		this.getView().byId("inputTopicos").setVisible(true);
		this.getView().byId("btnTopicos").setVisible(true);
		this.getView().byId("panelTopicos").destroyContent();
		this.getView().byId("inputHabilidades").setValue("");
		this.getView().byId("inputHabilidades").setVisible(true);
		this.getView().byId("btnHabilidades").setVisible(true);
		this.getView().byId("panelHabilidades").destroyContent();
		this.getView().byId("inputParticipantes").setValue("");
		this.getView().byId("inputParticipantes").setVisible(true);
		this.getView().byId("btnParticipantes").setVisible(true);
		this.getView().byId("panelParticipantes").destroyContent();
		
		this.carregaModelosBase();

		this.getView().byId("btnRodape").setText("Criar");
		this.getView().byId("tituloPage").setTitle("Novo projeto");

		this.getView().byId("btnRodape").setVisible(true);
		this.getView().byId("btnRodape2").setVisible(false);
		
		this.verb = "POST";
	},
	
	show(oEvent) {
		const projetoId = oEvent.getParameter('arguments').projetoId;
		
		const oModelProjeto = new sap.ui.model.json.JSONModel();
		oModelProjeto.setData(jQuery.sap.syncGetJSON(`api/projeto/p=${projetoId}`).data);
		this.getView().setModel(oModelProjeto, "projeto");
		
		let boolDono;
		if (this.getView().getModel("projeto").getData().criador.id != sap.ui.getCore().getModel("logado").getData().id) {
			boolDono = false;
		} else {
			boolDono = true;
		}
		
		this.getView().byId("abaComentarios").setVisible(true);
		this.getView().byId("tabBar").setSelectedKey("1");
		
		this.getView().byId("nomeProjeto").setValue(oModelProjeto.getData().nome);
		this.getView().byId("nomeProjeto").setEnabled(boolDono);
		this.getView().byId("descricaoProjeto").setValue(oModelProjeto.getData().descricao);
		this.getView().byId("descricaoProjeto").setEnabled(boolDono);
		
		this.getView().byId("inputTopicos").setValue("");
		this.getView().byId("inputTopicos").setVisible(boolDono);
		this.getView().byId("btnTopicos").setVisible(boolDono);
		this.getView().byId("panelTopicos").destroyContent();
		this.getView().byId("inputHabilidades").setValue("");
		this.getView().byId("inputHabilidades").setVisible(boolDono);
		this.getView().byId("btnHabilidades").setVisible(boolDono);
		this.getView().byId("panelHabilidades").destroyContent();
		this.getView().byId("inputParticipantes").setValue("");
		this.getView().byId("inputParticipantes").setVisible(boolDono);
		this.getView().byId("btnParticipantes").setVisible(boolDono);
		this.getView().byId("panelParticipantes").destroyContent();
		
		this.carregaModelosBase();
		
		this.getView().byId("btnRodape").setText("Salvar");
		this.getView().byId("tituloPage").setTitle("Projeto");
		
		const that = this;
		this.getView().getModel("projeto").getData().topicosInteresse.forEach(function(item) {
			that.getView().byId("inputTopicos").setValue(item.nome);
			that.onAddTopico();
		});
		if (this.getView().getModel("projeto").getData().criador.id != sap.ui.getCore().getModel("logado").getData().id) {
			this.getView().byId("panelTopicos").getContent().forEach(function(item) {
				item.setEditable(boolDono);
			});
		}
		
		this.getView().getModel("projeto").getData().habilidades.forEach(function(item) {
			that.getView().byId("inputHabilidades").setValue(item.nome);
			that.onAddHabilidade();
		});
		if (this.getView().getModel("projeto").getData().criador.id != sap.ui.getCore().getModel("logado").getData().id) {
			this.getView().byId("panelHabilidades").getContent().forEach(function(item) {
				item.setEditable(boolDono);
			});
		}
		
		this.getView().getModel("projeto").getData().participantes.forEach(function(item) {
			that.getView().byId("inputParticipantes").setValue(item.nome + " " + item.sobrenome + " (" + item.email + ")");
			that.onAddParticipante();
		});
		if (this.getView().getModel("projeto").getData().criador.id != sap.ui.getCore().getModel("logado").getData().id) {
			this.getView().byId("panelParticipantes").getContent().forEach(function(item) {
				item.setEditable(boolDono);
			});
		}
		
		const pId = this.getView().getModel("projeto").getData().id;
		const conversa = jQuery.sap.syncGetJSON(`api/conversa/p=${pId}`).data;
		
		// carrega os models da conversa
		const oModelConversa = new sap.ui.model.json.JSONModel();
		oModelConversa.setData(conversa);
		this.getView().setModel(oModelConversa, "conversa");
		
		const oModelMensagens = new sap.ui.model.json.JSONModel();
		oModelMensagens.setData(conversa.mensagens.reverse());
		this.getView().setModel(oModelMensagens, "mensagens");

		this.getView().byId("feedInput").setEnabled(true);

		if (this.getView().getModel("projeto").getData().criador.id != sap.ui.getCore().getModel("logado").getData().id) {
			this.getView().byId("btnRodape").setVisible(false);
		} else {
			this.getView().byId("btnRodape").setVisible(true);
		}
		this.getView().byId("btnRodape2").setVisible(false);
		
		this.verb = "PUT";
	},
	
	showPublic(oEvent) {
		const projetoId = oEvent.getParameter('arguments').projetoId;
		
		const oModelProjeto = new sap.ui.model.json.JSONModel();
		oModelProjeto.setData(jQuery.sap.syncGetJSON(`api/projeto/p=${projetoId}`).data);
		this.getView().setModel(oModelProjeto, "projeto");
		
		this.getView().byId("abaComentarios").setVisible(true);
		this.getView().byId("tabBar").setSelectedKey("1");

		this.getView().byId("nomeProjeto").setValue(oModelProjeto.getData().nome);
		this.getView().byId("nomeProjeto").setEnabled(false);
		this.getView().byId("descricaoProjeto").setValue(oModelProjeto.getData().descricao);
		this.getView().byId("descricaoProjeto").setEnabled(false);

		this.getView().byId("inputTopicos").setVisible(false);
		this.getView().byId("panelTopicos").destroyContent();
		this.getView().byId("btnTopicos").setVisible(false);
		this.getView().byId("inputHabilidades").setVisible(false);
		this.getView().byId("btnHabilidades").setVisible(false);
		this.getView().byId("panelHabilidades").destroyContent();
		this.getView().byId("inputParticipantes").setVisible(false);
		this.getView().byId("btnParticipantes").setVisible(false);
		this.getView().byId("panelParticipantes").destroyContent();
		
		this.carregaModelosBase();
		
		this.getView().byId("btnRodape").setText("Salvar");
		this.getView().byId("tituloPage").setTitle("Projeto");

		const that = this;
		this.getView().getModel("projeto").getData().topicosInteresse.forEach(function(item) {
			that.getView().byId("inputTopicos").setValue(item.nome);
			that.onAddTopico();
		});
		this.getView().byId("panelTopicos").getContent().forEach(function(item) {
			item.setEditable(false);
		});
		
		this.getView().getModel("projeto").getData().habilidades.forEach(function(item) {
			that.getView().byId("inputHabilidades").setValue(item.nome);
			that.onAddHabilidade();
		});
		this.getView().byId("panelHabilidades").getContent().forEach(function(item) {
			item.setEditable(false);
		});
		
		this.getView().getModel("projeto").getData().participantes.forEach(function(item) {
			that.getView().byId("inputParticipantes").setValue(item.nome + " " + item.sobrenome + " (" + item.email + ")");
			that.onAddParticipante();
		});
		this.getView().byId("panelParticipantes").getContent().forEach(function(item) {
			item.setEditable(false);
		});
		
		const pId = this.getView().getModel("projeto").getData().id;
		const conversa = jQuery.sap.syncGetJSON(`api/conversa/p=${pId}`).data;
		
		// carrega os models da conversa
		const oModelConversa = new sap.ui.model.json.JSONModel();
		oModelConversa.setData(conversa);
		this.getView().setModel(oModelConversa, "conversa");
		
		const oModelMensagens = new sap.ui.model.json.JSONModel();
		oModelMensagens.setData(conversa.mensagens.reverse());
		this.getView().setModel(oModelMensagens, "mensagens");

		this.getView().byId("feedInput").setEnabled(false);
		
		this.getView().byId("btnRodape").setVisible(false);
		this.getView().byId("btnRodape2").setVisible(true);
		this.verb = "---";
	},
	
	carregaModelosBase() {
		// carrega os topicos de interesse nas sugestoes
		const oModelTopicosInteresse = new sap.ui.model.json.JSONModel();
		oModelTopicosInteresse.setData(jQuery.sap.syncGetJSON(`api/topicointeresse/all`).data);
		this.getView().setModel(oModelTopicosInteresse, "topicos");

		// carrega as habilildades nas sugestoes
		const oModelHabilidades = new sap.ui.model.json.JSONModel();
		oModelHabilidades.setData(jQuery.sap.syncGetJSON(`api/habilidade/all`).data);
		this.getView().setModel(oModelHabilidades, "habilidades");

		// carrega os usuarios nas sugestoes
		const oModelUsuarios = new sap.ui.model.json.JSONModel();
		let aUsuarios = jQuery.sap.syncGetJSON(`api/usuario/all`).data;
		
		const idUser = sap.ui.getCore().getModel("logado").getData().id;
		let indice = -1;
		aUsuarios.forEach((item, index) => {
			if (item.id == idUser) {
				indice = index;
			}
		});
		if (indice > -1) {
			aUsuarios.splice(indice, 1);
		}
		
		oModelUsuarios.setData(aUsuarios);
		this.getView().setModel(oModelUsuarios, "usuarios");
	},
	
	onAddTopico(oEvent) {
		if (this.getView().byId("inputTopicos").getValue() == "") {
			sap.m.MessageBox.error("Tópico de interesse em branco!", {title: "Erro"});
			return;
		}
		
		const dialog = new sap.m.BusyDialog();
		dialog.open();
		
		// guarda o valor da busca
		const nomeTopico = this.getView().byId("inputTopicos").getValue().toUpperCase();
		
		// ve se já tem no projeto, só adiciona se ainda não tem
		let jaTem = false;
		let aTokens = this.getView().byId("panelTopicos").getContent();
		aTokens.forEach((item) => {
			if (item.getText() == nomeTopico) {
				this.getView().byId("inputTopicos").setValue("");
				sap.m.MessageToast.show("Tópico de interesse já incluído!");
				jaTem = true;
			}
		});
		
		if (jaTem) {
			dialog.close();
			return;
		}
		
		// ve se é um novo tópico de interesse pelo model de sugestões
		let jaCriado = false;
		this.getView().getModel("topicos").getData().forEach((item) => {
			if (item.nome == nomeTopico) {
				jaCriado = true;
			}
		});
		
		// se não encontrou é pq o usuário inseriu um novo. Cria no banco e atualiza o model
		if (!jaCriado) {
			let topicoInteresse = {
				nome: nomeTopico
			};
			
			const that = this;
			$.ajax({
				method: "POST",
				url: "/carbon/api/topicointeresse/novo",
				data: JSON.stringify(topicoInteresse),
				dataType: "json",
				contentType: 'application/json',
				success: (oResult, oResponse) => {
					sap.m.MessageToast.show("Tópico de interesse adicionado à base");
					that.getView().getModel("topicos").setData(oResult);
					that.getView().invalidate();
				}
			})
		}
		
		// cria e adiciona o token no painel, limpa o input de busca
		const oToken = new sap.m.Token({
			text: nomeTopico,
			delete: function() {
				this.destroy();
			}
		}).addStyleClass("campoBusca");
		oToken.placeAt(this.getView().byId("panelTopicos"));
		this.getView().byId("inputTopicos").setValue("");
		
		dialog.close();
	},
	
	onAddHabilidade(oEvent) {
		if (this.getView().byId("inputHabilidades").getValue() == "") {
			sap.m.MessageBox.error("Habilidade em branco!", {title: "Erro"});
			return;
		}
		
		const dialog = new sap.m.BusyDialog();
		dialog.open();
		
		// guarda o valor da busca
		const nomeHabilidade = this.getView().byId("inputHabilidades").getValue().toUpperCase();
		
		// ve se já tem no perfil do usuário, só adiciona se ainda não tem
		let jaTem = false;
		let aTokens = this.getView().byId("panelHabilidades").getContent();
		aTokens.forEach((item) => {
			if (item.getText() == nomeHabilidade) {
				this.getView().byId("inputHabilidades").setValue("");
				sap.m.MessageToast.show("Habilidade já incluída!");
				jaTem = true;
			}
		});
		
		if (jaTem) {
			dialog.close();
			return;
		}
		
		// ve se é uma nova habilidade pelo model de sugestões
		let jaCriado = false;
		this.getView().getModel("habilidades").getData().forEach((item) => {
			if (item.nome == nomeHabilidade) {
				jaCriado = true;
			}
		});
		
		// se não encontrou é pq o usuário inseriu uma nova. Cria no banco e atualiza o model
		if (!jaCriado) {
			let habilidade = {
				nome: nomeHabilidade
			};
			
			const that = this;
			$.ajax({
				method: "POST",
				url: "/carbon/api/habilidade/novo",
				data: JSON.stringify(habilidade),
				dataType: "json",
				contentType: 'application/json',
				success: (oResult, oResponse) => {
					sap.m.MessageToast.show("Habilidade adicionada à base");
					that.getView().getModel("habilidades").setData(oResult);
					that.getView().invalidate();
				}
			});
		}
		
		// cria e adiciona o token no painel, limpa o input de busca
		const oToken = new sap.m.Token({
			text: nomeHabilidade,
			delete: function() {
				this.destroy();
			}
		}).addStyleClass("campoBusca");
		oToken.placeAt(this.getView().byId("panelHabilidades"));
		this.getView().byId("inputHabilidades").setValue("");
		
		dialog.close();
	},
	
	onAddParticipante(oEvent) {
		const dialog = new sap.m.BusyDialog();
		dialog.open();
		
		// guarda o valor da busca
		const valorbusca = this.getView().byId("inputParticipantes").getValue();
		
		// ve se já tem no perfil do usuário, só adiciona se ainda não tem
		let jaTem = false;
		let aTokens = this.getView().byId("panelParticipantes").getContent();
		aTokens.forEach((item) => {
			if (item.getText() == valorbusca) {
				this.getView().byId("inputParticipantes").setValue("");
				sap.m.MessageToast.show("Usuário já incluído!");
				jaTem = true;
			}
		});
		
		if (jaTem) {
			dialog.close();
			return;
		}

		jQuery.sap.require("sap.m.MessageBox");
		
		// verifica se é um usuário válido
		const email = valorbusca.substring(valorbusca.indexOf("(") + 1, valorbusca.indexOf(")"));
		
		const userReq = jQuery.sap.syncGetJSON("/carbon/api/usuario/email" + email);
		
		if (userReq.statusCode != 200) {
			let mensagem = userReq.errorResponse.substring(userReq.errorResponse.indexOf('essage":"') + 9, userReq.errorResponse.indexOf('","path'))
			sap.m.MessageBox.error(mensagem, {title: "Erro"});

			dialog.close();
			return;
		}
		
		// cria e adiciona o token no painel, limpa o input de busca
		const oToken = new sap.m.Token({
			text: valorbusca,
			delete: function() {
				this.destroy();
			}
		}).addStyleClass("campoBusca");
		oToken.placeAt(this.getView().byId("panelParticipantes"));
		this.getView().byId("inputParticipantes").setValue("");
		
		dialog.close();
	},
	
	onSalvaProjeto(oEvento) {
		const bDialog = new sap.m.BusyDialog();
		bDialog.open();
		
		jQuery.sap.require("sap.m.MessageBox");
		
		//valida entradas
		
		const nome = this.getView().byId("nomeProjeto").getValue();
		const descricao = this.getView().byId("descricaoProjeto").getValue();

		if (nome == "" || descricao == "") {
			sap.m.MessageBox.error("Preencher informações do projeto!", {title: "Erro"});
			bDialog.close();
			return;
		}
		
		let oProjeto = {
				nome: nome,
				descricao: descricao,
				criador: {id: sap.ui.getCore().getModel("logado").getData().id},
				participantes: [],
				topicosInteresse: [],
				habilidades: []
		};
		
		if (this.verb == "PUT") {
			oProjeto.id = this.getView().getModel("projeto").getData().id;
			oProjeto.criador.id = this.getView().getModel("projeto").getData().criador.id;
			
		}
		
		this.getView().byId("panelTopicos").getContent().forEach((item) => {
			oProjeto.topicosInteresse.push({id: null, nome: item.getProperty("text")});
		});
		this.getView().byId("panelHabilidades").getContent().forEach((item) => {
			oProjeto.habilidades.push({id: null, nome: item.getProperty("text")});
		});
		this.getView().byId("panelParticipantes").getContent().forEach((item) => {
			oProjeto.participantes.push({id: null, email: item.getProperty("text")
				.substring(item.getProperty("text").indexOf("(") + 1, item.getProperty("text").indexOf(")"))});
		});
		
		const that = this;
		if (that.verb == "POST") {
			$.ajax({
				method: "POST",
				url: "/carbon/api/projeto/novo",
				data: JSON.stringify(oProjeto),
				dataType: "json",
				contentType: 'application/json',
				success: (oResult, oResponse) => {
					const oConversa = {
						projeto: {
							id: oResult.id
						},
						mensagens: []
					};
					
					$.ajax({
						method: "POST",
						url: "/carbon/api/conversa/novop",
						data: JSON.stringify(oConversa),
						dataType: "json",
						contentType: 'application/json',
						success: (oResult, oResponse) => {
							bDialog.close();
							that.getRouter().navTo("toHomepage");

							sap.m.MessageToast.show("Projeto criado!");
						}
					})
				}
			});
		} else {
			$.ajax({
				method: "PUT",
				url: "/carbon/api/projeto/update",
				data: JSON.stringify(oProjeto),
				dataType: "json",
				contentType: 'application/json',
				success: (oResult, oResponse) => {
					bDialog.close();
					that.getRouter().navTo("toHomepage");

					sap.m.MessageToast.show("Projeto atualizado!");
				}
			});
		}
	},
	
	onPostaComentario(oEvent) {
		if (oEvent.getParameter("value") == "") {
			sap.m.MessageBox.error("Preencher mensagem!", {title: "Erro"});
			return;
		}
		
		let mensagem = {
			conversa: {id: this.getView().getModel("projeto").getData().conversa.id},
			texto: oEvent.getParameter("value"),
			remetente: {id: sap.ui.getCore().getModel("logado").getData().id}
		}
		
		const that = this;
		
		$.ajax({
			method: "POST",
			url: "/carbon/api/mensagem/novo",
			data: JSON.stringify(mensagem),
			dataType: "json",
			contentType: 'application/json',
			success: (oResult, oResponse) => {
				let aMensagens = that.getView().getModel("mensagens").getData().reverse();
				aMensagens.push(oResult);
				that.getView().getModel("mensagens").setData(aMensagens.reverse());
				that.getView().invalidate();
			}
		});
	}
}));