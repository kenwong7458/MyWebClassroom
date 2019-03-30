<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <c:url var="logoutUrl" value="/logout" />
        <form action="${logoutUrl}" method="POST">
            <input type="submit" value="Log out" />
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
        </form>
        <h3>Course #${courseId}: <c:out value="${course.courseName}" /></h3>
        <br /><br />
        <i>Lecturer - <c:out value="${course.courseLecturer}" /></i><br /><br />
        Description: <c:out value="${course.courseDescription}" /><br /><br />
        <c:if test="${course.numberOfAttachments > 0}">
            Attachments:
            <c:forEach items="${course.attachments}" var="attachment" varStatus="status">
                <c:if test="${!status.first}">, </c:if>
                <a href="<c:url value="/classroom/view/${courseId}/attachment/${attachment.name}" />">
                    <c:out value="${attachment.name}" /></a>
            </c:forEach><br /><br />
        </c:if>
        <a href="<c:url value="/classroom/listCourse" />">Return to course list</a>
    </body>
</html>
