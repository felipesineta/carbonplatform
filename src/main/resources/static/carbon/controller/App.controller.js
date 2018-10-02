sap.ui.define([
	'carbon/controller/BaseController',
], BaseController => BaseController.extend("carbon.controller.App", {

	onInit() {
		jQuery.sap.log.setLevel(jQuery.sap.log.Level.INFO);

		const oRouter = this.getRouter();

		oRouter.attachBypassed((oEvent) => {
			const sHash = oEvent.getParameter("hash");
			// do something here, i.e. send logging data to the backend for analysis
			// telling what resource the user tried to access...
			jQuery.sap.log.info(`Sorry, but the hash '${sHash}' is invalid.`, "The resource was not found.");
		});

		oRouter.attachRouteMatched((oEvent) => {
			const sRouteName = oEvent.getParameter("name");
			// do something, i.e. send usage statistics to backend
			// in order to improve our app and the user experience (Build-Measure-Learn cycle)
			jQuery.sap.log.info(`User accessed route ${sRouteName}, timestamp = ${new Date().getTime()}`);
		});
		
		window.location = `${window.location.origin}/carbon/home#/homepage`;
	}
}));
