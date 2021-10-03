package com.example.multinotepad;


//A separate Note class used to represent note data
public class Note {

    private String lastSaveDate;
    private String noteTitle;
    private String noteText;
    private String trimText;

    public Note(String t, String n, String l){
        noteTitle = t;
        noteText = n;
        lastSaveDate = l;

        if(n.length() > 80){
            trimText = noteText.substring(0, 79) + "...";
        }
        else{
            trimText = noteText;
        }
    }

    public void setLastSaveDate(String lastSaveDate) {
        this.lastSaveDate = lastSaveDate;
    }

    public void setNoteTitle(String noteTitle){
        this.noteTitle = noteTitle;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
        if(noteText.length() > 80){
            trimText = noteText.substring(0, 79) + "...";
        }
        else{
            trimText = noteText;
        }
    }

    public String getLastSaveDate() {
        return lastSaveDate;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteText() {
        return noteText;
    }

    public String getTrimText() {
        return this.trimText; }
}
