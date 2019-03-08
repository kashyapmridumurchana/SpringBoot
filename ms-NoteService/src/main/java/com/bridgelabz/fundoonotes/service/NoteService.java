package com.bridgelabz.fundoonotes.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Note;

public interface NoteService 
{
    //CRUD of note
	Note createNote( Note note, String token);

	List<Note> retrieveNote(String token, HttpServletRequest request);
	
	
	Note updateNote(String token, Note note, HttpServletRequest request);

	Note deleteNote(String token, int noteId, HttpServletRequest request);
	
	
	//CRUD of label
	Label createLabel( Label label,String token);
	
	Label updateLabel(String token, Label label, HttpServletRequest request);

	List<Label> retrieveLabel(String token, HttpServletRequest request);

	Label deleteLabel(String token, int labelId, HttpServletRequest request);

	boolean addNoteLabel( int noteId, Label label, HttpServletRequest request);

	boolean removeNoteLabel(int noteId, int labelId, HttpServletRequest request);

	
	
	
}
