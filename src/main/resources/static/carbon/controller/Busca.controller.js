sap.ui.define([
	'carbon/controller/BaseController',
], BaseController => BaseController.extend("carbon.controller.Busca", {

	onInit() {
		const oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		oRouter.getRoute("toBusca").attachPatternMatched(this.show, this);
		
		window.controller = this;
	},
	
	show(oEvent) {
		// carrega os meus projetos no model
		const idLogado = sap.ui.getCore().getModel("logado").getData().id;
		const oModelMeusProjetos = new sap.ui.model.json.JSONModel();
		oModelMeusProjetos.setData(jQuery.sap.syncGetJSON(`api/projeto/c=${idLogado}`).data);
		this.getView().setModel(oModelMeusProjetos, "meusprojetos");
	},
	
	onRealizaBusca(oEvent) {
		jQuery.sap.require("sap.m.MessageBox");
		
		if (this.getView().byId("rbgroupFiltro").getSelectedIndex() == 0) {
			this.buscaProjetos(oEvent);
		} else if (this.getView().byId("cboxProjeto").getValue() != ""){
			this.buscaParticipantes(oEvent);
		} else {
			sap.m.MessageBox.error("Preencher busca!", {title: "Erro"});
			return;
		}
	},
	
	buscaProjetos(oEvent) {
		const dialog = new sap.m.BusyDialog();
		dialog.open();
		
		const idLogado = sap.ui.getCore().getModel("logado").getData().id;
		const oModelResultadosBusca = new sap.ui.model.json.JSONModel();
		let resultadosBusca = jQuery.sap.syncGetJSON(`api/projeto/busca/u=${idLogado}`).data;
		
		const usuario = sap.ui.getCore().getModel("logado").getData();
		
		resultadosBusca.forEach(resultado => {
			let qtdTopicos = 0;
			resultado.topicosInteresse.forEach((topicoR) => {
				usuario.topicosInteresse.forEach((topicoU) => {
					if (topicoR.id == topicoU.id) {
						qtdTopicos++;
					}
				});
			});
			const totalTopicos = resultado.topicosInteresse.length;
			
			let qtdHabilidades = 0;
			resultado.habilidades.forEach((habilidadeR) => {
				usuario.habilidades.forEach((habilidadeU) => {
					if (habilidadeR.id == habilidadeU.id) {
						qtdHabilidades++;
					}
				});
			});
			const totalHabilidades = resultado.habilidades.length;
			
			resultado.afinidade = (qtdTopicos + qtdHabilidades) / (totalTopicos + totalHabilidades) * 100;
			resultado.afinidade = resultado.afinidade.toFixed();
		});
		resultadosBusca.sort(function(a, b){return b.afinidade-a.afinidade});
		
		oModelResultadosBusca.setData(resultadosBusca);
		this.getView().setModel(oModelResultadosBusca, "resultadosbusca");
		
		dialog.close();
	},
	
	buscaParticipantes(oEvent) {
		const dialog = new sap.m.BusyDialog();
		dialog.open();
		
		const idProjeto = this.getView().byId("cboxProjeto").getSelectedKey();
		const oModelResultadosBusca = new sap.ui.model.json.JSONModel();
		let resultadosBusca = jQuery.sap.syncGetJSON(`api/usuario/busca/p=${idProjeto}`).data;
		
		const projeto = jQuery.sap.syncGetJSON(`api/projeto/p=${idProjeto}`).data;
		
		resultadosBusca.forEach(resultado => {
			let qtdTopicos = 0;
			resultado.topicosInteresse.forEach((topicoR) => {
				projeto.topicosInteresse.forEach((topicoP) => {
					if (topicoR.id == topicoP.id) {
						qtdTopicos++;
					}
				});
			});
			const totalTopicos = projeto.topicosInteresse.length;
			
			let qtdHabilidades = 0;
			resultado.habilidades.forEach((habilidadeR) => {
				projeto.habilidades.forEach((habilidadeP) => {
					if (habilidadeR.id == habilidadeP.id) {
						qtdHabilidades++;
					}
				});
			});
			const totalHabilidades = projeto.habilidades.length;
			
			resultado.afinidade = (qtdTopicos + qtdHabilidades) / (totalTopicos + totalHabilidades) * 100;
			resultado.afinidade = resultado.afinidade.toFixed();
		});
		resultadosBusca.sort(function(a, b){return b.afinidade-a.afinidade});
		
		oModelResultadosBusca.setData(resultadosBusca);
		this.getView().setModel(oModelResultadosBusca, "resultadosbusca");
		
		dialog.close();
	},
	
	onSelecionaReultado(oEvent) {
		if (this.getView().byId("rbgroupFiltro").getSelectedIndex() == 0) {
			this.getRouter().navTo("toShowProjetoPublic", {
				projetoId: oEvent.getSource().getSelectedItem().getAttributes()[0].getText()
			});
		} else {
			this.getRouter().navTo("toShowUsuario", {
				usuarioId: oEvent.getSource().getSelectedItem().getAttributes()[0].getText()
			});
		}
	}
}));