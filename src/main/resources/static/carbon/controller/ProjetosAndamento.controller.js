sap.ui.define([
	'carbon/controller/BaseController',
], BaseController => BaseController.extend("carbon.controller.ProjetosAndamento", {

	onInit() {
		const oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		oRouter.getRoute("toProjetosAndamento").attachPatternMatched(this.show, this);
	},
	
	show() {
		// carrega os projetos no model
		const idLogado = sap.ui.getCore().getModel("logado").getData().id;
		const oModelProjetos = new sap.ui.model.json.JSONModel();
		oModelProjetos.setData(jQuery.sap.syncGetJSON(`api/projeto/u=${idLogado}`).data);
		this.getView().setModel(oModelProjetos, "projetos");
	},
	
	onBuscaProjeto(oEvent) {
		const valorFiltro = oEvent.getParameter("query");
	
		const oFilter = new sap.ui.model.Filter({
			filters: [
				new sap.ui.model.Filter("nome", sap.ui.model.FilterOperator.Contains, valorFiltro)
			],
			and: false
		});
	
		const oBinding = this.getView().byId("listaProjetos").getBinding("items");
		oBinding.filter(oFilter);
	},
	
	onSelecionaProjeto(oEvent) {
		this.getRouter().navTo("toShowProjeto", {
			projetoId: oEvent.getSource().getSelectedItem().getAttributes()[0].getText()
		});
	}
}));