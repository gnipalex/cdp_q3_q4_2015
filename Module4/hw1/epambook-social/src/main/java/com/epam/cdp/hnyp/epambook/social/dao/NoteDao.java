package com.epam.cdp.hnyp.epambook.social.dao;

import java.util.List;

import com.epam.cdp.hnyp.epambook.social.model.Note;

public interface NoteDao {
    
    Note getById(long id); 
    
    Note create(Note note); 

    List<Note> getAllByTimelineOwner(long timelineOwnerId);
    
}
