package com.epam.hnyp.springbooking.dao.db;

import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.internal.matchers.AnyVararg;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.KeyHolder;

@RunWith(MockitoJUnitRunner.class)
public class AbstractJdbcDaoTest {

	@Mock
	private JdbcTemplate mockJdbcTemplate;
	@Mock
	private KeyHolder mockKeyHolder;
	
	@InjectMocks
	private StubAbstractJdbcDao stubAbstractJdbcDao;
	
	@Test
	public void shouldCallJdbcTemplateUpdate_whenPerformUpdateAndGetKey() {
		stubAbstractJdbcDao.updateAndGetKey(anyString(), anyObject());
		verify(mockJdbcTemplate).update(any(PreparedStatementCreator.class), any(KeyHolder.class));
	}
	
	@Test
	public void shouldCallJdbcTemplateUpdate_whenPerformUpdateWithKeyHolder() {
		Object[] args = new Object[] { "aaa" }; 
		stubAbstractJdbcDao.updateWithKeyHolder(anyString(), eq(mockKeyHolder), eq(args));
		verify(mockJdbcTemplate).update(any(PreparedStatementCreator.class), eq(mockKeyHolder));
	}
	
	@Test
	public void shouldReturnPageNumMinusOneMultiplyPageSize_whenPerformGetOffset() {
		int pageSize = 2, pageNum = 3;
		int result = stubAbstractJdbcDao.getOffset(pageSize, pageNum);
		assertThat(result).isEqualTo((pageNum - 1) * pageSize);
	}
	
	private static class StubAbstractJdbcDao extends AbstractJdbcDao {}

}
