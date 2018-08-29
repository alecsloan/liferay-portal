package com.liferay.amf.registration.portlet;

import com.liferay.amf.registration.constants.RegistrationPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import com.liferay.portal.kernel.servlet.SessionMessages;

import java.io.IOException;
import java.sql.*;
import java.util.*;

import javax.portlet.Portlet;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;

/**
 * @author liferay
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.tools",
		"com.liferay.portlet.instanceable=true",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + RegistrationPortletKeys.Registration,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class
)
public class RegistrationPortlet extends MVCPortlet {
	public Map<String,String> getStates() {
		Map<String,String> states = new HashMap<String,String>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lportal", "root", "root");

			Statement stmt = conn.createStatement();

			String sql = "select name, regionCode from region where countryId = 19";
        
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				states.put(rs.getString("name"), rs.getString("regionCode"));
		    }
        
		} catch (Exception e) {
			states.put("Failed", "Failed to get states.");
		}
		return states;
	}
}