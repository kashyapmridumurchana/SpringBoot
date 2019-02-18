package com.bridgelabz.fundoonotes.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.bridgelabz.fundoonotes.service.NoteService;



public class NoteController
{
	
	 @Autowired  
	    private NoteService noteService;  
	 
	
	
	

	

	//Note updateNote(String token, int noteId, Note note, HttpServletRequest request);

	//Note deleteNote(String token, int noteId, HttpServletRequest request);

}
