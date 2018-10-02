sap.ui.define([
	"sap/ui/core/mvc/Controller",
	"sap/ui/model/Filter",
], (Controller, Filter) => Controller.extend("gdavaliacao.controller.BaseController", {

	onInit() {
		setTimeout(this.preventBack(), 0);
	},

	preventBack() {
		window.history.forward();
	},

	getRouter() {
		return sap.ui.core.UIComponent.getRouterFor(this);
	},

	onNavBack() {
		this.getRouter().navTo("toHomepage", {}, true);
	}
}));
