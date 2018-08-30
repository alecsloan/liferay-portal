package com.liferay.amf.registration.portlet;

import com.liferay.amf.registration.constants.RegistrationPortletKeys;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;

import com.liferay.portal.kernel.servlet.SessionMessages;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;

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
		
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lportal?autoReconnect=true&useSSL=false", "root", "root");

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

	private class UserForm {
		Map<String,String> errors = new HashMap<String,String>();
		
		String first_name = "",
			last_name = "",
			email_address = "",
			username = "",
			password1 = "",
			password2 = "",
			address = "",
			address2 = "",
			city = "",
			state = "",
			security_question = "",
			security_answer = "";
		
		Boolean male = false,
			accepted_tou = false,
			formValid = false;
		
		Integer home_phone = -1,
			mobile_phone = -1,
			zip = -1,
			b_month = -1,
			b_day = -1,
			b_year = -1;
		
		private void setFields(ActionRequest req){
			first_name = fieldValid("first_name", req.getParameter("first_name")) ? req.getParameter("first_name") : "";
			last_name = fieldValid("last_name", req.getParameter("last_name")) ? req.getParameter("last_name") : "";
			email_address = fieldValid("email_address", req.getParameter("email_address")) ? req.getParameter("email_address") : "";
			username = fieldValid("username", req.getParameter("username")) ? req.getParameter("username") : "";
			male = fieldValid("male", req.getParameter("male")) ? true : false;
			b_month = fieldValid("b_month", req.getParameter("b_month")) ? Integer.parseInt(req.getParameter("b_month")) : -1;
			b_day = fieldValid("b_day", req.getParameter("b_day")) ? Integer.parseInt(req.getParameter("b_day")) : -1;
			b_year = fieldValid("b_year", req.getParameter("b_year")) ? Integer.parseInt(req.getParameter("b_year")) : -1;
			address = fieldValid("address", req.getParameter("address")) ? req.getParameter("address") : "";
			city = fieldValid("city", req.getParameter("city")) ? req.getParameter("city") : "";
			state = fieldValid("state", req.getParameter("state")) ? req.getParameter("state") : "";
			zip = fieldValid("zip", req.getParameter("zip")) ? Integer.parseInt(req.getParameter("zip")) : -1;
			security_question = fieldValid("security_question", req.getParameter("security_question")) ? req.getParameter("security_question") : "";
			security_answer = fieldValid("security_answer", req.getParameter("security_answer")) ? req.getParameter("security_answer") : "";
			accepted_tou = fieldValid("accepted_tou", req.getParameter("accepted_tou")) ? true : false;

			//Parse non-required fields only if they have values
			if (req.getParameter("home_phone").contains("[0-9]"))
				home_phone = fieldValid("home_phone", req.getParameter("home_phone")) ? Integer.parseInt(req.getParameter("home_phone")) : -1;
			if (req.getParameter("mobile_phone").contains("[0-9]"))
				mobile_phone = fieldValid("mobile_phone", req.getParameter("mobile_phone")) ? Integer.parseInt(req.getParameter("mobile_phone")) : -1;
			if (req.getParameter("address2") != null)
				address2 = fieldValid("address2", req.getParameter("address2")) ? req.getParameter("address2") : "";
			
			
			password1 = fieldValid("password1", req.getParameter("password1")) ? req.getParameter("password1") : "";
			password2 = fieldValid("password2", req.getParameter("password2")) ? req.getParameter("password2") : "";
			//Validate that the Confirmed password is the same as the Password
			if (!req.getParameter("password2").equals(req.getParameter("password1"))) {
		    	errors.put("password2", "Passwords do not match.");
		    }
			
		}
		
		private Boolean fieldValid(String field, String value) {
			//Make sure the input has a value
			//We also don't need to pay attention to the not required fields (home_phone, mobile_phone, and address2)
			if ((value == null || value == "")) {
				errors.put(field, "Cannot be empty.");
				return false;
			}
		
			switch (field) {
				case "first_name":
				case "last_name":
					if (value.matches("[a-zA-Z0-9]+") && value.length() < 51)
						return true;
					else
						errors.put(field, "Must be alphanumeric and have no more than 50 characters.");
					break;
				case "email_address":
					if (value.matches("([a-zA-Z0-9._-]+[@a-zA-Z]+[.a-z]){4,}") && value.length() < 256)
						return true;
					else
						errors.put(field, "That is an invalid email or it is more than 255 characters.");
					break;
				case "address": 
				case "address2": 
				case "city": 
				case "security_answer":
					if (value.matches("[a-zA-Z0-9]+") && value.length() < 256)
						return true;
					else
						errors.put(field, "Must be alphanumeric and have no more than 255 characters.");
					break;
				case "username":
					if (value.matches("[a-zA-Z0-9]+") && value.length() > 3 && value.length() < 17 && uniqueUsername(value))
						return true;
					else if (!uniqueUsername(value))
						errors.put(field, "This username is already taken, Please select a different one.");
					else
						errors.put(field, "Must be alphanumeric, have 4 or more characters, have no more than 16 characters");
					break;
				case "b_month":
					try {
						if(Integer.parseInt(value) > -1 && Integer.parseInt(value) < 12)
							return true;
						else
							errors.put(field, "Month must be between 1 and 12.");
					}catch (Exception e) {
						errors.put(field, "Must be numeric.");
					}
					break;
				case "b_day":
					try {
						if(Integer.parseInt(value) > 0 && Integer.parseInt(value) < 32)
							return true;
						else
							errors.put(field, "Must be between 1 and 31");
					}catch (Exception e) {
						errors.put(field, "Must be numeric.");
					}
					break;
				case "b_year":
					try {
						if(Integer.parseInt(value) > -1)
							return true;
						else
							errors.put(field, "Must be greater than 0.");
					}catch (Exception e) {
						errors.put(field, "Must be numeric.");
					}
					break;
				case "password1":
					if (value.length() > 5 && !value.equals(value.toLowerCase()) && Pattern.compile("[0-9]").matcher(value).find() && !value.matches("[a-zA-Z0-9]*"))
						return true;
					else
						errors.put(field, "Must have 6 or more characters, must contain one uppercase, one number, and one special character.");
					break;
				case "home_phone":	
				case "mobile_phone":
					if (value.length() == 10) {
						try {
							if(Integer.parseInt(value) > 0)
								return true;
						}catch (Exception e) {
							errors.put(field, "Must be numeric.");
						}
					}
					else
						errors.put(field, "Must be 10 digits long");
					break;
				case "state":
					return true;
				case "zip":
					if (value.length() == 5) {
						try {
							if(Integer.parseInt(value) > 0)
								return true;
						}catch (Exception e) {
							errors.put(field, "Must be numeric.");
						}
					}
					else
						errors.put(field, "Must be 5 digits long");
					break;
				case "security_question":
					return true;
				case "accepted_tou":
					return true;
			}
			return false;
		}
		
		private Boolean isThirteen() {
			LocalDate birthday = LocalDate.of(b_year, b_month+1, b_day);
			LocalDate now = LocalDate.now();
			
			if (ChronoUnit.YEARS.between(birthday, now) > 12)
				return true;
			else
				return false;
		}
		
		private Boolean uniqueUsername (String username) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lportal?autoReconnect=true&useSSL=false", "root", "root");

				Statement stmt = conn.createStatement();

				String sql = "select count(*) as userExists from user_ where screenName = '" + username + "'";
	    
				ResultSet rs = stmt.executeQuery(sql);
		
				rs.next();
				if(Integer.parseInt(rs.getString("userExists")) > 0)
					return false;
				else
					return true;
				
			} catch (Exception e) {
				System.out.println(e);
			}
			return false;
		}
	}
	
	public void registerUser(ActionRequest actionRequest,ActionResponse actionResponse) throws IOException, PortletException {
	    System.out.println("Registering...");
	    
	    UserForm user = new UserForm();
	    
	    user.setFields(actionRequest);
	    
	    if (!user.isThirteen()) {
	    	user.errors.put("birthday", "You must be 13 to register for an account.");
	    }
	    
	    System.out.println("Errors: " + user.errors.toString());
	    
	    //Set the errors to be output to the form
	    for (Map.Entry<String, String> entry : user.errors.entrySet()) {
	    	actionRequest.setAttribute(entry.getKey(),entry.getValue());
	    }

	    actionResponse.setRenderParameter("jspPage", "/view.jsp");
	}
	
}