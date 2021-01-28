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

<%@ include file="/price/init.jsp" %>

<%
String additionalDiscountClasses = (String)request.getAttribute("commerce-ui:price:additionalDiscountClasses");
String additionalPriceClasses = (String)request.getAttribute("commerce-ui:price:additionalPriceClasses");
String additionalPromoPriceClasses = (String)request.getAttribute("commerce-ui:price:additionalPromoPriceClasses");
PriceModel prices = (PriceModel)request.getAttribute("commerce-ui:price:prices");
boolean displayDiscountLevels = (boolean)request.getAttribute("commerce-ui:price:displayDiscountLevels");
boolean netPrice = (boolean)request.getAttribute("commerce-ui:price:netPrice");

String[] discountPercentages = new String[0];

if (prices != null) {
	discountPercentages = prices.getDiscountPercentages();
}
%>

<div class="price">

	<%
	String priceActiveClass = "";

	if (Validator.isNotNull(prices.getPromoPrice()) || (Validator.isNotNull(prices.getDiscountPercentage()) && Validator.isNotNull(prices.getFinalPrice()))) {
		priceActiveClass = "price-value-inactive";
	}
	%>

	<span class="price-label">
		<liferay-ui:message key="list-price" />
	</span>
	<span class="price-value <%= priceActiveClass %> <%= GetterUtil.getString(additionalPriceClasses) %>">
		<%= GetterUtil.getString(prices.getPrice()) %>
	</span>

	<%
	String promoPriceHideClass = "hide";
	String promoPriceActiveClass = "";

	if (Validator.isNotNull(prices.getPromoPrice())) {
		promoPriceHideClass = "";

		if (Validator.isNotNull(prices.getFinalPrice()) && Validator.isNotNull(prices.getDiscountPercentage())) {
			promoPriceActiveClass = "price-value-inactive";
		}
	}
	%>

	<span class="price-label <%= promoPriceHideClass %>">
		<liferay-ui:message key="sale-price" />
	</span>
	<span class="price-value price-value-promo <%= promoPriceActiveClass %> <%= promoPriceHideClass %> <%= GetterUtil.getString(additionalPromoPriceClasses) %>">
		<%= GetterUtil.getString(prices.getPromoPrice()) %>
	</span>

	<%
	String discountHideClass = "hide";

	if (Validator.isNotNull(prices.getFinalPrice()) && Validator.isNotNull(prices.getDiscountPercentage())) {
		discountHideClass = "";
	}
	%>

	<span class="price-label <%= discountHideClass %>">
		<liferay-ui:message key="discount" />
	</span>
	<span class="price-value price-value-discount <%= discountHideClass %>">
		<span>

			<%
			String discountPercentageHideClass = "";

			if ((discountPercentages != null) && ArrayUtil.isEmpty(discountPercentages)) {
				discountPercentageHideClass = "hide";
			}

			String discountLevel1HideClass = "hide";
			String discountLevel2HideClass = "hide";
			String discountLevel3HideClass = "hide";
			String discountLevel4HideClass = "hide";

			String discountPercentageLevel1 = "";
			String discountPercentageLevel2 = "";
			String discountPercentageLevel3 = "";
			String discountPercentageLevel4 = "";

			if (displayDiscountLevels && !ArrayUtil.isEmpty(discountPercentages)) {
				discountLevel1HideClass = "";

				discountPercentageLevel1 = discountPercentages[0];

				if (Validator.isNotNull(discountPercentages[1])) {
					discountLevel2HideClass = "";

					discountPercentageLevel2 = discountPercentages[1];
				}

				if (Validator.isNotNull(discountPercentages[2])) {
					discountLevel3HideClass = "";

					discountPercentageLevel3 = discountPercentages[2];
				}

				if (Validator.isNotNull(discountPercentages[3])) {
					discountLevel4HideClass = "";

					discountPercentageLevel4 = discountPercentages[3];
				}
			}
			%>

			<span class="discount-percentage-level1 price-value-discount <%= discountLevel1HideClass %>" data-text-cp-instance-discount-percentage-level-1>
				<%= discountPercentageLevel1 %>
			</span>
			<span class="discount-percentage-level2 price-value-discount <%= discountLevel2HideClass %>" data-text-cp-instance-discount-percentage-level-2>
				<%= discountPercentageLevel2 %>
			</span>
			<span class="discount-percentage-level3 price-value-discount <%= discountLevel3HideClass %>" data-text-cp-instance-discount-percentage-level-3>
				<%= discountPercentageLevel3 %>
			</span>
			<span class="discount-percentage-level4 price-value-discount <%= discountLevel4HideClass %>" data-text-cp-instance-discount-percentage-level-4>
				<%= discountPercentageLevel4 %>
			</span>
		</span>
		<span class="discount-percentage price-value price-value-discount <%= discountPercentageHideClass %>">
			-<%= prices.getDiscountPercentage() %>
		</span>
	</span>

	<%
	String netPriceHideClass = "hide";
	String grossPriceHideClass = "";

	if (netPrice) {
		netPriceHideClass = "";
		grossPriceHideClass = "hide";
	}
	%>

	<span class="price-label <%= discountHideClass %>">
		<span class="<%= netPriceHideClass %>">
			<liferay-ui:message key="net-price" />
		</span>
		<span class="<%= grossPriceHideClass %>">
			<liferay-ui:message key="gross-price" />
		</span>
	</span>
	<span class="price-value price-value-final <%= discountHideClass %> <%= GetterUtil.getString(additionalDiscountClasses) %>">
		<%= prices.getFinalPrice() %>
	</span>
</div>