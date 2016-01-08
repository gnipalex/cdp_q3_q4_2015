<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" trimDirectiveWhitespaces="true" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
	<head>
		<title>Users List Page</title>
	</head>
	<body>
		<h2>Users found for ${userName}</h2>
		<c:choose>
			<c:when test="${not empty users}">
				<table>
					<thead>
						<tr>
							<th>id</th>
							<th>name</th>
							<th>email</th>
						</tr>
					</thead>
					<tbody>
						<c:forEach items="${users}" var="user">
							<tr>
								<td>${user.id}</td>
								<td>${user.name}</td>
								<td>${user.email}</td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</c:when>
			<c:otherwise>
				<p>Nothing found</p>
			</c:otherwise>
		</c:choose>
	</body>
</html>