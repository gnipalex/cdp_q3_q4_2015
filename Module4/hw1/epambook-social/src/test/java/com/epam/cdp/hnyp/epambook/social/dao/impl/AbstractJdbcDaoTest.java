package com.epam.cdp.hnyp.epambook.social.dao.impl;

import static java.util.Arrays.asList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyVararg;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.KeyHolder;

@RunWith(MockitoJUnitRunner.class)
public class AbstractJdbcDaoTest {

	private static final String SAMPLE_QUERY = StringUtils.EMPTY;

    private static final Object[] SAMPLE_ARGUMENTS = new Object[] { "aaa" };
    
    @Mock
	private JdbcTemplate mockJdbcTemplate;
	@Mock
	private KeyHolder mockKeyHolder;
	@Mock
	private RowMapper<Object> mockRowMapper;
	
	@InjectMocks
	private StubAbstractJdbcDao stubAbstractJdbcDao = new StubAbstractJdbcDao();
	
	@Test
	public void shouldCallJdbcTemplateUpdate_whenPerformUpdateAndGetKey() {
	    when(mockJdbcTemplate.update(any(PreparedStatementCreator.class), any(KeyHolder.class)))
	        .thenReturn(1);
		stubAbstractJdbcDao.updateAndGetKey(anyString(), anyVararg());
		verify(mockJdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
	}
	
	@Test(expected = IncorrectResultSizeDataAccessException.class)
    public void shouldThrowIncorrectResultSizeDataAccessException_whenPerformUpdateAndGetKeyAndNoRowsUpdated() {
	    stubAbstractJdbcDao.updateAndGetKey(anyString(), anyVararg());
	}
	
	@Test
	public void shouldCallJdbcTemplateUpdate_whenPerformUpdateWithKeyHolder() {
		stubAbstractJdbcDao.updateWithKeyHolder(SAMPLE_QUERY, mockKeyHolder, SAMPLE_ARGUMENTS);
		verify(mockJdbcTemplate).update(any(PreparedStatementCreator.class), eq(mockKeyHolder));
	}
	
	@Test
	public void shouldReturnPageNumMinusOneMultiplyPageSize_whenPerformGetOffset() {
		int pageSize = 2, pageNum = 3;
		int result = stubAbstractJdbcDao.getOffset(pageSize, pageNum);
		assertThat(result).isEqualTo((pageNum - 1) * pageSize);
	}
	
	@Test
	public void shouldCallJdbcTemplateUpdate_whenPerformUpdateRow() {
	    when(mockJdbcTemplate.update(SAMPLE_QUERY, SAMPLE_ARGUMENTS)).thenReturn(1);
	    stubAbstractJdbcDao.updateRow(SAMPLE_QUERY, SAMPLE_ARGUMENTS);
	    verify(mockJdbcTemplate).update(SAMPLE_QUERY, SAMPLE_ARGUMENTS);
	}
	
	@Test(expected = IncorrectResultSizeDataAccessException.class)
    public void shouldThrowIncorrectResultSizeDataAccessException_whenPerformUpdateRowAndNoRowsChanged() {
	    stubAbstractJdbcDao.updateRow(SAMPLE_QUERY, SAMPLE_ARGUMENTS);
	}
	
	@Test
    public void shouldCallJdbcTemplateQuery_whenPerformQueryForObject() {
	    stubAbstractJdbcDao.queryForObject(SAMPLE_QUERY, SAMPLE_ARGUMENTS);
	    verify(mockJdbcTemplate).query(SAMPLE_QUERY, mockRowMapper, SAMPLE_ARGUMENTS);
	}
	
	@Test
    public void shouldReturnFirstItem_whenPerformQueryForObject() {
		Object object1 = new Object();
		Object object2 = new Object();
		when(mockJdbcTemplate.query(SAMPLE_QUERY, mockRowMapper, SAMPLE_ARGUMENTS))
			.thenReturn(asList(object1, object2));
	    Object result = stubAbstractJdbcDao.queryForObject(SAMPLE_QUERY, SAMPLE_ARGUMENTS);
	    assertThat(result).isEqualTo(object1);
	}
	
	@Test
    public void shouldCallJdbcTemplateQuery_whenPerformQueryForList() {
	    stubAbstractJdbcDao.queryForList(SAMPLE_QUERY, SAMPLE_ARGUMENTS);
	    verify(mockJdbcTemplate).query(SAMPLE_QUERY, mockRowMapper, SAMPLE_ARGUMENTS);
	}
	
	private class StubAbstractJdbcDao extends AbstractJdbcDao<Object> {
        @Override
        protected RowMapper<Object> getRowMapper() {
            return mockRowMapper;
        }
    }

}
