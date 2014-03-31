<%@ page import="java.util.List, com.bodejidi.hellojdbc.Member" %>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
List<Member> memberList = (List<Member>) request.getAttribute("memberList");
%>
<html>
  <head>
    <title>Member List</title>
  </head>
  <body>
    Welcome, admin. <a href="<%=request.getContextPath()%>/auth/logout">logout</a>
    <h1>所有会员</h1>
    <table border="1">
      <tr>
        <td>ID</td>
        <td>Name</td>
      </tr>

      <c:forEach var="member" items="${memberList}">
      <tr>
        <td><a href="?action=Show&id=${member.id}">${member.id}</a></td>
        <td>${member.firstName} ${member.lastName}</td>
      </tr>
      </c:forEach>

    </table>
    <p><a href="?action=Create">Add member</a></p>
  </body>
</html>
