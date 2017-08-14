package com.epam.cdp.hnyp.epambook.social.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.epam.cdp.hnyp.epambook.social.dao.NoteDao;
import com.epam.cdp.hnyp.epambook.social.model.Note;

public class NoteDaoImpl extends AbstractJdbcDao<Note> implements NoteDao {

    private static final String SELECT_ALL_BY_TIMELINE_OWNER_ID = "SELECT * FROM note WHERE timelineOwnerId=? ORDER BY postDateTime DESC";
    private static final String SELECT_BY_ID = "SELECT * FROM note WHERE id=?";
    private static final String CREATE = "INSERT INTO note(noteText,authorId,timelineOwnerId) VALUES (?,?,?)";
    
    private static final RowMapper<Note> BEAN_PROPERTY_ROW_MAPPER = new BeanPropertyRowMapper<>(Note.class);
    
    @Override
    public Note create(Note note) {
        Object[] args = {note.getNoteText(), note.getPostDateTime(), note.getAuthorId(), note.getTimelineOwnerId()};
        Number key = updateAndGetKey(CREATE, args);
        note.setId(key.longValue());
        return note;
    }

    @Override
    public List<Note> getAllByTimelineOwner(long timelineOwnerId) {
        return queryForList(SELECT_ALL_BY_TIMELINE_OWNER_ID, timelineOwnerId);
    }
    
    @Override
    public Note getById(long id) {
        return queryForObject(SELECT_BY_ID, id);
    }
    
    @Override
    protected RowMapper<Note> getRowMapper() {
        return BEAN_PROPERTY_ROW_MAPPER;
    }

}
