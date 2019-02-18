package com.bridgelabz.fundoonotes.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Note;
import com.bridgelabz.fundoonotes.service.NoteService;


@Controller
@RequestMapping("/note/")
public class NoteController
{
	
	 @Autowired  
	    private NoteService noteService;  
	 
	 
	 @PostMapping(value = "createnote")
		public ResponseEntity<String> createNote(@RequestHeader("token") String token, @RequestBody Note note,
				HttpServletRequest request)
	 {
			
		     Note note1=noteService.createNote(token, note, request);
				if (note1!=null) {
					return new ResponseEntity<String>("Successfully created", HttpStatus.OK);
			}
				else
			{
				return new ResponseEntity<String>("Cannot create a note", HttpStatus.NOT_FOUND);
			}
		}

	    

		@PutMapping(value = "updatenote")
		public ResponseEntity<?> updateNote(@RequestHeader("token") String token, @RequestParam("noteId") int noteId,
				@RequestBody Note note, HttpServletRequest request) {

			Note newNote = noteService.updateNote(token, noteId, note, request);
			if (newNote != null) {
				return new ResponseEntity<Note>(newNote, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Please enter the note id or verify your login",
						HttpStatus.NOT_FOUND);
			}
		}
	
		@DeleteMapping(value = "deletenote")
		public ResponseEntity<?> deleteNote(@RequestHeader("token") String token, @RequestParam("noteId") int noteId,
				HttpServletRequest request) {
			Note note = noteService.deleteNote(token, noteId, request);
			if (note != null) {
				return new ResponseEntity<Note>(note, HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("Please enter the note id or verify your login",
						HttpStatus.NOT_FOUND);
			}
		}

		@GetMapping(value = "retrievenote")
		public ResponseEntity<?> retrieveNote(@RequestHeader("token") String token, HttpServletRequest request) {
			List<Note> notes = noteService.retrieveNote(token, request);
			if (!notes.isEmpty()) {
				return new ResponseEntity<List<Note>>(notes, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("Please enter the note id or verify your login",
						HttpStatus.NOT_FOUND);
			}
		}

		@PostMapping(value = "createlabel")
		public ResponseEntity<String> createLabel(@RequestHeader("token") String token, @RequestBody Label label,
				HttpServletRequest request) 
		{
			Label label1=noteService.createLabel(token, label, request);
				if (label1!=null)
				{
					return new ResponseEntity<String>("Successfully created", HttpStatus.OK);
			} 
				
				return new ResponseEntity<String>("User id given is not present or not yet been activated",
						HttpStatus.CONFLICT);
			}
		
		@PutMapping(value = "updatelabel")
		public ResponseEntity<?> updateUser(@RequestHeader("token") String token, @RequestParam("labelId") int labelId,
				@RequestBody Label label, HttpServletRequest request) {
			Label newLabel = noteService.updateLabel(token, labelId, label, request);
			if (newLabel != null) {
				return new ResponseEntity<Label>(newLabel, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("User id given is not present or not yet been activated",
						HttpStatus.NOT_FOUND);
			}
		}
		
		@GetMapping(value = "retrievelabel")
		public ResponseEntity<?> retrieveLabel(@RequestHeader("token") String token, HttpServletRequest request) {
			List<Label> labels = noteService.retrieveLabel(token, request);
			if (!labels.isEmpty()) {
				return new ResponseEntity<List<Label>>(labels, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("User id given is not present or not yet been activated",
						HttpStatus.NOT_FOUND);
			}
		}
		
		
		@DeleteMapping(value = "deletelabel")
		public ResponseEntity<?> deleteLabel(@RequestHeader("token") String token, @RequestParam("labelId") int labelId,
				HttpServletRequest request) {

			Label label = noteService.deleteLabel(token, labelId, request);
			if (label != null) {
				return new ResponseEntity<Label>(label, HttpStatus.FOUND);
			} else {
				return new ResponseEntity<String>("User id given is not present or not yet been activated",
						HttpStatus.NOT_FOUND);
			}
		}

		@PutMapping(value = "addnotelabel")
		public ResponseEntity<?> addNoteLabel(@RequestHeader("token") String token, @RequestParam("noteId") int noteId,
				@RequestParam("labelId") int labelId, HttpServletRequest request) {
			if (noteService.addNoteLabel(token, noteId, labelId, request)) {
				return new ResponseEntity<String>("Successfully mapped", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("User id given is not present or not yet been activated",
						HttpStatus.NOT_FOUND);
			}
		}

		@DeleteMapping(value = "removenotelabel")
		public ResponseEntity<?> removeNoteLabel(@RequestHeader("token") String token, @RequestParam("noteId") int noteId,
				@RequestParam("labelId") int labelId, HttpServletRequest request) {
			if (noteService.removeNoteLabel(token, noteId, labelId, request)) {
				return new ResponseEntity<String>("Successfully mapped", HttpStatus.OK);
			} else {
				return new ResponseEntity<String>("User id given is not present or not yet been activated",
						HttpStatus.NOT_FOUND);
			}
		}

		
		
		}
			


		
