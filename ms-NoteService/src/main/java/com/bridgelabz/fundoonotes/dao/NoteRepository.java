package com.bridgelabz.fundoonotes.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bridgelabz.fundoonotes.model.Note;



@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {

	List<Note> findAllByUserId(int userId);
	
	List<Note> findAllArchiveNotes(int userId,boolean isArchiveOrNot);


}
