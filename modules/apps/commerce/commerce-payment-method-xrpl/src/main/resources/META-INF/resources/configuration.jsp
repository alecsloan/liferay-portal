<%@ page import="java.util.Map" %>
<%@ page import="com.liferay.portal.kernel.util.Validator" %>
<%@ page import="com.liferay.portal.kernel.util.HttpUtil" %>
<%@ page import="com.liferay.portal.kernel.json.JSONFactoryUtil" %>
<%@ page import="com.liferay.commerce.currency.util.CommercePriceFormatter" %>
<%@ page
	import="com.liferay.commerce.currency.model.CommerceMoneyFactoryUtil" %>
<%@ page import="com.liferay.commerce.currency.model.CommerceCurrency" %>
<%@ page
	import="com.liferay.commerce.currency.service.CommerceCurrencyServiceUtil" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.liferay.commerce.currency.model.CommerceMoney" %>
<%@ page import="com.liferay.portal.kernel.json.JSONObject" %><%--
/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
--%>

<%@ include file="/init.jsp" %>

<%
XRPLDisplayContext xrplDisplayContext = (XRPLDisplayContext) request.getAttribute("XRPL_DISPLAY_CONTEXT");

XRPLGroupServiceConfiguration xrplGroupServiceConfiguration = xrplDisplayContext.getXRPLGroupServiceConfiguration();

String datasource = xrplGroupServiceConfiguration.priceQuoteDatasource();

if (Validator.isNull(datasource)) {
	datasource = "Bitstamp";
}

CommerceCurrency commerceCurrency = CommerceCurrencyServiceUtil.getCommerceCurrency(themeDisplay.getCompanyId(), "USD");

CommerceMoney commerceMoney = CommerceMoneyFactoryUtil.create(commerceCurrency, new BigDecimal(xrplDisplayContext.getXRPPriceQuote()));
%>

<portlet:actionURL name="/commerce_payment_methods/edit_xrpl_payment_method_configuration" var="editXRPLPaymentMethodActionURL" />

<aui:form action="<%= editXRPLPaymentMethodActionURL %>" method="post" name="fm">
	<aui:input name="<%= Constants.CMD %>" type="hidden" value="<%= Constants.UPDATE %>" />
	<aui:input name="commerceChannelId" type="hidden" value='<%= ParamUtil.getLong(request, "commerceChannelId") %>' />
	<aui:input name="redirect" type="hidden" value="<%= currentURL %>" />

	<commerce-ui:panel>
		<commerce-ui:info-box
			title=""
		>
			<aui:select id="xrpl-settings--mode" name="settings--mode--">

				<%
					for (String mode : XRPLPaymentMethodConstants.MODES.keySet()) {
				%>

				<aui:option label="<%= mode %>" selected="<%= mode.equals(xrplGroupServiceConfiguration.mode()) %>" value="<%= mode %>" />

				<%
					}
				%>

			</aui:select>

			<aui:input disabled="true" name="priceQuote" value="<%= commerceMoney.format(locale) %>"/>

			<aui:select id="xrpl-settings--priceQuoteDatasource" name="settings--priceQuoteDatasource--">

				<%
					for (String datasourceKey : XRPLPaymentMethodConstants.PRICE_DATASOURCES.keySet()) {
				%>

				<aui:option data-url="<%= XRPLPaymentMethodConstants.PRICE_DATASOURCES.get(datasourceKey) %>" label="<%= datasourceKey %>" selected="<%= datasourceKey.equals(datasource) %>" value="<%= datasourceKey %>" />

				<%
					}
				%>

			</aui:select>

			<aui:input hidden="<%= !datasource.equals("Other") %>" labelCssClass="<%= !datasource.equals("Other") ? "hide" : "" %>"  name="settings--customPriceQuoteUrl--" value="<%= xrplGroupServiceConfiguration.customPriceQuoteURL() %>" />

			<aui:input checked="<%= xrplGroupServiceConfiguration.createNewWallet() %>" helpMessage="configures-whether-you-want-to-accept-payments-to-an-existing-wallet-or-create-a-new-wallet" label="wallet" labelOff="existing" labelOn="new" name="settings--createNewWallet--" type="toggle-switch" />

			<clay:label
				displayType="<%= xrplDisplayContext.isValidAddress() ? "success" : "danger" %>"
				label="<%= xrplDisplayContext.isValidAddress() ? "valid-address" : "invalid-address" %>"
			/>

			<aui:input disabled="<%= xrplGroupServiceConfiguration.createNewWallet() %>" labelCssClass="<%= xrplGroupServiceConfiguration.createNewWallet() ? "hide" : "" %>"  name="settings--xrpAddress--" value="<%= xrplGroupServiceConfiguration.xrpAddress() %>" />
		</commerce-ui:info-box>
	</commerce-ui:panel>

	<aui:button-row>
		<aui:button cssClass="btn-lg" type="submit" />

		<aui:button cssClass="btn-lg" href="<%= redirect %>" type="cancel" />
	</aui:button-row>
</aui:form>

<aui:script use="aui-base">
	A.one('#<portlet:namespace />xrpl-settings--priceQuoteDatasource').on('valueChange', (event) => {
		var selectField = event.target.getDOMNode();

		Liferay.Util.fetch(
			selectField[selectField.selectedIndex].dataset.url,
			{
				headers: new Headers({
					Accept: 'application/json',
					'Access-Control-Allow-Origin': '*',
					'Content-Type': 'application/json',
				})
			}
		).then((res) => {
			return res.json();
		}).then((payload) => {
			console.log(payload)
		});
	});
</aui:script>