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

package com.liferay.commerce.payment.method.xrpl.internal.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.commerce.payment.method.xrpl.internal.constants.XRPLPaymentMethodConstants;
import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Alec Sloan
 */
@ExtendedObjectClassDefinition(
	category = "payment", scope = ExtendedObjectClassDefinition.Scope.GROUP
)
@Meta.OCD(
	id = "com.liferay.commerce.payment.method.xrpl.internal.configuration.XRPLGroupServiceConfiguration",
	localization = "content/Language",
	name = "payment-method-xrpl-group-service-configuration-name"
)
public interface XRPLGroupServiceConfiguration {

	@Meta.AD(deflt = "TESTNET", name = "mode", required = true)
	public String mode();

	@Meta.AD(deflt = "true", name = "create-new-wallet", required = true)
	public boolean createNewWallet();

	@Meta.AD(deflt = "Bitstamp", name = "price-quote-datasource", required = true)
	public String priceQuoteDatasource();

	@Meta.AD(name = "custom-price-quote-url")
	public String customPriceQuoteURL();

	@Meta.AD(name = "xrp-address")
	public String xrpAddress();

}