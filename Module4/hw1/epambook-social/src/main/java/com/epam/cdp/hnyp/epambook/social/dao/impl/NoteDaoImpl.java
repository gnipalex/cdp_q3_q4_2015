package com.epam.cdp.hnyp.epambook.social.dao.impl;

import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import com.epam.cdp.hnyp.epambook.social.dao.NoteDao;
import com.epam.cdp.hnyp.epambook.social.model.Note;

public class NoteDaoImpl extends AbstractJdbcDao<Note> implements NoteDao {

    private static final String SELECT_ALL_BY_TIMELINE_OWNER_ID = "SELECT * FROM note WHERE timelineOwnerId=?";
    private static final String SELECT_BY_ID = "SELECT * FROM note WHERE id=?";
    private static final String CREATE = "INSERT INTO note(noteText,authorId,timelineOwnerId) VALUE (?,?,?)";
    
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
    
//    protected Note createEntity() {
//        return new Note();
//    }

    @Override
    protected RowMapper<Note> getRowMapper() {
//        return (rs, i) -> {
//            Note note = createEntity();
//            note.setId(rs.getLong("id"));
//            note.setAuthorId(rs.getLong("authorId"));
//            note.setNoteText(rs.getString("noteText"));
//            note.setPostDateTime(rs.getTimestamp("postDateTime"));
//            note.setTimelineOwnerId(rs.getLong("timelineOwnerId"));
//            return note;
//        };
        return BEAN_PROPERTY_ROW_MAPPER;
    }

}
