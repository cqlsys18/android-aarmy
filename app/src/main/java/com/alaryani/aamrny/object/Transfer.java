package com.alaryani.aamrny.object;

public class Transfer {

	private Double amount;
	private String receiverEmail;
	private String receiverName;
	private String receiverProfile;
	private String receiverGender;
	private String note;

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverProfile() {
		return receiverProfile;
	}

	public void setReceiverProfile(String receiverProfile) {
		this.receiverProfile = receiverProfile;
	}

	public String getReceiverGender() {
		return receiverGender;
	}

	public void setReceiverGender(String receiverGender) {
		this.receiverGender = receiverGender;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

}
