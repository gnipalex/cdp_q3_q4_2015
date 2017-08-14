package com.epam.cdp.hnyp.epambook.social.web.converter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;

import com.epam.cdp.hnyp.epambook.social.data.NoteData;
import com.epam.cdp.hnyp.epambook.social.model.Note;
import com.epam.cdp.hnyp.epambook.social.model.UserProfile;
import com.epam.cdp.hnyp.epambook.social.service.UserProfileService;

public class NoteConverter implements Converter<Note, NoteData> {
    
    @Autowired
    private UserProfileService userProfileService;
    
    @Override
    public NoteData convert(Note source) {
        NoteData target = new NoteData();
        target.setNoteText(source.getNoteText());
        UserProfile author = userProfileService.getById(source.getAuthorId());
        if (author != null) {
            target.setAuthor(author.getUserName());
        }
        return target;
    }
    
}
