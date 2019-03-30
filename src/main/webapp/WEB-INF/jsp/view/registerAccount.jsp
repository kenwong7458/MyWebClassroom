<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyWebClassroom</title>
    </head>
    <body>
        <c:url var="loginUrl" value="/login" />
        <form action="${logoutUrl}" method="POST">
            <input type="submit" value="Log in" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
        
        <h3>Create a Course</h3>
        <form:form method="POST" enctype="multipart/form-data" modelAttribute="registerAccountForm">
            <form:label path="username">Username</form:label><br/>
            <form:input type="text" path="username" /><br/><br/>
            
            <form:label path="email">Email</form:label><br/>
            <form:input type="email" path="email" /><br/><br/>
            
            <form:label path="password">Password</form:label><br/>
            <form:input type="password" path="password" /><br/><br/>
            
            <form:label path="cPassword">Confirm your password</form:label><br/>
            <form:input type="password" path="cPassword" /><br/><br/>

            <input type="submit" value="Submit" />

        </form:form>
    </body>
</html>

