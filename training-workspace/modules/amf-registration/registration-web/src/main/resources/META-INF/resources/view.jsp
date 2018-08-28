<%@ include file="/init.jsp" %>
<%
	System.out.println(request.getParameter("test"));
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
		required="true"
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
</aui:form>
