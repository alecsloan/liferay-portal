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

package com.liferay.commerce.product.internal.upgrade.v3_4_1;

import com.liferay.commerce.product.model.CPDefinition;
import com.liferay.commerce.product.model.CProduct;
import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.dao.jdbc.AutoBatchPreparedStatementUtil;
import com.liferay.portal.kernel.service.ClassNameLocalService;
import com.liferay.portal.kernel.upgrade.UpgradeProcess;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author Alec Sloan
 */
public class CPDefinitionFriendlyURLEntryUpgradeProcess extends UpgradeProcess {

	public CPDefinitionFriendlyURLEntryUpgradeProcess(
		ClassNameLocalService classNameLocalService) {

		_classNameLocalService = classNameLocalService;
	}

	@Override
	public void doUpgrade() throws Exception {
		long cpDefinitionClassNameId = _classNameLocalService.getClassNameId(
			CPDefinition.class);

		long cProductClassNameId = _classNameLocalService.getClassNameId(
			CProduct.class);

		String updateFriendlyUrlEntry =
			"UPDATE FriendlyUrlEntry SET classNameId = ?, classPK = ? WHERE " +
				"classNameId = ? AND classPK = ?";

		String selectFriendlyUrlEntry = StringBundler.concat(
			"SELECT DISTINCT classNameId, classPK, CPDefinitionId FROM ",
			"FriendlyUrlEntry INNER JOIN CPDefinition ON ",
			"CPDefinition.CProductId = FriendlyUrlEntry.classPK WHERE ",
			"classNameId = ", cProductClassNameId);

		try (PreparedStatement preparedStatement =
				AutoBatchPreparedStatementUtil.concurrentAutoBatch(
					connection, updateFriendlyUrlEntry);
			Statement s = connection.createStatement();
			ResultSet resultSet = s.executeQuery(selectFriendlyUrlEntry)) {

			while (resultSet.next()) {
				long cpDefinitionId = resultSet.getLong("CPDefinitionId");
				long cProductId = resultSet.getLong("classPK");

				preparedStatement.setLong(1, cpDefinitionClassNameId);
				preparedStatement.setLong(2, cpDefinitionId);
				preparedStatement.setLong(3, cProductClassNameId);
				preparedStatement.setLong(4, cProductId);

				preparedStatement.addBatch();
			}

			preparedStatement.executeBatch();
		}

		runSQL(
			String.format(
				"UPDATE FriendlyURLEntryLocalization SET classNameId = %s " +
					"WHERE classNameId = %s",
				cpDefinitionClassNameId, cProductClassNameId));
	}

	private final ClassNameLocalService _classNameLocalService;

}