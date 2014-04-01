package com.bodejidi.hellojdbc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.sql.ResultSet;

import static com.bodejidi.hellojdbc.Constants.MEMBER_TABLE;
import static com.bodejidi.hellojdbc.Constants.MEMBER_ID;
import static com.bodejidi.hellojdbc.Constants.MEMBER_FIRST_NAME;
import static com.bodejidi.hellojdbc.Constants.MEMBER_LAST_NAME;
import static com.bodejidi.hellojdbc.Constants.MEMBER_FORM_ID;
import static com.bodejidi.hellojdbc.Constants.MEMBER_FORM_FIRST_NAME;
import static com.bodejidi.hellojdbc.Constants.MEMBER_FORM_LAST_NAME;
import static com.bodejidi.hellojdbc.Constants.FORM_SUBMIT_ACTION;

public class MemberServlet extends HttpServlet {

    static final Logger logger = LoggerFactory.getLogger(MemberServlet.class);

    public void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter("action");
        if(null == action || "".equals(action)) {
            action = "List";
        }

        switch(action.toLowerCase()) {
        case "create" : create(req, resp); break;
        case "show"   : show(req, resp); break;
        case "list"   :
        default       :
            list(req, resp);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        String action = req.getParameter(FORM_SUBMIT_ACTION);
        try{
            if("Save".equalsIgnoreCase(action)) {
                save(req, resp);
            } else if ("Delete".equalsIgnoreCase(action)) {
                delete(req ,resp);
            } else if("Update".equalsIgnoreCase(action)){
                update(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Operation not allowed.");
            }
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
        }

    }

    public void debug(String str) {
        logger.debug(str);
    }

    public void create(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {
        forward("create", req, resp);
    }

    public void list(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();

        try {
            MemberService memberService = new MemberService();
            req.setAttribute("memberList", memberService.findAllMember());
            forward("list", req, resp);
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
    }

    public void show(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        PrintWriter out = resp.getWriter();
        try {
            Long paramId = Long.valueOf(req.getParameter(MEMBER_FORM_ID));
            MemberService memberService = new MemberService();
            Member member = memberService.getMemberById(paramId);
            req.setAttribute("member", member);
            forward("show", req, resp);
        } catch (SQLException ex) {
            // handle any errors
            debug("SQLException: " + ex.getMessage());
            debug("SQLState: " + ex.getSQLState());
            debug("VendorError: " + ex.getErrorCode());
            out.println("Error!");
        }
    }

    public void save(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException {

        Member member = new Member();
        member.setFirstName(req.getParameter(MEMBER_FORM_FIRST_NAME));
        member.setLastName(req.getParameter(MEMBER_FORM_LAST_NAME));

        try {
            MemberService memberService = new MemberService();
            memberService.save(member);
            req.setAttribute("flash.message",
                             "Add " + member + " success!");
        } catch(Exception e) {
            req.setAttribute("flash.errorMessage",
                             "Error: first name or last name cannot be empty.");
        }
        forward("result", req, resp);
    }

    public void delete(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException, SQLException {

        PrintWriter out = resp.getWriter();

        String id = req.getParameter(MEMBER_FORM_ID);

        MemberService memberService = new MemberService();
        memberService.deleteById(Long.valueOf(id));

        req.setAttribute("flash.message", "Delete ID=" + id + " success!");
        forward("result", req, resp);
    }

    public void update(HttpServletRequest req, HttpServletResponse resp)
        throws IOException, ServletException, SQLException {

        PrintWriter out = resp.getWriter();

        Member member = new Member();
        member.setId(Long.valueOf(req.getParameter(MEMBER_FORM_ID)));
        member.setFirstName(req.getParameter(MEMBER_FORM_FIRST_NAME));
        member.setLastName(req.getParameter(MEMBER_FORM_LAST_NAME));

        MemberService memberService = new MemberService();
        memberService.update(member);

        req.setAttribute("flash.message",
                         "Update id=" + member.getId() + ": " + member + " success!");
        forward("result", req, resp);
    }

    public void forward(String page,
                        HttpServletRequest req,
                        HttpServletResponse resp)
        throws IOException, ServletException {

        String jsp = "/WEB-INF/member/" + page + ".jsp";
        getServletContext()
            .getRequestDispatcher(jsp)
            .forward(req, resp);
    }
}
