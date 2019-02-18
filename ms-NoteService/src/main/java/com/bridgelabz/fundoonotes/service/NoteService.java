package com.bridgelabz.fundoonotes.service;

import javax.servlet.http.HttpServletRequest;

public interface NoteService 
{

	boolean createNote(String token, Note note, HttpServletRequest request);

	//List<Note> retrieveNote(String token, HttpServletRequest request);
	
	//Note updateNote(String token, int noteId, Note note, HttpServletRequest request);

		//Note deleteNote(String token, int noteId, HttpServletRequest request);
}
