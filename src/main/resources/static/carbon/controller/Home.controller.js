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
		}
	},

	onLogout() {
		jQuery.sap.require("sap.m.MessageBox");
		
		sap.m.MessageBox.confirm(
			"Você deseja mesmo sair?",
			{
				title: "Confirmação",
				onClose: null,
				actions: [sap.m.MessageBox.Action.YES, sap.m.MessageBox.Action.NO],
				onClose: function(oAction) {
					if (oAction == sap.m.MessageBox.Action.YES) {
						window.location = `${window.location.origin}/carbon/logout`;
					}
				}
			}
		)
	}
}));
