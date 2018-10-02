sap.ui.define([
	'carbon/controller/BaseController',
], BaseController => BaseController.extend("carbon.controller.Projeto", {

	onInit() {
		const oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		oRouter.getRoute("toNovoProjeto").attachPatternMatched(this.create, this);
	},
	
	create() {
		jQuery.sap.require("sap.m.MessageBox");
		
		this.getView().byId("abaComentarios").setVisible(false);

		this.getView().byId("nomeProjeto").setValue("");
		this.getView().byId("descricaoProjeto").setValue("");
		this.getView().byId("inputTopicos").setValue("");
		this.getView().byId("panelTopicos").destroyContent();
		this.getView().byId("inputHabilidades").setValue("");
		this.getView().byId("panelHabilidades").destroyContent();
		this.getView().byId("inputParticipantes").setValue("");
		this.getView().byId("panelParticipantes").destroyContent();
		this.getView().byId("inputDataEncerramento").setValue("");
		
		this.carregaModelosBase();
	},
	
	show() {
		
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
			sap.m.MessageBox.error("Preencher busca!", {title: "Erro"});
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
		$.ajax({
			method: "POST",
			url: "/carbon/api/projeto/novo",
			data: JSON.stringify(oProjeto),
			dataType: "json",
			contentType: 'application/json',
			success: (oResult, oResponse) => {
				sap.m.MessageToast.show("Projeto criado!");
				
				bDialog.close();
				that.getRouter().navTo("toHomepage");
			}
		});
	}
}));