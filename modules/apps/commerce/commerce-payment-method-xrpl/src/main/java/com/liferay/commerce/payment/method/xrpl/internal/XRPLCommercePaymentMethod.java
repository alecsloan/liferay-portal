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

package com.liferay.commerce.payment.method.xrpl.internal;

import com.liferay.commerce.constants.CommerceOrderConstants;
import com.liferay.commerce.constants.CommerceOrderPaymentConstants;
import com.liferay.commerce.constants.CommercePaymentConstants;
import com.liferay.commerce.model.CommerceOrder;
import com.liferay.commerce.payment.method.CommercePaymentMethod;
import com.liferay.commerce.payment.method.xrpl.internal.configuration.XRPLGroupServiceConfiguration;
import com.liferay.commerce.payment.method.xrpl.internal.constants.XRPLPaymentMethodConstants;
import com.liferay.commerce.payment.request.CommercePaymentRequest;
import com.liferay.commerce.payment.result.CommercePaymentResult;
import com.liferay.commerce.service.CommerceOrderLocalService;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.settings.GroupServiceSettingsLocator;
import com.liferay.portal.kernel.util.LocaleUtil;
import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**se
 * @author Alec Sloan
 */
@Component(
	immediate = true,
	property = "commerce.payment.engine.method.key=" + XRPLCommercePaymentMethod.KEY,
	service = CommercePaymentMethod.class
)
public class XRPLCommercePaymentMethod implements CommercePaymentMethod {

	public static final String KEY = "xrpl";

	@Override
	public CommercePaymentResult cancelPayment(
			CommercePaymentRequest commercePaymentRequest) throws Exception {

		return null;
	}

	@Override
	public CommercePaymentResult completePayment(
			CommercePaymentRequest commercePaymentRequest) throws Exception {

		return null;
	}

	@Override
	public String getDescription(Locale locale) {
		return _getResource(locale, "xrpl-description");
	}

	@Override
	public String getKey() {
		return KEY;
	}

	@Override
	public String getName(Locale locale) {
		return _getResource(locale, KEY);
	}

	@Override
	public int getPaymentType() {
		return CommercePaymentConstants.
			COMMERCE_PAYMENT_METHOD_TYPE_ONLINE_REDIRECT;
	}

	@Override
	public String getServletPath() {
		return XRPLPaymentMethodConstants.SERVLET_PATH;
	}

	@Override
	public boolean isCancelEnabled() {
		return true;
	}

	@Override
	public boolean isCompleteEnabled() {
		return true;
	}

	@Override
	public boolean isPartialRefundEnabled() {
		return true;
	}

	@Override
	public boolean isProcessPaymentEnabled() {
		return true;
	}

	@Override
	public boolean isRefundEnabled() {
		return true;
	}

	@Override
	public boolean isVoidEnabled() {
		return true;
	}

	@Override
	public CommercePaymentResult partiallyRefundPayment(
			CommercePaymentRequest commercePaymentRequest)
		throws Exception {

		boolean success = false;

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(
				commercePaymentRequest.getCommerceOrderId());

		List<String> messages = Arrays.asList();

		return new CommercePaymentResult(
			null, commercePaymentRequest.getCommerceOrderId(),
			CommerceOrderConstants.ORDER_STATUS_PARTIALLY_REFUNDED, false, null,
			null, messages, success);
	}

	@Override
	public CommercePaymentResult processPayment(
			CommercePaymentRequest commercePaymentRequest)
		throws Exception {

		boolean success = false;
		int status = CommerceOrderPaymentConstants.STATUS_FAILED;

		try {

			CommerceOrder commerceOrder =
				_commerceOrderLocalService.getCommerceOrder(
					commercePaymentRequest.getCommerceOrderId());

			List<String> messages = Arrays.asList();

			return new CommercePaymentResult(
				null, commercePaymentRequest.getCommerceOrderId(),
				status, true, null, null, messages, success);
		}
		catch (Exception exception) {
			_log.error(exception.getMessage(), exception);

			return new CommercePaymentResult(
				null, commercePaymentRequest.getCommerceOrderId(), status, true,
				null, null, null, false);
		}
	}

	@Override
	public CommercePaymentResult refundPayment(
			CommercePaymentRequest commercePaymentRequest)
		throws Exception {

		boolean success = false;

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(
				commercePaymentRequest.getCommerceOrderId());

		List<String> messages = Arrays.asList();

		return new CommercePaymentResult(
			null, commercePaymentRequest.getCommerceOrderId(),
			CommerceOrderConstants.ORDER_STATUS_REFUNDED, false, null, null,
			messages, success);
	}

	@Override
	public CommercePaymentResult voidTransaction(
			CommercePaymentRequest commercePaymentRequest)
		throws Exception {

		boolean success = false;

		CommerceOrder commerceOrder =
			_commerceOrderLocalService.getCommerceOrder(
				commercePaymentRequest.getCommerceOrderId());

		List<String> messages = Arrays.asList();

		return new CommercePaymentResult(
			null, commercePaymentRequest.getCommerceOrderId(),
			CommerceOrderConstants.PAYMENT_STATUS_PENDING, false, null, null,
			messages, success);
	}

	private XRPLGroupServiceConfiguration _getXRPLGroupServiceConfiguration(
			long groupId)
		throws PortalException {

		return _configurationProvider.getConfiguration(
			XRPLGroupServiceConfiguration.class,
			new GroupServiceSettingsLocator(
				groupId, XRPLPaymentMethodConstants.SERVICE_NAME));
	}

	private String _getResource(Locale locale, String key) {
		if (locale == null) {
			locale = LocaleUtil.getSiteDefault();
		}

		ResourceBundle resourceBundle = _getResourceBundle(locale);

		return LanguageUtil.get(resourceBundle, key);
	}

	private ResourceBundle _getResourceBundle(Locale locale) {
		return ResourceBundleUtil.getBundle(
			"content.Language", locale, getClass());
	}

	private static final Log _log = LogFactoryUtil.getLog(
		XRPLCommercePaymentMethod.class);

	@Reference
	private CommerceOrderLocalService _commerceOrderLocalService;

	@Reference
	private ConfigurationProvider _configurationProvider;

}