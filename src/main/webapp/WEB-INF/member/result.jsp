<html>
  <head>
    <meta http-equiv="refresh" content="2; URL=member?action=List" />
  </head>
  <body>
<%
String message = (String) request.getAttribute("flash.message");
if(null != message) {
    out.print(message);
}
%>
<%
String errorMessage = (String) request.getAttribute("flash.errorMessage");
if(null != errorMessage) {
    out.print(errorMessage);
}
%>
  <br/>
  Please wait for 2 seconds, if not redirect please click <a href="member?action=List">here</a>.
  </body>
</html>
