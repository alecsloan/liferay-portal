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

package com.liferay.commerce.payment.method.xrpl.internal.constants;

import com.liferay.portal.kernel.util.HashMapBuilder;
import java.util.Map;


/**
 * @author Alec Sloan
 */
public class XRPLPaymentMethodConstants {

	public static final Map<String, String> MODES =
		HashMapBuilder.put(
			"MAINNET", "https://s2.ripple.com:51234"
		).put(
			"TESTNET", "https://s.altnet.rippletest.net:51234"
		).build();

	public static final Map<String, String> PRICE_DATASOURCES =
		HashMapBuilder.put(
			"Bitstamp", "https://data.ripple.com/v2/exchange_rates/XRP/USD+rvYAfWj5gh67oV6fW32ZzP3Aw4Eubs59B"
		).put(
			"Gatehub", "https://data.ripple.com/v2/exchange_rates/XRP/USD+rhub8VRN55s94qWKDv6jmDy1pUykJzF3wq"
		).put(
			"CoinMarketCap", "https://web-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol=xrp"
		).put(
			"CoinGecko", "https://api.coingecko.com/api/v3/simple/price?ids=ripple&vs_currencies=usd"
		).put(
			"Other", ""
		).build();

	public static final String SERVICE_NAME =
		"com.liferay.commerce.payment.method.xrpl";

	public static final String SERVLET_PATH = "xrpl-payment";

}