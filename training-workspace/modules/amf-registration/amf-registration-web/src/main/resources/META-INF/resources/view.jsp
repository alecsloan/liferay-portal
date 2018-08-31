<%@ include file="/init.jsp" %>
<%@page import="com.liferay.amf.registration.portlet.RegistrationPortlet" %>
<%@page import="java.util.*" %>

<% 
	RegistrationPortlet reg = new RegistrationPortlet();

	Boolean created = false;
	if (renderRequest.getAttribute("created") != null)
		created = (Boolean)renderRequest.getAttribute("created");

	//Put states into a TreeMap so that they're ordered alphabetically
	Map<String,String> states = new TreeMap<String,String>(reg.getStates());

	request.setAttribute("states",states);
%>
<c:if test="<%= !themeDisplay.isSignedIn()%>">
	<c:if test="<%=created%>">
		<h2>Welcome!</h2>
		Use the Sign In button in the top right to access your account.
	</c:if>
	<c:if test="<%=!created %>">
		<liferay-portlet:actionURL name="registerUser" var="registerUserURL"></liferay-portlet:actionURL>
		<aui:form action="<%=registerUserURL%>" method="post" name="fm">
			<aui:fieldset label="Basic Info">
			
				<aui:input 
					label="First Name:"
					maxlength="50"
					name="first_name"
					required="true"
					title="First Name"
					type="text"
					cssClass="has-error"
				/>
				<% if (renderRequest.getAttribute("first_name") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						First Name:	<%=renderRequest.getAttribute("first_name") %>
					</aui:alert>
				<% } %>
				
				<aui:input 
					label="Last Name:"
					maxlength="50"
					name="last_name" 
					required="true"
					title="Last Name" 
					type="text" 
				/>
				<% if (renderRequest.getAttribute("last_name") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Last Name: <%=renderRequest.getAttribute("last_name") %>
					</aui:alert>
				<% } %>
		
				<aui:input
					label="Email Address:"
					maxlength="255"
					name="email_address"
					required="true"
					title="Email Address"
					type="email"
				/>
				<% if (renderRequest.getAttribute("email_address") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Email Address:	<%=renderRequest.getAttribute("email_address") %>
					</aui:alert>
				<% } %>
		
				<aui:input 
					label="Username:"
					maxlength="16"
					name="username"
					required="true"
					title="Username"
					type="text"
				/>
				<% if (renderRequest.getAttribute("username") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Username: <%=renderRequest.getAttribute("username") %>
					</aui:alert>
				<% } %>	
	
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
				<% if (renderRequest.getAttribute("birthday") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Birthday: <%=renderRequest.getAttribute("birthday") %>
					</aui:alert>
				<% } %>
	
				<aui:input 
					label="Password:"
					name="password1" 
					required="true"
					title="Password" 
					type="password"
				/>
				<% if (renderRequest.getAttribute("password1") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Password: <%=renderRequest.getAttribute("password1") %>
					</aui:alert>
				<% } %>
	
				<aui:input 
					label="Confirm Password:" 
					name="password2" 
					required="true"
					title="Confirm Password" 
					type="password"
				/>
				<% if (renderRequest.getAttribute("password2") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Password: <%=renderRequest.getAttribute("password2") %>
					</aui:alert>
				<% } %>
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
				<% if (renderRequest.getAttribute("address") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Address 1: <%=renderRequest.getAttribute("address") %>
					</aui:alert>
				<% } %>
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
				<% if (renderRequest.getAttribute("city") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						City: <%=renderRequest.getAttribute("city") %>
					</aui:alert>
				<% } %>
				
				<% if (states.size() == 1){ %>
				<aui:input 
					label="State"
					maxlength="2"
					name="state"
					required="true"
					title="state" 
					type="text"
				/>
				<% } else { %>
				<aui:select label="State: " name="state" title="State" required="true">
					<c:forEach items="${states}" var="state">
						<aui:option label="${state.key}" value="${state.value}"></aui:option>
					</c:forEach>
				</aui:select>
				<% } %>
				<aui:input 
					label="Zip Code"
					maxlength="5"
					name="zip"
					required="true"
					title="zip" 
					type="text"
				/>
				<% if (renderRequest.getAttribute("zip") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Zip: <%=renderRequest.getAttribute("zip") %>
					</aui:alert>
				<% } %>
			</aui:fieldset>
		
			<hr />
	
			<aui:fieldset label="Misc.">
				<aui:select label="Security Question:" name="security_question" required="true" title="Security Question">
					<aui:option label="Must choose one of the following" value="" selected="true"/>
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
				<% if (renderRequest.getAttribute("security_answer") != null) { %>
					<aui:alert closable="true" cssClass="alert alert-danger">
						Answer: <%=renderRequest.getAttribute("security_answer") %>
					</aui:alert>
				<% } %>
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
	</c:if>
</c:if>

<c:if test="<%= themeDisplay.isSignedIn()%>">
	<h2><%=themeDisplay.getUser().getGreeting() %></h2>
	<p>You are already logged in.</p>
</c:if>
