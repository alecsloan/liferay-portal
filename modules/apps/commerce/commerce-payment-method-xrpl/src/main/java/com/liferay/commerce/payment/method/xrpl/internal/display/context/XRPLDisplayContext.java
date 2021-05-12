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

package com.liferay.commerce.payment.method.xrpl.internal.display.context;

import com.google.common.primitives.UnsignedInteger;
import com.google.common.primitives.UnsignedLong;
import com.liferay.commerce.constants.CommerceCheckoutWebKeys;
import com.liferay.commerce.currency.model.CommerceCurrency;
import com.liferay.commerce.currency.model.CommerceMoney;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.payment.method.xrpl.internal.configuration.XRPLGroupServiceConfiguration;
import com.liferay.commerce.payment.method.xrpl.internal.constants.XRPLPaymentMethodConstants;
import com.liferay.commerce.product.model.CommerceChannel;
import com.liferay.commerce.product.service.CommerceChannelLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.settings.ParameterMapSettingsLocator;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.portal.kernel.util.ParamUtil;

import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;
import okhttp3.HttpUrl;
import org.xrpl.xrpl4j.client.XrplClient;
import org.xrpl.xrpl4j.model.client.accounts.AccountInfoRequestParams;
import org.xrpl.xrpl4j.model.client.accounts.AccountTransactionsRequestParams;
import org.xrpl.xrpl4j.model.client.accounts.AccountInfoResult;
import org.xrpl.xrpl4j.model.client.common.LedgerIndex;
import org.xrpl.xrpl4j.model.client.accounts.AccountTransactionsResult;
import org.xrpl.xrpl4j.model.transactions.Address;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.Locale;
import java.util.Optional;

/**
 * @author Alec Sloan
 */
public class XRPLDisplayContext {

