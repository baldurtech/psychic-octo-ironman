package com.bodejidi.hellojdbc;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthServlet extends HttpServlet {

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        if(isNotLogin(req)) {
            showLoginPage(req, resp);
        } else {
            resp.sendRedirect(req.getContextPath());
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        login(req, resp);
    }

    public boolean isNotLogin(HttpServletRequest req)
        throws IOException, ServletException {

        Long memberId = (Long)req.getSession().getAttribute("memberId");
        return null == memberId;
    }

    public void login(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        String username = req.getParameter("username");
        String password = req.getParameter("password");

        if(username.equalsIgnoreCase("admin") &&
           password.equals("s3cr3t")) {
            HttpSession session = req.getSession();
            session.setAttribute("memberId", 0L);

            showLoginSuccess(req, resp);
        } else {
            showLoginFail(req, resp);
        }
    }

    public void showLoginFail(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("  <head>");
        out.println("    <title>Login Fail!</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>Login Fail!</h1>");
        out.println("  </body>");
        out.println("</html>");

    }

    public void showLoginSuccess(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        Integer timeout = 5;
        PrintWriter out = resp.getWriter();
        out.println("<html>");
        out.println("  <head>");
        out.println("    <meta http-equiv=\"refresh\""
                    + " content=\"" + timeout + "; URL=member?action=List\">");
        out.println("    <title>Login Success</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>Welcome Admin!</h1>");
        out.println("    Please wait for " + timeout
                    + " seconds, if not redirect please click ");
        out.println("    <a href=\"member\">Member list</a>");
        out.println("  </body>");
        out.println("</html>");
    }

    public void showLoginPage(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        out.println("<html>");
        out.println("  <head>");
        out.println("    <title>Login</title>");
        out.println("  </head>");
        out.println("  <body>");
        out.println("    <h1>Please Login!</h1>");
        out.println("    <form action=\"" + req.getRequestURI() + "\" method=\"POST\">");
        out.println("      <label>Username: <input type=\"text\" name=\"username\"/></label>");
        out.println("      <label>Password: <input type=\"password\" name=\"password\"/></label>");
        out.println("      <input type=\"submit\" name=\"action\" value=\"Login\" />");
        out.println("    </form>");
        out.println("  </body>");
        out.println("</html>");
    }

}
