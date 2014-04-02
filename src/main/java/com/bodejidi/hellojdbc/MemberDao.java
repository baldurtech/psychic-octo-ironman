package com.bodejidi.hellojdbc;

import java.sql.SQLException;

public class MemberDao {

    public Member save(Member member) {
        String firstName = member.getFirstName();
        String lastName = member.getLastName();

        String sql = "INSERT INTO member (first_name, last_name, date_created, last_updated) VALUES(?, ?, ?, ?)";
        DatabaseService ds = DatabaseService.newInstance();

        try {
            ds.prepare(sql)
                .setString(firstName)
                .setString(lastName)
                .setDate(new Date())
                .setDate(new Date())
                .execute();
        } catch(SQLException e) {
            throw new DataAccessException("Cannot save member: " + member, e);
        } finally {
            ds.close();
        }
        return member;
    }
}
