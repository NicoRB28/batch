package com.examplebatch.batchdemo.configuration;

import com.examplebatch.batchdemo.model.Person;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PersonRowMapper implements RowMapper<Person> {
    @Override
    public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
        return new Person(rs.getInt("personId"),
                rs.getString("firstName"),
                rs.getString("lastName"));
    }
}
