<%@ include file="/init.jsp" %>
<%@page import="com.liferay.amf.registration.portlet.RegistrationPortlet" %>
<%
	RegistrationPortlet reg = new RegistrationPortlet();

	System.out.println(reg.getStates());
%>
<aui:form action="" method="post" name="fm">
	<aui:fieldset label="Basic Info">
		<aui:input 
			label="First Name:"
			maxlength="50"
			name="first_name"
			required="true"
			title="First Name"
			type="text"
		/>
	
		<aui:input 
			label="Last Name:"
			maxlength="50"
			name="last_name" 
			required="true"
			title="Last Name" 
			type="text" 
		/>
	
		<aui:input
			label="Email Address:"
			maxlength="255"
			name="email_address"
			required="true"
			title="Email Address"
			type="email"
		/>
	
		<aui:input 
			label="Username:"
			maxlength="16"
			name="username"
			required="true"
			title="Username"
			type="text"
		/>
	
		<aui:input 
			label=" Male" 
			name="male" 
			title="Male" 
			type="checkbox"
		/>
		
		<label for="birthday">Birthday: </label>
		<liferay-ui:input-date
			dayParam="b_day"
			dayValue="1"
			name="birthday"
			monthParam="b_month"
			monthValue="0"
			yearParam="b_year"
			yearValue="1970"
		/>
	
		<aui:input 
			label="Password:"
			name="password1" 
			required="true"
			title="Password" 
			type="password"
		/>
	
		<aui:input 
			label="Confirm Password:" 
			name="password2" 
			required="true"
			title="Confirm Password" 
			type="password"
		/>
	</aui:fieldset>
	
	<hr />
	
	<aui:fieldset label="Phone">
		<aui:input 
			label="Home Phone:"
			maxlength="10"
			name="home_phone"
			prefix="+1"
			title="Home Phone" 
			type="text"
		/>
		<aui:input 
			label="Mobile Phone:" 
			maxlength="10"
			name="mobile_phone"
			prefix="+1"
			title="Mobile Phone" 
			type="text"
		/>
	</aui:fieldset>
	
	<hr />
	
	<aui:fieldset label="Billing Address">
		<aui:input 
			label="Address 1"
			maxlength="255"
			name="address"
			required="true"
			title="Address" 
			type="text"
		/>
		<aui:input 
			label="Address 2"
			maxlength="255"
			name="address"
			title="Address" 
			type="text"
		/>
		<aui:input 
			label="City"
			maxlength="255"
			name="city"
			required="true"
			title="City" 
			type="text"
		/>
		<aui:input 
			label="State"
			maxlength="2"
			name="state"
			required="true"
			title="state" 
			type="text"
		/>
		<aui:input 
			label="Zip Code"
			maxlength="5"
			name="zip"
			required="true"
			title="zip" 
			type="text"
		/>
	</aui:fieldset>
	
	<hr />
	
	<aui:fieldset label="Misc.">
		<aui:select label="Security Question:" name="security_question" required="true" title="Security Question">
			<aui:option label="Must choose one of the following" selected="true"/>
			<aui:option label="What is your mother's maiden name?" value="what-is-your-mother's-maiden-name"/>
			<aui:option label="What is the make of your first car?" value="what-is-the-make-of-your-first-car"/>
			<aui:option label="What is your high school mascot?" value="what-is-your-high-school-mascot"/>
			<aui:option label="Who is your favorite actor?" value="who-is-your-favorite-actor"/>			
		</aui:select>
		<aui:input 
			label="Answer"
			maxlength="255"
			name="security_answer"
			required="true"
			title="Security Answer" 
			type="text"
		/>
		<aui:input 
			label="I have read, understand, and agree with the Terms of Use governing my access to and use of the Acme Movie Fanatics web site." 
			name="accepted_tou" 
			required="true"
			title="Terms of Use" 
			type="checkbox"
		/>
	</aui:fieldset>
	<aui:button name="submit" type="submit" value="Register"></aui:button>
</aui:form>
