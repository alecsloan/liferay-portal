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

package com.liferay.amf.registration.service;

import aQute.bnd.annotation.ProviderType;

import com.liferay.portal.kernel.service.ServiceWrapper;

/**
 * Provides a wrapper for {@link registrationService}.
 *
 * @author Brian Wing Shun Chan
 * @see registrationService
 * @generated
 */
@ProviderType
public class registrationServiceWrapper implements registrationService,
	ServiceWrapper<registrationService> {
	public registrationServiceWrapper(registrationService registrationService) {
		_registrationService = registrationService;
	}

	/**
	* Returns the OSGi service identifier.
	*
	* @return the OSGi service identifier
	*/
	@Override
	public java.lang.String getOSGiServiceIdentifier() {
		return _registrationService.getOSGiServiceIdentifier();
	}

	@Override
	public registrationService getWrappedService() {
		return _registrationService;
	}

	@Override
	public void setWrappedService(registrationService registrationService) {
		_registrationService = registrationService;
	}

	private registrationService _registrationService;
}