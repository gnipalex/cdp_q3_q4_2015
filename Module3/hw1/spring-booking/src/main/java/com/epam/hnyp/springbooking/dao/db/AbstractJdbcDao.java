package com.epam.hnyp.springbooking.dao.db;

import java.sql.PreparedStatement;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

public abstract class AbstractJdbcDao {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    protected Number updateAndGetKey(String query, Object... args) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        if (updateWithKeyHolder(query, keyHolder, args) != 1) {
            throw new IncorrectResultSizeDataAccessException(1);
        }
        return keyHolder.getKey();
    }
    
    protected int updateWithKeyHolder(String query, KeyHolder keyHolder, Object... args) {
        PreparedStatementSetter statementSetter = getPreparedStatementSetter(args);
        PreparedStatementCreator statementCreator = getPraparedStatementCreator(query, statementSetter);
        return jdbcTemplate.update(statementCreator, keyHolder);
    }
    
    private PreparedStatementCreator getPraparedStatementCreator(String query, PreparedStatementSetter preparedStatementSetter) {
        return (connection) -> {
            PreparedStatement preparedStatement = connection.prepareStatement(query, 
                    Statement.RETURN_GENERATED_KEYS);
            preparedStatementSetter.setValues(preparedStatement);
            return preparedStatement;
        };
    }

    private PreparedStatementSetter getPreparedStatementSetter(Object... args) {
        return (ps) -> {
            for (int i = 1; i <= args.length; i++) {
                ps.setObject(i, args[i]);
            }
        };
    }
    
    protected int getOffset(int pageSize, int pageNum) {
        return (pageNum - 1) * pageSize;
    }

    protected JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }
    
}
