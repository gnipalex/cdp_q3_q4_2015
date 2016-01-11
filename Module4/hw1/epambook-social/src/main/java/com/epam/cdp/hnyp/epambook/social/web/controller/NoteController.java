package com.epam.cdp.hnyp.epambook.social.web.controller;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.epam.cdp.hnyp.epambook.social.data.NoteData;
import com.epam.cdp.hnyp.epambook.social.model.Note;
import com.epam.cdp.hnyp.epambook.social.service.FriendshipService;
import com.epam.cdp.hnyp.epambook.social.service.NoteService;

@RequestMapping("/users/{userName}/timeline")
@RestController
public class NoteController {

    @Autowired
    private NoteService noteService;
    @Autowired
    private FriendshipService friendshipService;
    @Resource
    private Converter<Note, NoteData> noteConverter;

    @RequestMapping(value = "", method = RequestMethod.POST)
    public Note postNote(@PathVariable String userName,
            @RequestParam String noteText) {
        return noteService.create(noteText, userName, userName);
    }
    
    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<NoteData> getUsersTimeline(@PathVariable String userName) {
        List<Note> notes = noteService.getAllByTimelineOwner(userName);
        return convertAllNotes(notes);
    }
    
    private List<NoteData> convertAllNotes(List<Note> notes) {
        return notes.stream().map(noteConverter::convert)
                .collect(Collectors.toList());
    }
    
    @RequestMapping(value = "/{friendName}/timeline", method = RequestMethod.GET)
    public List<NoteData> getFriendsTimeline(@PathVariable String userName,
            @PathVariable String friendName) {
        friendshipService.getFriendProfile(userName, friendName);
        List<Note> notes = noteService.getAllByTimelineOwner(friendName);
        return convertAllNotes(notes);
    }
    
    @RequestMapping(value = "/{friendName}/timeline", method = RequestMethod.POST)
    public Note postNoteToFriendsTimeline(@PathVariable String userName,
            @PathVariable String friendName, 
            @RequestParam String noteText) {
        friendshipService.getFriendProfile(userName, friendName);
        return noteService.create(noteText, userName, friendName);
    }
    
}
