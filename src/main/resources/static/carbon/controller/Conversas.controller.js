sap.ui.define([
	'carbon/controller/BaseController',
], BaseController => BaseController.extend("carbon.controller.Conversas", {

	onInit() {
		const oRouter = sap.ui.core.UIComponent.getRouterFor(this);
		oRouter.getRoute("toConversas").attachPatternMatched(this.show, this);
		
		window.controller = this;
	},
	
	show(oEvent) {
		// pega o id do user logado
		const logadoId = sap.ui.getCore().getModel("logado").getData().id;
		
		// carrega o model das conversas
		const oModelConversas = new sap.ui.model.json.JSONModel();
		let aConversasRaw = jQuery.sap.syncGetJSON(`api/conversa/users/u=${logadoId}`).data;

		let aConversas = [];
		
		aConversasRaw[0].forEach((item) => {
			aConversas.push(item);
		});
		
		aConversasRaw[1].forEach((item) => {
			let temp = item.user1;
			item.user1 = item.user2;
			item.user2 = temp;

			aConversas.push(item);
		});
		
		oModelConversas.setData(aConversas);
		this.getView().setModel(oModelConversas, "conversas");
	},
	
	onNewConversa(oEvent) {
		const dialog = new sap.m.Dialog({
			width: "75%", height: "50%",
			content: [
				new sap.ui.layout.form.SimpleForm({
					editable: true, layout: "ResponsiveGridLayout",
					content: [
						new sap.m.Label({
							width: "100%",
							text: "Nome de usuário"
						}),
						new sap.m.Input({
							width: "100%", type: "Text", placeholder: "Buscar usuário..."//,
						}),
						new sap.m.Button({
							text: "Buscar e criar", tooltip: "Buscar usuário e criar conversa",
							press: this.onBuscaECriaConversa
						})
					]
				})
			]
		});
		
		const custHeader = new sap.m.Bar({
			contentMiddle: [
				new sap.m.Title({
					text: "Nova conversa"
				})
			],
			contentRight: [
				new sap.m.Button({
					icon: "sap-icon://decline", tooltip: "Fechar",
					press: function(oEvent) {
						oEvent.getSource().getParent().getParent().close();
						oEvent.getSource().getParent().getParent().destroy();
					}
				})
			]
		});
		
		dialog.setCustomHeader(custHeader);
		dialog.open();
	},
	
	onBuscaECriaConversa(oEvent) {
		const bDialog = new sap.m.BusyDialog();
		bDialog.open();

		const that = this;
		
		jQuery.sap.require("sap.m.MessageBox");
		
		const nomeUser = oEvent.getSource().getParent().getParent().getParent().getParent().getContent()[1].getValue();
		if (nomeUser == "") {
			sap.m.MessageBox.error("Preencher busca!", {title: "Erro"});
			bDialog.close();
			return;
		}
		
		let conversa = {
			user1: {
				id: sap.ui.getCore().getModel("logado").getData().id
			},
			user2: {},
			mensagens: []
		};
		
		$.ajax({
			method: "GET",
			url: "/carbon/api/usuario/username" + nomeUser,
			success: (oResult, oResponse) => {
				
				conversa.user2.id = oResult.id;
				
				$.ajax({
					method: "POST",
					url: "/carbon/api/conversa/novo",
					data: JSON.stringify(conversa),
					dataType: "json",
					contentType: 'application/json',
					success: (oResult, oResponse) => {
						bDialog.close();
						window.controller.getRouter().navTo("toMensagens", {
							conversaId: oResult.id.toString(),
						});
						sap.m.MessageToast.show("Conversa criada com sucesso!");
					},
					error: (oResult, oResponse) => {
						bDialog.close();
						sap.m.MessageBox.error(oResult.responseJSON.message, {title: "Erro"});
					}
				});
				
				
			},
			error: (oResult, oResponse) => {
				bDialog.close();
				sap.m.MessageBox.error(oResult.responseJSON.message, {title: "Erro"});
			}
		})
	},
	
	onBuscaConversa(oEvent) {
		const valorFiltro = oEvent.getParameter("query");

		const oFilter = new sap.ui.model.Filter({
			filters: [
				new sap.ui.model.Filter("user2/nome", sap.ui.model.FilterOperator.Contains, valorFiltro),
				new sap.ui.model.Filter("user2/sobrenome", sap.ui.model.FilterOperator.Contains, valorFiltro),
				new sap.ui.model.Filter("user2/username", sap.ui.model.FilterOperator.Contains, valorFiltro),
				new sap.ui.model.Filter("user2/email", sap.ui.model.FilterOperator.Contains, valorFiltro)
			],
			and: false
		});

		const oBinding = this.getView().byId("listaConversa").getBinding("items");
		oBinding.filter(oFilter);
	},
	
	onSelecionaConversa(oEvent) {
		this.getRouter().navTo("toMensagens", {
			conversaId: oEvent.getSource().getSelectedItem().getAttributes()[0].getText()
		});
	}
}));