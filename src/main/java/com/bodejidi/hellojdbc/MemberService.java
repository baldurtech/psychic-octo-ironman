package com.bodejidi.hellojdbc;

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

public class MemberService {

    static final Logger logger = LoggerFactory.getLogger(MemberServlet.class);

    public Member save(Member member) throws Exception {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();

        if(firstName == null || firstName.length() == 0 || lastName == null || lastName.length() == 0) {
            throw new Exception("Member validator error!");
        }

        String sql = "INSERT INTO " + MEMBER_TABLE+ "(" + MEMBER_FIRST_NAME + ", " + MEMBER_LAST_NAME + ", date_created, last_updated) "
            + "VALUES('" + firstName + "', '" + lastName + "', now(), now());";
        logger.debug("SQL: " + sql);
        DatabaseService ds = DatabaseService.newInstance();
        ds.execute(sql);
        ds.close();
        return member;
    }

}
