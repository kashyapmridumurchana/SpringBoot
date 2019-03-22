package com.bridgelabz.fundoonotes.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.fundoonotes.controller.NoteController;
import com.bridgelabz.fundoonotes.dao.CollaboratorRepository;
import com.bridgelabz.fundoonotes.dao.LabelRepository;
import com.bridgelabz.fundoonotes.dao.NoteRepository;
import com.bridgelabz.fundoonotes.model.Collaborator;
import com.bridgelabz.fundoonotes.model.Label;
import com.bridgelabz.fundoonotes.model.Note;
import com.bridgelabz.fundoonotes.utility.EmailUtil;
import com.bridgelabz.fundoonotes.utility.TokenGenerator;

@Service
public class NoteServiceimpl implements NoteService {

	  private static Logger logger = LoggerFactory.getLogger(NoteServiceimpl.class);

	
	@Autowired
	private NoteRepository noteRepository;

	@Autowired
	private TokenGenerator tokenGenerator;
	
	
	@Autowired
	private LabelRepository labelRepository;

	@Autowired
	private CollaboratorRepository collaboratorRepository;
	
	@Autowired
	private EmailUtil emailUtility;
	
	@Override
	public Note createNote( Note note, String token) {
		int userId = tokenGenerator.verifyToken(token);
		note.setUserId(userId);
		noteRepository.save(note);
		return note;
	}

	
	@Override
    public Note updateNote(String token, Note note, HttpServletRequest request) {
        Note newNote=null;
        int userId = tokenGenerator.verifyToken(token);
        List<Note> notes= noteRepository.findAllByUserId(userId);
        if(!notes.isEmpty())
        {
            newNote=notes.stream().filter(existingNote -> existingNote.getNoteId() == note.getNoteId()).findAny().get();
            Note updatedNote = newNoteUpdate(newNote, note);
            noteRepository.save(updatedNote);
            return updatedNote;
        }
        return null;

    }

    public Note newNoteUpdate(Note newNote, Note note) {
        if (note.getTitle() != null)
            newNote.setTitle(note.getTitle());
        if (note.getDescription() != null)
            newNote.setDescription(note.getDescription());
            newNote.setArchive(note.isArchive());
            newNote.setPinned(note.isPinned());
            newNote.setInTrash(note.isInTrash());
            newNote.setColor(note.getColor());
            newNote.setReminder(note.getReminder());
        return newNote;
    }
	
	


	public Note deleteNote(String token, int noteId, HttpServletRequest request) {
		int userId = tokenGenerator.verifyToken(token);
		Optional<Note> optional = noteRepository.findById(noteId);
		if (optional.isPresent()) {
			Note newNote = optional.get();
			if (newNote != null && (newNote.getUserId() == userId)) {
				noteRepository.delete(newNote);
			}
			return newNote;
		}
		return null;
	}

	@Override
	public List<Note> retrieveNote(String token, HttpServletRequest request) {
		int userId = tokenGenerator.verifyToken(token);
		List<Note> collabNotes = new ArrayList<>();
		List<Collaborator> collaborators = collaboratorRepository.findAllByUserId(userId);
		for (Collaborator collaborator : collaborators) {
			collabNotes.add(noteRepository.findById(collaborator.getNoteId()).get());
		}
		List<Note> notes = noteRepository.findAllByUserId(userId);
		notes.addAll(collabNotes);
		if (!notes.isEmpty()) {
			return notes;
		}
return null;
	}

	
	
	
	@Override
	public Label createLabel(Label label,String token)
	{
		int userId = tokenGenerator.verifyToken(token);
		label.setUserId(userId);
		labelRepository.save(label);
		return label;
	}
	
	
	public Label updateLabel(String token, Label label, HttpServletRequest request) {
		int userId = tokenGenerator.verifyToken(token);
		Optional<Label> optional = labelRepository.findById(label.getLabelId());
		if (optional.isPresent()) {
			Label newLabel = optional.get();
			if (newLabel != null && (newLabel.getUserId()==userId)) {
				newLabel.setLabelName(label.getLabelName());
				labelRepository.save(newLabel);
			}
			return newLabel;
		}
		return null;
	}

	@Override
	public List<Label> retrieveLabel(String token, HttpServletRequest request) {
		int userId = tokenGenerator.verifyToken(token);
		List<Label> labels=labelRepository.findAllByUserId(userId);
		if (!labels.isEmpty()) 
		{
				return labels;
		}
		return null;
	}

	@Override
	public Label deleteLabel(String token, int labelId, HttpServletRequest request) {
		int userId = tokenGenerator.verifyToken(token);
		Optional<Label> optional=labelRepository.findById(labelId); 
		if (optional.isPresent()) 
		{
			Label newLabel =optional.get();
			if (newLabel != null &&(newLabel.getUserId()==userId)) {
				labelRepository.delete(newLabel);
				return newLabel;
			}
		}
		return null;
	}

	@Override
	public boolean addNoteLabel( int noteId, Label existingLabel, HttpServletRequest request) {
		Optional<Label> optional=labelRepository.findById(existingLabel.getLabelId()); 
		Optional<Note> optional1=noteRepository.findById(noteId); 
		if (optional.isPresent() && optional1.isPresent()) 
		{
			Note note = optional1.get();
			Label label = optional.get();
			
			if(label!=null && note!=null)
			{
			List<Label> labels = note.getLabels();
			labels.add(label);
			if (!labels.isEmpty()) 
			{
			note.setLabels(labels);
			noteRepository.save(note);
			}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean removeNoteLabel(int noteId, int labelId, HttpServletRequest request) {
		Optional<Label> optionallabel=labelRepository.findById(labelId); 
		Optional<Note> optionalnote=noteRepository.findById(noteId); 
		if (optionallabel.isPresent() && optionalnote.isPresent()) {	
			Note note = optionalnote.get();
			List<Label> labels = note.getLabels();
			if (!labels.isEmpty()) {
				labels = labels.stream().filter(label -> label.getLabelId() != labelId).collect(Collectors.toList());
				note.setLabels(labels);
				noteRepository.save(note);
			
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean createCollaborator(String token, int noteId, int userId) {
		Collaborator collaborator = new Collaborator();
		collaborator = collaboratorRepository.save(collaborator.setNoteId(noteId).setUserId(userId));
		if (collaborator != null) 
		{
			emailUtility.sendEmail("","Note has been shared ","");
			return true;
			}
		return false;
}


	@Override
	public boolean deleteCollaborator(int noteId, int userId) {
		Collaborator collaborator = collaboratorRepository.findByNoteIdAndUserId(noteId, userId).get();
		if (collaborator != null) {
			collaboratorRepository.delete(collaborator);
			return true;}
		return false;
	}
	
	
	
	
	
}
