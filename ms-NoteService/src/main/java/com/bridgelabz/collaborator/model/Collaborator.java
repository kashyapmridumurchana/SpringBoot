package com.bridgelabz.collaborator.model;

import javax.persistence.Column;

public class Collaborator {
	
	@Column(name = "userId")
	private int userId;
	
	@Column(name = "noteId")
	private int noteId;



}
