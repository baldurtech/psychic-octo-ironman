<html>
  <head>
    <title>新增会员</title>
  </head>
  <body>
    <h1>新增会员</h1>
    <form action="member" method="POST">
      <label>First Name: <input type="text" name="first_name"/></label>
      <label>Last Name: <input type="text" name="last_name"/></label>
      <input type="hidden" name="action" value="Save"/>
      <input type="submit" value="新增"/>
    </form>
    <p><a href="member">Member List</a></p>
  </body>
</html>
