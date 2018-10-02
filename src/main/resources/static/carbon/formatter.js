sap.ui.define([
	"carbon/controller/BaseController",
], (BaseController) => {
	"use strict";

	return {
		timestamps(timestamp) {
			if (timestamp !== null) {
				const d = new Date(timestamp);
				return d.toLocaleString();
			}
			return null;
		}
	};
});