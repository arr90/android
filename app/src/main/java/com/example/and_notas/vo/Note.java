package com.example.and_notas.vo;

import java.io.Serializable;
import java.util.Date;

public class Note implements Serializable{

	private long id;
	private String note;
	private Date createDate;

	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}

	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}

	public Date getCreateDate() {return createDate;}
	public void setCreateDate(Date createDate) {this.createDate = createDate;}

	@Override
	public String toString() {
		return note;
	}

}