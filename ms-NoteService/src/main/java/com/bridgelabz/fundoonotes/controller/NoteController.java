package com.bridgelabz.fundoonotes.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Note;
import com.bridgelabz.fundoonotes.service.NoteService;

@Controller
@RequestMapping("/note/")
public class NoteController {
	private static Logger logger = LoggerFactory.getLogger(NoteController.class);

	@Autowired
	private NoteService noteService;

	@PostMapping(value = "createnote")
	public ResponseEntity<Void> createNote( @RequestBody Note note,@RequestHeader("token") String token) {

		Note note1 = noteService.createNote(note,token);
		if (note1 != null) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.CONFLICT);
		}
	}

	@PutMapping(value = "updatenote")
	public ResponseEntity<?> updateNote(@RequestHeader("token") String token, @RequestBody Note note,
			HttpServletRequest request) {
		logger.info("archive " + note.isArchive());
		Note newNote = noteService.updateNote(token, note, request);
		if (newNote != null) {
			return new ResponseEntity<Note>(newNote, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Please enter the note id or verify your login", HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "deletenote/{noteId:.+}")
	public ResponseEntity<?> deleteNote(@RequestHeader("token") String token, @PathVariable("noteId") int noteId,
			HttpServletRequest request) {
		Note note = noteService.deleteNote(token, noteId, request);
		if (note != null) {
			return new ResponseEntity<Note>(note, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Please enter the note id or verify your login", HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "retrievenote")
	public ResponseEntity<?> retrieveNote(@RequestHeader("token") String token, HttpServletRequest request) {
		List<Note> notes = noteService.retrieveNote(token, request);
		if (!notes.isEmpty()) {
			return new ResponseEntity<List<Note>>(notes, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Please enter the note id or verify your login", HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(value = "createlabel")
	public ResponseEntity<?> createLabel(@RequestBody Label label, @RequestHeader("token") String token) {
		Label label1 = noteService.createLabel(label, token);
		if (label1 != null) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		}

		return new ResponseEntity<String>("User id given is not present or not yet been activated",
				HttpStatus.CONFLICT);
	}

	@PutMapping(value = "updatelabel")
	public ResponseEntity<?> updateUser(@RequestHeader("token") String token, @RequestBody Label label,
			HttpServletRequest request) {
		Label newLabel = noteService.updateLabel(token, label, request);
		if (newLabel != null) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User id given is not present or not yet been activated",
					HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping(value = "retrievelabel")
	public ResponseEntity<?> retrieveLabel(@RequestHeader("token") String token, HttpServletRequest request) {
		List<Label> labels = noteService.retrieveLabel(token, request);
		if (!labels.isEmpty()) {
			return new ResponseEntity<List<Label>>(labels, HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User id given is not present or not yet been activated",
					HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "deletelabel/{labelId:.+}")
	public ResponseEntity<?> deleteLabel(@RequestHeader("token") String token, @PathVariable("labelId") int labelId,
			HttpServletRequest request) {

		Label label = noteService.deleteLabel(token, labelId, request);
		if (label != null) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User id given is not present or not yet been activated",
					HttpStatus.NOT_FOUND);
		}
	}

	@PutMapping(value = "addnotelabel/{noteId:.+}")
	public ResponseEntity<?> addNoteLabel( @PathVariable("noteId") int noteId,
			@RequestBody Label label, HttpServletRequest request) {
		if (noteService.addNoteLabel( noteId, label, request)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User id given is not present or not yet been activated",
					HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping(value = "removenotelabel")
	public ResponseEntity<?> removeNoteLabel( @RequestParam("noteId") int noteId,
			@RequestParam("labelId") int labelId, HttpServletRequest request) {
		if (noteService.removeNoteLabel( noteId, labelId, request)) {
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("User id given is not present or not yet been activated",
					HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping(value = "createcollaborator/{noteId}/{userId}")
	public ResponseEntity<?> createCollaborator(@RequestHeader("token") String token, @PathVariable("noteId") int noteId,
			@PathVariable("userId") int userId,HttpServletRequest request) {
		if (noteService.createCollaborator(token, noteId, userId))
			return new ResponseEntity<Void>(HttpStatus.OK);
		return new ResponseEntity<String>("There was a issue raised cannot create a collaborator", HttpStatus.CONFLICT);
}
	
	@DeleteMapping(value = "deletecollaborator/{noteId}/{userId}")
	public ResponseEntity<?> deleteCollaborator(@PathVariable("noteId") int noteId,
			@PathVariable("userId") int userId,HttpServletRequest request) {
		if (noteService.deleteCollaborator(noteId, userId))
			return new ResponseEntity<Void>(HttpStatus.OK);
		return new ResponseEntity<String>("There was a issue raised cannot delete a collaborator", HttpStatus.CONFLICT);
}
	@PostMapping(value = "photo/{noteId}")
	public ResponseEntity<?> storeFile(@RequestParam("file") MultipartFile file,
			@PathVariable("noteId") int noteId)
			throws IOException {
		if (noteService.storeFile(file,noteId))
			return new ResponseEntity<Void>(HttpStatus.OK);
		return new ResponseEntity<String>("Error in uploading the image", HttpStatus.CONFLICT);
	}
	
	@GetMapping("photo/{noteId}")
    public ResponseEntity<?> downloadFile(@RequestHeader("token") String token,
    		@PathVariable("noteId") int noteId) {
        Note note = noteService.getFile(token,noteId);
        if(note!=null)
			return new ResponseEntity<Note>(note,HttpStatus.OK);
        return new ResponseEntity<String>("Could not download the image", HttpStatus.CONFLICT);
}
	@DeleteMapping("photo/{imagesId}")
    public ResponseEntity<?> deleteFile(@PathVariable("imagesId") int imagesId) {
        if(noteService.deleteFile(imagesId))
			return new ResponseEntity<Void>(HttpStatus.OK);
        return new ResponseEntity<String>("Couldnot delete the image", HttpStatus.CONFLICT);
}
	
}
