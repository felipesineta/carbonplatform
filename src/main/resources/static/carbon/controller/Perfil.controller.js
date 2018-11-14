sap.ui.define([
	'carbon/controller/BaseController',
], BaseController => BaseController.extend("carbon.controller.Perfil", {

	onInit() {
		const oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		oRouter.getRoute("toPerfil").attachPatternMatched(this.show, this);
		oRouter.getRoute("toShowUsuario").attachPatternMatched(this.showPublic, this);
	},
	
	show(oEvent) {
		jQuery.sap.require("sap.m.MessageBox");
		
		// carrega o model do usuario
		const oModelPerfil = new sap.ui.model.json.JSONModel();
		oModelPerfil.setData(jQuery.sap.syncGetJSON(`api/usuario/logado`).data);
		this.getView().setModel(oModelPerfil, "perfil");
		
		// carrega os topicos de interesse do usuario no painel
		this.getView().byId("panelTopicos").destroyContent();
		oModelPerfil.getData().topicosInteresse.forEach((item) => {
			const oToken = new sap.m.Token({
				key: item.id,
				text: item.nome,
				delete: function() {
					this.destroy();
				}
			}).addStyleClass("campoBusca");
			oToken.placeAt(this.getView().byId("panelTopicos"));
		});
		
		// carrega as habilidades do usuario no painel
		this.getView().byId("panelHabilidades").destroyContent();
		oModelPerfil.getData().habilidades.forEach((item) => {
			const oToken = new sap.m.Token({
				key: item.id,
				text: item.nome,
				delete: function() {
					this.destroy();
				}
			}).addStyleClass("campoBusca");
			oToken.placeAt(this.getView().byId("panelHabilidades"));
		});
		
		// carrega os topicos de interesse nas sugestoes
		const oModelTopicosInteresse = new sap.ui.model.json.JSONModel();
		oModelTopicosInteresse.setData(jQuery.sap.syncGetJSON(`api/topicointeresse/all`).data);
		this.getView().setModel(oModelTopicosInteresse, "topicos");

		// carrega as habilildades nas sugestoes
		const oModelHabilidades = new sap.ui.model.json.JSONModel();
		oModelHabilidades.setData(jQuery.sap.syncGetJSON(`api/habilidade/all`).data);
		this.getView().setModel(oModelHabilidades, "habilidades");
		
		// limpa os campos de input
		this.getView().byId("inputTopicos").setValue("");
		this.getView().byId("inputHabilidades").setValue("");

		this.getView().byId("btnAddT").setVisible(true);
		this.getView().byId("btnAddH").setVisible(true);

		this.getView().byId("btnAdd").setVisible(false);
		this.getView().byId("btnSav").setVisible(true);
	},
	
	showPublic(oEvent) {
		const usuarioId = oEvent.getParameter('arguments').usuarioId;
		const projetoId = oEvent.getParameter('arguments').projetoId;
		
		// carrega o model do usuario
		const oModelPerfil = new sap.ui.model.json.JSONModel();
		oModelPerfil.setData(jQuery.sap.syncGetJSON(`api/usuario/u=${usuarioId}`).data);
		this.getView().setModel(oModelPerfil, "perfil");
		
		// carrega os topicos de interesse do usuario no painel
		this.getView().byId("panelTopicos").destroyContent();
		oModelPerfil.getData().topicosInteresse.forEach((item) => {
			const oToken = new sap.m.Token({
				key: item.id,
				text: item.nome,
				editable: false
			}).addStyleClass("campoBusca");
			oToken.placeAt(this.getView().byId("panelTopicos"));
		});
		
		// carrega as habilidades do usuario no painel
		this.getView().byId("panelHabilidades").destroyContent();
		oModelPerfil.getData().habilidades.forEach((item) => {
			const oToken = new sap.m.Token({
				key: item.id,
				text: item.nome,
				editable: false
			}).addStyleClass("campoBusca");
			oToken.placeAt(this.getView().byId("panelHabilidades"));
		});
		
		// carrega o model do projeto
		const oModelProjeto = new sap.ui.model.json.JSONModel();
		oModelProjeto.setData(jQuery.sap.syncGetJSON(`api/projeto/p=${projetoId}`).data);
		this.getView().setModel(oModelProjeto, "projeto");

		// apaga os campos de input
		this.getView().byId("inputTopicos").setVisible(false);
		this.getView().byId("inputHabilidades").setVisible(false);

		this.getView().byId("btnAddT").setVisible(false);
		this.getView().byId("btnAddH").setVisible(false);

		this.getView().byId("btnAdd").setVisible(true);
		this.getView().byId("btnSav").setVisible(false);
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
		
		// ve se já tem no perfil do usuário, só adiciona se ainda não tem
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
	
	onSave(oEvent) {
		const dialog = new sap.m.BusyDialog();
		dialog.open();
		
		let usuario = this.getView().getModel("perfil").getData();
		usuario.topicosInteresse = [];
		usuario.habilidades = [];
		
		this.getView().byId("panelTopicos").getContent().forEach((item) => {
			usuario.topicosInteresse.push({id: null, nome: item.getProperty("text")});
		});
		this.getView().byId("panelHabilidades").getContent().forEach((item) => {
			usuario.habilidades.push({id: null, nome: item.getProperty("text")});
		});
		
		const that = this;
		$.ajax({
			method: "PUT",
			url: "/carbon/api/usuario/atualiza",
			data: JSON.stringify(usuario),
			dataType: "json",
			contentType: 'application/json',
			success: (oResult, oResponse) => {
				dialog.close();
				
				const modelUsuario = new sap.ui.model.json.JSONModel();
				modelUsuario.setData(oResult);
				sap.ui.getCore().setModel(modelUsuario, 'logado');
				
				that.getRouter().navTo("toHomepage");
				sap.m.MessageToast.show("Perfil atualizado com sucesso!");
			}
		});
	},
	
	onAddParticipante(oEvent) {
		const dialog = new sap.m.BusyDialog();
		dialog.open();
		
		const projeto = this.getView().getModel("projeto").getData();
		const usuario = this.getView().getModel("perfil").getData();
		
		let jaTem = false;
		
		projeto.participantes.forEach((participante) => {
			if (participante.id == usuario.id) {
				sap.m.MessageToast.show("Usuário já é participante!");
				jaTem = true;
			}
		});
		
		if (jaTem) {
			dialog.close();
			return;
		}

		projeto.participantes.push(usuario);
		
		let oProjeto = {
			id: projeto.id,
			nome: projeto.nome,
			descricao: projeto.descricao,
			criador: {id: projeto.criador.id},
			participantes: [],
			topicosInteresse: [],
			habilidades: []
		};
		
		projeto.topicosInteresse.forEach((item) => {
			oProjeto.topicosInteresse.push({
				id: null,
				nome: item.nome
			});
		});
		
		projeto.habilidades.forEach((item) => {
			oProjeto.habilidades.push({
				id: null,
				nome: item.nome
			});
		});
		
		projeto.participantes.forEach((item) => {
			oProjeto.participantes.push({
				id: null,
				email: item.email
			});
		});
		
		const that = this;
		
		$.ajax({
			method: "PUT",
			url: "/carbon/api/projeto/update",
			data: JSON.stringify(oProjeto),
			dataType: "json",
			contentType: 'application/json',
			success: (oResult, oResponse) => {
				dialog.close();
				that.getRouter().navTo("toBusca");

				sap.m.MessageToast.show("Participante adicionado!");
			}
		});
	}
}));