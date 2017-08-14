package com.epam.cdp.hnyp.epambook.social.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.epam.cdp.hnyp.epambook.social.dao.NoteDao;
import com.epam.cdp.hnyp.epambook.social.model.Note;
import com.epam.cdp.hnyp.epambook.social.model.UserProfile;
import com.epam.cdp.hnyp.epambook.social.service.NoteService;
import com.epam.cdp.hnyp.epambook.social.service.UserProfileService;

public class NoteServiceImpl implements NoteService {

    @Autowired
    private NoteDao noteDao;
    @Autowired
    private UserProfileService userProfileService;
    
    @Override
    public Note create(String noteText, String authorName, String timelineOwnerName) { 
        UserProfile authorProfile = userProfileService.getExistingUserProfile(authorName);
        UserProfile timelineOwnerProfile = userProfileService.getExistingUserProfile(timelineOwnerName);
        
        Note note = createAndPopulateNoteEntity(noteText, authorProfile,  timelineOwnerProfile);
        
        Note createdNote = noteDao.create(note);
        return noteDao.getById(createdNote.getId());
    }

    protected Note createAndPopulateNoteEntity(String noteText,
            UserProfile authorProfile, UserProfile timelineOwnerProfile) {
        Note note = createEntity();
        note.setAuthorId(authorProfile.getId());
        note.setTimelineOwnerId(timelineOwnerProfile.getId());
        note.setNoteText(noteText);
        return note;
    }
    
    protected Note createEntity() {
        return new Note();
    }

    @Override
    public List<Note> getAllByTimelineOwner(String timelineOwnerName) {
        UserProfile timelineOwnerProfile = userProfileService.getExistingUserProfile(timelineOwnerName);
        return noteDao.getAllByTimelineOwner(timelineOwnerProfile.getId());
    }

}