	public XRPLDisplayContext(
		CommerceChannelLocalService commerceChannelLocalService,
		ConfigurationProvider configurationProvider,
		HttpServletRequest httpServletRequest) {

		_commerceChannelLocalService = commerceChannelLocalService;
		_configurationProvider = configurationProvider;
		_httpServletRequest = httpServletRequest;

		try {
			getXRPLTransaction();
		}
		catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public String getCommerceCurrencySymbol() throws Exception {
		CommerceOrder commerceOrder = getCommerceOrder();
		
		CommerceCurrency commerceCurrency = commerceOrder.getCommerceCurrency();
		
		return commerceCurrency.getSymbol();
	}

	public long getCommerceOrderId() throws Exception {
		CommerceOrder commerceOrder = getCommerceOrder();

		return commerceOrder.getCommerceOrderId();
	}

	public String getOrderTotal(Locale locale) throws Exception {
		CommerceOrder commerceOrder = getCommerceOrder();

		CommerceMoney commerceMoney = commerceOrder.getTotalMoney();

		return commerceMoney.format(locale);
	}

	public BigDecimal getXRPPriceEquivalent() throws Exception {
		CommerceOrder commerceOrder = getCommerceOrder();

		BigDecimal total = commerceOrder.getTotal();

		BigDecimal xrpPriceQuote = new BigDecimal(getXRPPriceQuote());

		return total.divide(xrpPriceQuote, 4, RoundingMode.HALF_EVEN);
	}

	public String getXRPPriceQuote() throws Exception {
		XRPLGroupServiceConfiguration xrplGroupServiceConfiguration =
			getXRPLGroupServiceConfiguration();

		String datasource = xrplGroupServiceConfiguration.priceQuoteDatasource();

		if (Validator.isNull(datasource)) {
			datasource = "Bitstamp";
		}

		String priceQuoteURL = XRPLPaymentMethodConstants.PRICE_DATASOURCES.get(datasource);

		String priceQuote = "0";

		if (Validator.isNotNull(priceQuoteURL) &&
			(priceQuoteURL.startsWith("http://") ||
			 priceQuoteURL.startsWith("https://"))) {

			JSONObject responseObject = JSONFactoryUtil.createJSONObject(
				HttpUtil.URLtoString(priceQuoteURL));

			if (datasource.equals("CoinMarketCap")) {
				priceQuote = responseObject.getJSONObject("data").getJSONObject(
					"XRP").getJSONObject("quote").getJSONObject("USD").getString(
					"price");
			}
			else if (datasource.equals("CoinGecko")) {
				priceQuote = responseObject.getJSONObject("ripple").getString("usd");
			}
			else if (!datasource.equals("other")) {
				priceQuote = responseObject.getString("rate");
			}
		}

		return priceQuote;
	}

	public void getXRPLTransaction() throws Exception {
		XRPLGroupServiceConfiguration xrplGroupServiceConfiguration =
			getXRPLGroupServiceConfiguration();

		AccountTransactionsRequestParams params = AccountTransactionsRequestParams.builder()
			.account(Address.of(xrplGroupServiceConfiguration.xrpAddress()))
			.ledgerIndexMin(LedgerIndex.of("0"))
			.ledgerIndexMax(LedgerIndex.of(UnsignedLong.valueOf(64724064)))
			.ledgerIndex(LedgerIndex.CURRENT)
			.limit(UnsignedInteger.valueOf(10))
			.build();

		XrplClient xprlClient = getXrplClient();

		AccountTransactionsResult result =
			xprlClient.accountTransactions(params);

//		AccountTransactionsResult result = xprlClient.accountTransactions(
//			Address.of(xrplGroupServiceConfiguration.xrpAddress()));

		System.out.println(result.transactions().size());
	}

	public XRPLGroupServiceConfiguration getXRPLGroupServiceConfiguration()
		throws Exception {

		long commerceChannelId =
			ParamUtil.getLong(_httpServletRequest, "commerceChannelId");

		long commerceChannelGroupId;

		if (commerceChannelId > 0) {
			CommerceChannel commerceChannel =
				_commerceChannelLocalService.getCommerceChannel(
					commerceChannelId);

			commerceChannelGroupId = commerceChannel.getGroupId();
		}
		else {
			commerceChannelGroupId =
				_commerceChannelLocalService.
					getCommerceChannelGroupIdBySiteGroupId(
						PortalUtil.getScopeGroupId(_httpServletRequest));
		}

		return _configurationProvider.getConfiguration(
			XRPLGroupServiceConfiguration.class,
			new ParameterMapSettingsLocator(
				_httpServletRequest.getParameterMap(),
				new GroupServiceSettingsLocator(
					commerceChannelGroupId,
					XRPLPaymentMethodConstants.SERVICE_NAME)));
	}

	public boolean isValidAddress() throws Exception {
		XRPLGroupServiceConfiguration xrplGroupServiceConfiguration =
			getXRPLGroupServiceConfiguration();

		XrplClient xrplClient = getXrplClient();

		try {
			AccountInfoRequestParams requestParams =
				AccountInfoRequestParams.of(
					Address.of(xrplGroupServiceConfiguration.xrpAddress()));

			AccountInfoResult accountInfoResult =
				xrplClient.accountInfo(requestParams);

			Optional<String> status = accountInfoResult.status();

			return status.orElse("").equals("success");
		}
		catch (Exception e) {
			return false;
		}
	}

	protected CommerceOrder getCommerceOrder() throws PortalException {
		if (_commerceOrder != null) {
			return _commerceOrder;
		}

		_commerceOrder = (CommerceOrder)_httpServletRequest.getAttribute(
			CommerceCheckoutWebKeys.COMMERCE_ORDER);

		return _commerceOrder;
	}

	protected XrplClient getXrplClient() throws Exception {
		XRPLGroupServiceConfiguration xrplGroupServiceConfiguration =
			getXRPLGroupServiceConfiguration();

		String mode = xrplGroupServiceConfiguration.mode();

		String modeURL = XRPLPaymentMethodConstants.MODES.get(mode);

		HttpUrl rippledUrl = HttpUrl.get(new URI(modeURL));

		return new XrplClient(rippledUrl);
	}

	private CommerceOrder _commerceOrder;
	private final CommerceChannelLocalService _commerceChannelLocalService;
	private final ConfigurationProvider _configurationProvider;
	private final HttpServletRequest _httpServletRequest;

}