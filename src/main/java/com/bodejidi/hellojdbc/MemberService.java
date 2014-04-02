package com.bodejidi.hellojdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

        String sql = "INSERT INTO member (first_name, last_name, date_created, last_updated) VALUES(?, ?, ?, ?)";
        logger.debug("SQL: " + sql);
        DatabaseService ds = DatabaseService.newInstance();

        ds.prepare(sql)
            .setString(firstName)
            .setString(lastName)
            .setDate(new Date())
            .setDate(new Date())
            .execute();

        ds.close();
        return member;
    }

    public void deleteById(Long id) throws SQLException {
        String sql = "DELETE FROM " + MEMBER_TABLE + " where " + MEMBER_ID + "=" + id;
        logger.debug("SQL: " + sql);

        DatabaseService ds = DatabaseService.newInstance();
        ds.execute(sql);
        ds.close();
    }

    public Member update(Member member) throws SQLException {
        Long id = member.getId();
        String firstName = member.getFirstName();
        String lastName = member.getLastName();

        String sql = "update " + MEMBER_TABLE + " set " + MEMBER_FIRST_NAME + "='" + firstName + "', " + MEMBER_LAST_NAME + "='" + lastName + "' where " + MEMBER_ID + "="+id;
        logger.debug("SQL: " + sql);

        DatabaseService ds = DatabaseService.newInstance();
        ds.execute(sql);
        ds.close();

        return member;
    }

    public Member getMemberById(Long paramId)
        throws SQLException {

        Member member = new Member();

        String sql = "SELECT * from " + MEMBER_TABLE;
        sql = sql + " WHERE " + MEMBER_ID + "=" + paramId;
        logger.debug("SQL: " + sql);

        DatabaseService databaseService = DatabaseService.newInstance();

        ResultSet rs = databaseService.executeQuery(sql);

        rs.next();
        member.setId(rs.getLong(MEMBER_ID));
        member.setFirstName(rs.getString(MEMBER_FIRST_NAME));
        member.setLastName(rs.getString(MEMBER_LAST_NAME));

        databaseService.close();

        return member;
    }

    public List<Member> findAllMember() throws SQLException {
        List<Member> memberList = new ArrayList<Member>();

        DatabaseService databaseService = DatabaseService.newInstance();

        String sql = "SELECT * from " + MEMBER_TABLE;
        logger.debug("SQL: " + sql);

        ResultSet rs = databaseService.executeQuery(sql);

        while(rs.next()) {
            Member member = new Member();
            member.setId(rs.getLong(MEMBER_ID));
            member.setFirstName(rs.getString(MEMBER_FIRST_NAME));
            member.setLastName(rs.getString(MEMBER_LAST_NAME));
            memberList.add(member);
        }

        databaseService.close();

        return memberList;
    }

}
