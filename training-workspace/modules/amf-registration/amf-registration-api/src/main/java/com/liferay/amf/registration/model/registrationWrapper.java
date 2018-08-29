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

package com.liferay.amf.registration.model;

import aQute.bnd.annotation.ProviderType;

import com.liferay.expando.kernel.model.ExpandoBridge;

import com.liferay.exportimport.kernel.lar.StagedModelType;

import com.liferay.portal.kernel.model.ModelWrapper;
import com.liferay.portal.kernel.service.ServiceContext;

import java.io.Serializable;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * <p>
 * This class is a wrapper for {@link registration}.
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @see registration
 * @generated
 */
@ProviderType
public class registrationWrapper implements registration,
	ModelWrapper<registration> {
	public registrationWrapper(registration registration) {
		_registration = registration;
	}

	@Override
	public Class<?> getModelClass() {
		return registration.class;
	}

	@Override
	public String getModelClassName() {
		return registration.class.getName();
	}

	@Override
	public Map<String, Object> getModelAttributes() {
		Map<String, Object> attributes = new HashMap<String, Object>();

		attributes.put("uuid", getUuid());
		attributes.put("fooId", getFooId());
		attributes.put("groupId", getGroupId());
		attributes.put("companyId", getCompanyId());
		attributes.put("userId", getUserId());
		attributes.put("userName", getUserName());
		attributes.put("createDate", getCreateDate());
		attributes.put("modifiedDate", getModifiedDate());
		attributes.put("field1", getField1());
		attributes.put("field2", getField2());
		attributes.put("field3", getField3());
		attributes.put("field4", getField4());
		attributes.put("field5", getField5());

		return attributes;
	}

	@Override
	public void setModelAttributes(Map<String, Object> attributes) {
		String uuid = (String)attributes.get("uuid");

		if (uuid != null) {
			setUuid(uuid);
		}

		Long fooId = (Long)attributes.get("fooId");

		if (fooId != null) {
			setFooId(fooId);
		}

		Long groupId = (Long)attributes.get("groupId");

		if (groupId != null) {
			setGroupId(groupId);
		}

		Long companyId = (Long)attributes.get("companyId");

		if (companyId != null) {
			setCompanyId(companyId);
		}

		Long userId = (Long)attributes.get("userId");

		if (userId != null) {
			setUserId(userId);
		}

		String userName = (String)attributes.get("userName");

		if (userName != null) {
			setUserName(userName);
		}

		Date createDate = (Date)attributes.get("createDate");

		if (createDate != null) {
			setCreateDate(createDate);
		}

		Date modifiedDate = (Date)attributes.get("modifiedDate");

		if (modifiedDate != null) {
			setModifiedDate(modifiedDate);
		}

		String field1 = (String)attributes.get("field1");

		if (field1 != null) {
			setField1(field1);
		}

		Boolean field2 = (Boolean)attributes.get("field2");

		if (field2 != null) {
			setField2(field2);
		}

		Integer field3 = (Integer)attributes.get("field3");

		if (field3 != null) {
			setField3(field3);
		}

		Date field4 = (Date)attributes.get("field4");

		if (field4 != null) {
			setField4(field4);
		}

		String field5 = (String)attributes.get("field5");

		if (field5 != null) {
			setField5(field5);
		}
	}

	/**
	* Returns the field2 of this registration.
	*
	* @return the field2 of this registration
	*/
	@Override
	public boolean getField2() {
		return _registration.getField2();
	}

	@Override
	public boolean isCachedModel() {
		return _registration.isCachedModel();
	}

	@Override
	public boolean isEscapedModel() {
		return _registration.isEscapedModel();
	}

	/**
	* Returns <code>true</code> if this registration is field2.
	*
	* @return <code>true</code> if this registration is field2; <code>false</code> otherwise
	*/
	@Override
	public boolean isField2() {
		return _registration.isField2();
	}

	@Override
	public boolean isNew() {
		return _registration.isNew();
	}

	@Override
	public com.liferay.amf.registration.model.registration toEscapedModel() {
		return new registrationWrapper(_registration.toEscapedModel());
	}

	@Override
	public com.liferay.amf.registration.model.registration toUnescapedModel() {
		return new registrationWrapper(_registration.toUnescapedModel());
	}

	@Override
	public ExpandoBridge getExpandoBridge() {
		return _registration.getExpandoBridge();
	}

	@Override
	public com.liferay.portal.kernel.model.CacheModel<com.liferay.amf.registration.model.registration> toCacheModel() {
		return _registration.toCacheModel();
	}

	@Override
	public int compareTo(
		com.liferay.amf.registration.model.registration registration) {
		return _registration.compareTo(registration);
	}

	/**
	* Returns the field3 of this registration.
	*
	* @return the field3 of this registration
	*/
	@Override
	public int getField3() {
		return _registration.getField3();
	}

	@Override
	public int hashCode() {
		return _registration.hashCode();
	}

	@Override
	public Serializable getPrimaryKeyObj() {
		return _registration.getPrimaryKeyObj();
	}

	@Override
	public java.lang.Object clone() {
		return new registrationWrapper((registration)_registration.clone());
	}

	/**
	* Returns the field1 of this registration.
	*
	* @return the field1 of this registration
	*/
	@Override
	public java.lang.String getField1() {
		return _registration.getField1();
	}

	/**
	* Returns the field5 of this registration.
	*
	* @return the field5 of this registration
	*/
	@Override
	public java.lang.String getField5() {
		return _registration.getField5();
	}

	/**
	* Returns the user name of this registration.
	*
	* @return the user name of this registration
	*/
	@Override
	public java.lang.String getUserName() {
		return _registration.getUserName();
	}

	/**
	* Returns the user uuid of this registration.
	*
	* @return the user uuid of this registration
	*/
	@Override
	public java.lang.String getUserUuid() {
		return _registration.getUserUuid();
	}

	/**
	* Returns the uuid of this registration.
	*
	* @return the uuid of this registration
	*/
	@Override
	public java.lang.String getUuid() {
		return _registration.getUuid();
	}

	@Override
	public java.lang.String toString() {
		return _registration.toString();
	}

	@Override
	public java.lang.String toXmlString() {
		return _registration.toXmlString();
	}

	/**
	* Returns the create date of this registration.
	*
	* @return the create date of this registration
	*/
	@Override
	public Date getCreateDate() {
		return _registration.getCreateDate();
	}

	/**
	* Returns the field4 of this registration.
	*
	* @return the field4 of this registration
	*/
	@Override
	public Date getField4() {
		return _registration.getField4();
	}

	/**
	* Returns the modified date of this registration.
	*
	* @return the modified date of this registration
	*/
	@Override
	public Date getModifiedDate() {
		return _registration.getModifiedDate();
	}

	/**
	* Returns the company ID of this registration.
	*
	* @return the company ID of this registration
	*/
	@Override
	public long getCompanyId() {
		return _registration.getCompanyId();
	}

	/**
	* Returns the foo ID of this registration.
	*
	* @return the foo ID of this registration
	*/
	@Override
	public long getFooId() {
		return _registration.getFooId();
	}

	/**
	* Returns the group ID of this registration.
	*
	* @return the group ID of this registration
	*/
	@Override
	public long getGroupId() {
		return _registration.getGroupId();
	}

	/**
	* Returns the primary key of this registration.
	*
	* @return the primary key of this registration
	*/
	@Override
	public long getPrimaryKey() {
		return _registration.getPrimaryKey();
	}

	/**
	* Returns the user ID of this registration.
	*
	* @return the user ID of this registration
	*/
	@Override
	public long getUserId() {
		return _registration.getUserId();
	}

	@Override
	public void persist() {
		_registration.persist();
	}

	@Override
	public void setCachedModel(boolean cachedModel) {
		_registration.setCachedModel(cachedModel);
	}

	/**
	* Sets the company ID of this registration.
	*
	* @param companyId the company ID of this registration
	*/
	@Override
	public void setCompanyId(long companyId) {
		_registration.setCompanyId(companyId);
	}

	/**
	* Sets the create date of this registration.
	*
	* @param createDate the create date of this registration
	*/
	@Override
	public void setCreateDate(Date createDate) {
		_registration.setCreateDate(createDate);
	}

	@Override
	public void setExpandoBridgeAttributes(ExpandoBridge expandoBridge) {
		_registration.setExpandoBridgeAttributes(expandoBridge);
	}

	@Override
	public void setExpandoBridgeAttributes(
		com.liferay.portal.kernel.model.BaseModel<?> baseModel) {
		_registration.setExpandoBridgeAttributes(baseModel);
	}

	@Override
	public void setExpandoBridgeAttributes(ServiceContext serviceContext) {
		_registration.setExpandoBridgeAttributes(serviceContext);
	}

	/**
	* Sets the field1 of this registration.
	*
	* @param field1 the field1 of this registration
	*/
	@Override
	public void setField1(java.lang.String field1) {
		_registration.setField1(field1);
	}

	/**
	* Sets whether this registration is field2.
	*
	* @param field2 the field2 of this registration
	*/
	@Override
	public void setField2(boolean field2) {
		_registration.setField2(field2);
	}

	/**
	* Sets the field3 of this registration.
	*
	* @param field3 the field3 of this registration
	*/
	@Override
	public void setField3(int field3) {
		_registration.setField3(field3);
	}

	/**
	* Sets the field4 of this registration.
	*
	* @param field4 the field4 of this registration
	*/
	@Override
	public void setField4(Date field4) {
		_registration.setField4(field4);
	}

	/**
	* Sets the field5 of this registration.
	*
	* @param field5 the field5 of this registration
	*/
	@Override
	public void setField5(java.lang.String field5) {
		_registration.setField5(field5);
	}

	/**
	* Sets the foo ID of this registration.
	*
	* @param fooId the foo ID of this registration
	*/
	@Override
	public void setFooId(long fooId) {
		_registration.setFooId(fooId);
	}

	/**
	* Sets the group ID of this registration.
	*
	* @param groupId the group ID of this registration
	*/
	@Override
	public void setGroupId(long groupId) {
		_registration.setGroupId(groupId);
	}

	/**
	* Sets the modified date of this registration.
	*
	* @param modifiedDate the modified date of this registration
	*/
	@Override
	public void setModifiedDate(Date modifiedDate) {
		_registration.setModifiedDate(modifiedDate);
	}

	@Override
	public void setNew(boolean n) {
		_registration.setNew(n);
	}

	/**
	* Sets the primary key of this registration.
	*
	* @param primaryKey the primary key of this registration
	*/
	@Override
	public void setPrimaryKey(long primaryKey) {
		_registration.setPrimaryKey(primaryKey);
	}

	@Override
	public void setPrimaryKeyObj(Serializable primaryKeyObj) {
		_registration.setPrimaryKeyObj(primaryKeyObj);
	}

	/**
	* Sets the user ID of this registration.
	*
	* @param userId the user ID of this registration
	*/
	@Override
	public void setUserId(long userId) {
		_registration.setUserId(userId);
	}

	/**
	* Sets the user name of this registration.
	*
	* @param userName the user name of this registration
	*/
	@Override
	public void setUserName(java.lang.String userName) {
		_registration.setUserName(userName);
	}

	/**
	* Sets the user uuid of this registration.
	*
	* @param userUuid the user uuid of this registration
	*/
	@Override
	public void setUserUuid(java.lang.String userUuid) {
		_registration.setUserUuid(userUuid);
	}

	/**
	* Sets the uuid of this registration.
	*
	* @param uuid the uuid of this registration
	*/
	@Override
	public void setUuid(java.lang.String uuid) {
		_registration.setUuid(uuid);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (!(obj instanceof registrationWrapper)) {
			return false;
		}

		registrationWrapper registrationWrapper = (registrationWrapper)obj;

		if (Objects.equals(_registration, registrationWrapper._registration)) {
			return true;
		}

		return false;
	}

	@Override
	public StagedModelType getStagedModelType() {
		return _registration.getStagedModelType();
	}

	@Override
	public registration getWrappedModel() {
		return _registration;
	}

	@Override
	public boolean isEntityCacheEnabled() {
		return _registration.isEntityCacheEnabled();
	}

	@Override
	public boolean isFinderCacheEnabled() {
		return _registration.isFinderCacheEnabled();
	}

	@Override
	public void resetOriginalValues() {
		_registration.resetOriginalValues();
	}

	private final registration _registration;
}