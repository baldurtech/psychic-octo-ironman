<%@ page import="com.bodejidi.hellojdbc.Member" %>
<%@ page pageEncoding="UTF-8" %>
<%
Member member = (Member) request.getAttribute("member");
%>
<html>
  <head>
    <title>Member</title>
  </head>
  <body>
    Welcome, admin. <a href="/hellojdbc/auth/logout">logout</a>
    <h1><% out.print(member.getId());%>号会员</h1>
    <form action="member" method="POST">
      <table border="1">
        <tr>
          <td>ID</td>
          <td><% out.print(member.getId());%></td>
        </tr>
        <tr>
          <td>First Name</td>
          <td><input type="text" name="first_name" value="<% out.print(member.getFirstName()); %>" /></td>
        </tr>
        <tr>
          <td>Last Name</td>
          <td><input type="text" name="last_name" value="<% out.print(member.getLastName());%>" /></td>
        </tr>
      </table>
      <input type="hidden" name="id" value="<% out.print(member.getId());%>" />
      <input type="submit" name="action" value="Update" />
      <input type="submit" name="action" value="Delete" />
    </form>
    <p><a href="member">Member list</a></p>
  </body>
</html>
