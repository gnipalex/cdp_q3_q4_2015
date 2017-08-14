package com.epam.cdp.hnyp.epambook.social.service;

import java.util.List;

import com.epam.cdp.hnyp.epambook.social.model.Note;

public interface NoteService {
    
    /**
     * Creates new note for timeline and userProfile. 
     * Post date is being generated automatically.
     * @param noteText to be created
     * @param authorName userName of author
     * @param timelineOwnerName userName of timeline owner
     * @return created Note with generated id and postDate
     * @throws IllegalArgumentException if author or timelineOwner not found
     */
    Note create(String noteText, String authorName, String timelineOwnerName); 
    
    /**
     * Gets all notes of the timeline owner
     * @param timelineOwnerName
     * @return List of Note's or empty list if no notes found
     * @throws IllegalArgumentException if timelineOwner profile doesn't exist
     */
    List<Note> getAllByTimelineOwner(String timelineOwnerName);
    
}
