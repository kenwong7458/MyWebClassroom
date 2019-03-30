<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout"/>
        <form action="${logoutUrl}" method="post">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Course #${courseId}</h2>
        <form:form method="POST" enctype="multipart/form-data" modelAttribute="courseForm">
            <form:label path="courseName">Course Name</form:label><br/>
            <form:input type="text" path="courseName" /><br/><br/>
            
            <form:label path="courseDescription">Course Description</form:label><br/>
            <form:input type="text" path="courseDescription" /><br/><br/>
            <c:if test="${course.numberOfAttachments > 0}">
                <b>Attachments:</b><br/>
                <ul>
                    <c:forEach items="${course.attachments}" var="attachment">
                        <li>
                            <c:out value="${attachment.name}" />
                            [<a href="<c:url value="/classroom/${courseId}/delete/${attachment.name}" />">Delete</a>]
                        </li>
                    </c:forEach>
                </ul>
            </c:if>
            <b>Add attachments</b><br />
            <input type="file" name="attachments" multiple="multiple"/><br/><br/>
            <input type="submit" value="Save"/>
        </form:form>
    </body>
</html>