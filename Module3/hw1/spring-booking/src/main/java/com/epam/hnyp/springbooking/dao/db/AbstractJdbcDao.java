package com.epam.hnyp.springbooking.dao.db;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.KeyHolder;

public abstract class AbstractJdbcDao <T> {
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    protected T queryForObject(String query, Object... args) {
        List<T> results = queryForList(query, args);
        return getFirstOrNull(results);
    }
    
    private T getFirstOrNull(List<T> items) {
        if (CollectionUtils.isNotEmpty(items)) {
            return items.iterator().next();
        }
        return null;
    }
    
    protected List<T> queryForList(String query, Object... args) {
        return getJdbcTemplate().query(query, getRowMapper(), args);
    }
    
    protected abstract RowMapper<T> getRowMapper();
    
    protected void updateRow(String query, Object... args) {
        assertSingleRowUpdated(getJdbcTemplate().update(query, args)); 
    }
    
    protected Number updateAndGetKey(String query, Object... args) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        assertSingleRowUpdated(updateWithKeyHolder(query, keyHolder, args));
        return keyHolder.getKey();
    }
    
    private void assertSingleRowUpdated(int actualUpdatedCount) {
        if (actualUpdatedCount != 1) {
            throw new IncorrectResultSizeDataAccessException(1);
        }
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
                ps.setObject(i, args[i - 1]);
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
