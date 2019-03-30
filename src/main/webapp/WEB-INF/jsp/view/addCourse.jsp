<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyWebClassroom</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout" />
        <form action="${logoutUrl}" method="POST">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
        
        <h3>Create a Course</h3>
        <form:form method="POST" enctype="multipart/form-data" modelAttribute="courseForm">
            <form:label path="courseName">Course Name</form:label><br/>
            <form:input type="text" path="courseName" /><br/><br/>

            <form:label path="courseDescription">Course Description</form:label><br/>
            <form:input type="text" path="courseDescription" /><br/><br/>
            
            Additional Attachment:
            <input type="file" name="attachments" multiple="multiple" /><br/><br/>

            <input type="submit" value="Submit" />

        </form:form>
    </body>
</html>
