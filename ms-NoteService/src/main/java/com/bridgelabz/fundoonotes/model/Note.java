package com.bridgelabz.fundoonotes.model;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SuppressWarnings("serial")
@Entity
public class Note implements Serializable {

	@Id
	@GeneratedValue
	@Column(name = "noteId")
	private int noteId;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "createdTime")
	@CreationTimestamp
	private Timestamp createdTime;

	@Column(name = "updatedTime")
	@UpdateTimestamp
	private Timestamp updatedTime;

	@Column(name = "isArchive")
	private boolean isArchive;

	@Column(name = "isPinned")
	private boolean isPinned;

	@Column(name = "inTrash")
	private boolean inTrash;

	@Column(name = "userId")
	private int userId;

	
	 @ManyToMany(fetch = FetchType.EAGER, targetEntity = Label.class, cascade = {
	 CascadeType.ALL })
	 @JoinTable(name = "Note_Label", joinColumns = { @JoinColumn(name = "noteId")
	 }, inverseJoinColumns = {
	 @JoinColumn(name = "labelId") })

	 private List<Label> labels;
	
	

	public int getNoteId() {
		return noteId;
	}

	public Note setNoteId(int noteId) {
		this.noteId = noteId;
		return this;
	}

	 public List<Label> getLabels() {
	 return labels;
	 }
	
	 public void setLabels(List<Label> labels) {
	 this.labels = labels;
	 }

	public String getTitle() {
		return title;
	}

	public Note setTitle(String title) {
		this.title = title;
		return this;
	}

	public String getDescription() {
		return description;
	}

	public Note setDescription(String description) {
		this.description = description;
		return this;
	}

	public int getUserId() {
		return userId;
	}

	public Note setUserId(int userId) {
		this.userId = userId;
		return this;
	}

	public boolean isArchive() {
		return isArchive;
	}

	public Note setArchive(boolean isArchive) {
		this.isArchive = isArchive;
		return this;
	}

	public boolean isPinned() {
		return isPinned;
	}

	public Note setPinned(boolean isPinned) {
		this.isPinned = isPinned;
		return this;
	}

	public boolean isInTrash() {
		return inTrash;
	}

	public void setInTrash(boolean inTrash) {
		this.inTrash = inTrash;
	}

	public Timestamp getCreatedTime() {
		return createdTime;
	}

	public Note setCreatedTime(Timestamp createdTime) {
		this.createdTime = createdTime;
		return this;
	}

	public Timestamp getUpdatedTime() {
		return updatedTime;
	}

	public Note setUpdatedTime(Timestamp updatedTime) {
		this.updatedTime = updatedTime;
		return this;
	}

	@Override
	public String toString() {
		return "Note [noteId=" + noteId + ", title=" + title + ", description=" + description + ", createdTime="
				+ createdTime + ", updatedTime=" + updatedTime + ", isArchive=" + isArchive + ", isPinned=" + isPinned
				+ ", inTrash=" + inTrash + ", userId=" + userId + "]";
	}

}
