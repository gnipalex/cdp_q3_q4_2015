package com.epam.cdp.hnyp.epambook.social.model;

import java.util.Date;

public class Note {
    
    private long id;
    private long authorId;
    private long timelineOwnerId;
    private String noteText;
    private Date postDateTime;
    
    public long getAuthorId() {
        return authorId;
    }
    public void setAuthorId(long authorId) {
        this.authorId = authorId;
    }
    public long getTimelineOwnerId() {
        return timelineOwnerId;
    }
    public void setTimelineOwnerId(long timelineOwnerId) {
        this.timelineOwnerId = timelineOwnerId;
    }
    public String getNoteText() {
        return noteText;
    }
    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }
    public Date getPostDateTime() {
        return postDateTime;
    }
    public void setPostDateTime(Date postDateTime) {
        this.postDateTime = postDateTime;
    }
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

}
