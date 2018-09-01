package com.liferay.amf.registration.portlet;

import com.liferay.amf.registration.constants.RegistrationPortletKeys;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.PhoneNumberException;
import com.liferay.portal.kernel.model.Address;
import com.liferay.portal.kernel.model.Contact;
import com.liferay.portal.kernel.model.Phone;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.service.AddressLocalServiceUtil;
import com.liferay.portal.kernel.service.PhoneLocalServiceUtil;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.servlet.SessionMessages;
import com.liferay.portal.kernel.util.PortalUtil;
import com.mysql.jdbc.StringUtils;

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
		"javax.portlet.display-name=amf-registration",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
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
		
			Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lportal?verifyServerCertificate=false&useSSL=true", "root", "root");

			Statement stmt = conn.createStatement();

			String sql = "select name, regionId from region where countryId = 19";
        
			ResultSet rs = stmt.executeQuery(sql);
			
			while (rs.next()) {
				states.put(rs.getString("name"), rs.getString("regionId"));
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
			home_phone = "",
			mobile_phone = "",
			address = "",
			address2 = "",
			city = "",
			state = "",
			security_question = "",
			security_answer = "",
			zip = "";
		
		Boolean male = false,
			accepted_tou = false;
		
		Integer b_month = -1,
			b_day = -1,
			b_year = -1;
		
		private void setFields(ActionRequest req){
			first_name = fieldValid("first_name", req.getParameter("first_name")) ? req.getParameter("first_name") : "";
			last_name = fieldValid("last_name", req.getParameter("last_name")) ? req.getParameter("last_name") : "";
			email_address = fieldValid("email_address", req.getParameter("email_address")) ? req.getParameter("email_address") : "";
			username = fieldValid("username", req.getParameter("username")) ? req.getParameter("username") : "";
			male = fieldValid("male", req.getParameter("male")) ? Boolean.parseBoolean(req.getParameter("male")) : false;
			b_month = fieldValid("b_month", req.getParameter("b_month")) ? Integer.parseInt(req.getParameter("b_month")) : -1;
			b_day = fieldValid("b_day", req.getParameter("b_day")) ? Integer.parseInt(req.getParameter("b_day")) : -1;
			b_year = fieldValid("b_year", req.getParameter("b_year")) ? Integer.parseInt(req.getParameter("b_year")) : -1;
			address = fieldValid("address", req.getParameter("address")) ? req.getParameter("address") : "";
			city = fieldValid("city", req.getParameter("city")) ? req.getParameter("city") : "";
			state = fieldValid("state", req.getParameter("state")) ? req.getParameter("state") : "";
			zip = fieldValid("zip", req.getParameter("zip")) ? req.getParameter("zip") : "";
			security_question = fieldValid("security_question", req.getParameter("security_question")) ? req.getParameter("security_question") : "";
			security_answer = fieldValid("security_answer", req.getParameter("security_answer")) ? req.getParameter("security_answer") : "";
			accepted_tou = fieldValid("accepted_tou", req.getParameter("accepted_tou")) ? true : false;

			//Parse non-required fields only if they have values
			if (req.getParameter("home_phone").matches("[0-9]+")) {
				home_phone = fieldValid("home_phone", req.getParameter("home_phone")) ? req.getParameter("home_phone") : "";
			}
			if (req.getParameter("mobile_phone").matches("[0-9]+"))
				mobile_phone = fieldValid("mobile_phone", req.getParameter("mobile_phone")) ? req.getParameter("mobile_phone") : "";
			if (req.getParameter("address2").matches("[a-zA-Z0-9 ]+"))
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
					if (value.matches("[a-zA-Z0-9 ]+") && value.length() < 256)
						return true;
					else
						errors.put(field, "Must be alphanumeric and have no more than 255 characters.");
					break;
				case "username":
					if (value.matches("[a-zA-Z0-9]+") && value.length() > 3 && value.length() < 17 && uniqueUsername(value, email_address))
						return true;
					else if (!uniqueUsername(value, email_address))
						errors.put(field, "This username or email address is already taken, Please select a different one.");
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
							if(StringUtils.isStrictlyNumeric(value))
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
		
		private Boolean uniqueUsername (String username, String email_address) {
			try {
				Class.forName("com.mysql.jdbc.Driver");
			
				Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/lportal?verifyServerCertificate=false&useSSL=true", "root", "root");

				Statement stmt = conn.createStatement();

				String sql = "select count(*) as userExists from user_ where screenName = '" + username + "' or emailAddress = '" + email_address + "'";
	    
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
	    
		UserForm user = new UserForm();
	    
	    System.out.println("Registering...");
	    
	    //Set the user's form data after validating it
	    user.setFields(actionRequest);
	    
	    //Check that the user is 13
	    if (!user.isThirteen()) {
	    	user.errors.put("birthday", "You must be 13 to register for an account.");
	    }
	    
	    //If there are no errors after validation we can create the user in our system
	    if (user.errors.size() == 0) {
	    	//Try to Insert user to user table
	    	Boolean userCreated = insertUser(user, PortalUtil.getCompanyId(actionRequest), PortalUtil.getLocale(actionRequest));
	    	
	    	actionRequest.setAttribute("created", userCreated);
	    } else {
	    
	    	System.out.println("Errors: " + user.errors.toString());
	    
	    	//Set the errors to be output to the form
	    	for (Map.Entry<String, String> entry : user.errors.entrySet()) {
	    		actionRequest.setAttribute(entry.getKey(),entry.getValue());
	    	}

	    	SessionMessages.add(actionRequest, "Form not submitted");
	    }
	    actionResponse.setRenderParameter("jspPage", "/view.jsp");
	}
	
	public Boolean insertUser(UserForm user, long companyId, Locale locale) {
		ServiceContext sc = new ServiceContext();
		
		try {
			User createdUser = UserLocalServiceUtil.addUser(0, companyId, false,
					user.password1, user.password1, false,
                    user.username, user.email_address, 0,
                    null, locale,
                    user.first_name, null,
                    user.last_name, 0, 0, user.male, user.b_month, user.b_day, user.b_year,
                    null, null, null, null, null, false, null);
			
			createdUser.setAgreedToTermsOfUse(user.accepted_tou);
			createdUser.setReminderQueryQuestion(user.security_question);
			createdUser.setReminderQueryAnswer(user.security_answer);
			
			UserLocalServiceUtil.updateUser(createdUser);
			
			System.out.println("User created...");
	
			//Set Address info
			
			try {
				Address createdAddress = AddressLocalServiceUtil.addAddress(createdUser.getUserId(), Address.class.getName(), PortalUtil.getClassNameId(Address.class.getName()),
						user.address, user.address2, null, user.city, user.zip, Long.parseLong(user.state),
						19L, 11002, true, true, sc);
				System.out.println("Address created...");
			} catch (PortalException e) {
				System.out.println("Failed to create Address: " + e);
			}
			
			//Only try to insert home phone if they gave it to us
			if (user.home_phone != "") {
				try {
					Phone createdHomePhone = PhoneLocalServiceUtil.addPhone(createdUser.getUserId(), Contact.class.getName(),
							createdUser.getContactId(), user.home_phone, null, 11011, true, sc);
			
					System.out.println("Home Phone Created...");
				} catch (PhoneNumberException e) {
					System.out.println("Failed to create Home phone: " + e);
				}
			}
			
			//Only try to insert mobile phone if they gave it to us
			if (user.mobile_phone != "") {
				try {
					Phone createdMobilePhone = PhoneLocalServiceUtil.addPhone(createdUser.getUserId(), Contact.class.getName(),
							createdUser.getContactId(), user.mobile_phone, null, 11011, false, sc);
			
					System.out.println("Mobile Phone Created...");
				} catch (PortalException e) {
					e.printStackTrace();
					System.out.println("Failed to create Home phone: " + e);
				}
			}
						
			
			System.out.println("Finished Registration...");
			return true;
		} catch (PortalException e) {
			System.out.println("Failed to create user: " + e);
		}
		return false;
	}
	
}