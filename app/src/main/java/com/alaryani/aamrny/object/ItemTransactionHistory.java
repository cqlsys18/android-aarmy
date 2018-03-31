package com.alaryani.aamrny.object;

public class ItemTransactionHistory {
	private String transactionId;
	private String dateTimeTransaction;
	private String noteTransaction;
	private String typeTransaction;
	private String pointTransaction;
	private String tripId;
	
	
	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getPointTransaction() {
		return pointTransaction;
	}

	public void setPointTransaction(String pointTransaction) {
		this.pointTransaction = pointTransaction;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getDateTimeTransaction() {
		return dateTimeTransaction;
	}

	public void setDateTimeTransaction(String dateTimeTransaction) {
		this.dateTimeTransaction = dateTimeTransaction;
	}

	public String getNoteTransaction() {
		return noteTransaction;
	}

	public void setNoteTransaction(String noteTransaction) {
		this.noteTransaction = noteTransaction;
	}

	public String getTypeTransaction() {
		return typeTransaction;
	}

	public void setTypeTransaction(String typeTransaction) {
		this.typeTransaction = typeTransaction;
	}

}
