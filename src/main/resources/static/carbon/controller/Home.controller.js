sap.ui.define([
	'carbon/controller/BaseController',
], BaseController => BaseController.extend("carbon.controller.Home", {

	onInit() {
		// carrega o model global do usuario logado
		const oModelUserLogado = new sap.ui.model.json.JSONModel();
		oModelUserLogado.setData(jQuery.sap.syncGetJSON(`api/usuario/logado`).data);
		sap.ui.getCore().setModel(oModelUserLogado, "logado");
	},
	
	onPerfil(oEvent) {
		this.getRouter().navTo("toPerfil");
	},
	
	onConversas(oEvent) {
		this.getRouter().navTo("toConversas");
	},
	
	onPressTile(oEvent) {
		switch (oEvent.getSource().getHeader()) {
		case "Criar novo projeto":
			this.getRouter().navTo("toNovoProjeto");
			break;
		case "Meus projetos":
			this.getRouter().navTo("toProjetosAndamento");
			break;
		case "Busca avançada":
			this.getRouter().navTo("toBusca");
			break;
		}
	},

	onLogout() {
		jQuery.sap.require("sap.m.MessageBox");
		
		sap.m.MessageBox.confirm(
			"Você deseja mesmo sair?",
			{
				title: "Confirmação",
				onClose: null,
				actions: ["Sim", "Não"],
				onClose: function(oAction) {
					if (oAction == "Sim") {
						window.location = `${window.location.origin}/carbon/logout`;
					}
				}
			}
		)
	}
}));
