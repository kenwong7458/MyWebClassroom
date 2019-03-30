<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>MyWebClassroom</title>
    </head>
    <body>
        <h3>Course List</h3>
        <a href="<c:url value="/classroom/create" />">Create a Course </a><br/><br/>
        
        <c:choose>
            <c:when test="${fn:length(courseDatabase)== 0}">
                <i>There are no courses in the system</i>
            </c:when>
            <c:otherwise>
                <c:forEach items="${courseDatabase}" var="entry">
                    Course ${entry.key}:
                    <a href="<c:url value="/classroom/view/${entry.key}" />">
                        <c:out value="${entry.value.courseName}" /></a>
                    (Lecturer: <c:out value="${entry.value.courseLecturer}" />)<br/>
                </c:forEach>
            </c:otherwise>    
        </c:choose>
    </body>
</html>
