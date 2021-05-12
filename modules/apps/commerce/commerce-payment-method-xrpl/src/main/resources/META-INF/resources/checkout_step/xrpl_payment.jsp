<%--
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
%>

<liferay-util:html-top
	outputKey="js_qrcode"
>
	<script src="https://cdn.jsdelivr.net/gh/davidshimjs/qrcodejs/qrcode.min.js" type="text/javascript"></script>
</liferay-util:html-top>

<div class="text-center">
	<div class="row">
		<div class="col-sm-6">
			<liferay-ui:message arguments="<%= xrplDisplayContext.getOrderTotal(locale) %>" key="order-total-x" />
		</div>
		<div class="col-sm-6">
			<%= "1 XRP = " + xrplDisplayContext.getCommerceCurrencySymbol() + " " + xrplDisplayContext.getXRPPriceQuote() %>
		</div>
	</div>

	<aui:alert closeable="<%= false %>">
		<liferay-ui:message arguments="<%= new Object[] {xrplDisplayContext.getXRPPriceEquivalent(), xrplDisplayContext.getCommerceOrderId()} %>" key="send-x-xrp-to-the-following-address-with-a-destination-tag-of-x" />
	</aui:alert>

	<style>
		#qrcode > img {
			margin: auto
		}
	</style>

	<div id="qrcode"></div>
	<liferay-ui:message arguments="<%= xrplDisplayContext.getXRPLGroupServiceConfiguration().xrpAddress() %>" key="address-x" /><br />
	<liferay-ui:message arguments="<%= xrplDisplayContext.getCommerceOrderId() %>" key="destination-tag-x" />
</div>

<aui:script use="aui-base">
	new QRCode("qrcode", "<%= xrplDisplayContext.getXRPLGroupServiceConfiguration().xrpAddress()%>");

	var continueButton = A.one('#<portlet:namespace />continue');

	if (continueButton) {
		Liferay.Util.toggleDisabled(continueButton, true);
	}
</aui:script>
