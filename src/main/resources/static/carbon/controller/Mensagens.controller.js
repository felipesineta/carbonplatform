sap.ui.define([
	'carbon/controller/BaseController',
	'carbon/formatter',
], (BaseController, Formatter) => BaseController.extend("carbon.controller.Mensagens", {

	formatter: Formatter,

	onInit() {
		const oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		oRouter.getRoute("toMensagens").attachPatternMatched(this.show, this);
	},
	
	show(oEvent) {
		const indexUrl = oEvent.getParameter('arguments').conversaId;
		const conversa = jQuery.sap.syncGetJSON(`api/conversa/c=${indexUrl}`).data;
		
		// carrega os models da conversa
		const oModelConversa = new sap.ui.model.json.JSONModel();
		oModelConversa.setData(conversa);
		this.getView().setModel(oModelConversa, "conversa");
		
		const oModelMensagens = new sap.ui.model.json.JSONModel();
		oModelMensagens.setData(conversa.mensagens.reverse());
		this.getView().setModel(oModelMensagens, "mensagens");
	},
	
	onMandaMensagem(oEvent) {
		if (oEvent.getParameter("value") == "") {
			sap.m.MessageBox.error("Preencher mensagem!", {title: "Erro"});
			return;
		}
		
		let mensagem = {
			conversa: {id: this.getView().getModel("conversa").getData().id},
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