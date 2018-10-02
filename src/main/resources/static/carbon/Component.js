sap.ui.define([
	"sap/ui/core/UIComponent",
], UIComponent => UIComponent.extend("carbon.Component", {

	metadata: {
		manifest: "json"
	},

	init() {
		// call the base component's init function
		UIComponent.prototype.init.apply(this, arguments);

		// create the views based on the url/hash
		this.getRouter().initialize();
	}
}));
