<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Customer Support</title>
    </head>
    <body>
        <h3>Course #${courseId}: <c:out value="${course.courseName}" /></h3>
        <br /><br />
        <i>Lecturer - <c:out value="${course.courseLecturer}" /></i><br /><br />
        <c:out value="${course.courseLecturer}" /><br /><br />
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
