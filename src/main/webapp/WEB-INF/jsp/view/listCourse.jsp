<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyWebClassroom</title>
    </head>
    <body>
        <security:authorize access="hasAnyRole('ADMIN','TEACHER','STUDENT')">
            <c:url var="logoutUrl" value="/logout" />
            <form action="${logoutUrl}" method="POST">
                <input type="submit" value="Log out" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </security:authorize>
        
        <security:authorize access="!hasAnyRole('ADMIN','TEACHER','STUDENT')">
            <c:url var="loginUrl" value="/login" />
            <form action="${loginUrl}" method="GET">
                <input type="submit" value="Log in" />
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
            </form>
        </security:authorize>
            
        <h3>Course List</h3>
        <security:authorize access="!hasAnyRole('ADMIN','TEACHER','STUDENT')">
            <i>Login to see more information of each course.</i><br/>
        </security:authorize>
            
        <security:authorize access="hasAnyRole('ADMIN','TEACHER')">
        <a href="<c:url value="/classroom/create" />">Create a Course </a><br/><br/>
        </security:authorize>
        <c:choose>
            <c:when test="${fn:length(courseDatabase)== 0}">
                <i>There are no courses in the system</i>
            </c:when>
            <c:otherwise>
                <i>There is/are ${fn:length(courseDatabase)} course(s) in total.</i><br/>
                <c:forEach items="${courseDatabase}" var="entry">
                    Course ${entry.key}:
                    <a href="<c:url value="/classroom/view/${entry.key}" />">
                        <c:out value="${entry.value.courseName}" /></a>
                    (Lecturer: <c:out value="${entry.value.courseLecturer}" />)<br/>
                    
                    <security:authorize access="hasAnyRole('ADMIN', 'TEACHER')">
                    [<a href="<c:url value="/classroom/edit/${entry.key}" />">Edit</a>]
                    </security:authorize>
                    <security:authorize access="hasRole('ADMIN')">
                        [<a href="<c:url value="/classroom/delete/${entry.key}" />">Delete</a>]
                    </security:authorize>
                </c:forEach>
            </c:otherwise>    
        </c:choose>
    </body>
</html>
