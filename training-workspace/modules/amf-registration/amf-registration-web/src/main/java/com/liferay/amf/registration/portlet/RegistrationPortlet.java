package com.liferay.amf.registration.portlet;

import com.liferay.amf.registration.constants.RegistrationPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import java.io.IOException;
import java.sql.*;

import javax.portlet.Portlet;

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
	
	public String getStates() {
		try {
			Class.forName("com.mysql.jdbc.Driver");
		
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lportal", "root", "root");

			Statement stmt = conn.createStatement();

			String sql = "select name, regionCode from region where countryId = 19";
        
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				System.out.println(rs.getString("name")+": "+rs.getString("regionCode"));
		    }
			
			return "Got States";
        
		}catch (Exception e) {
			System.out.println(e);
			return "Failed to get States";
		}
		
	}
}