package main.java.models;

public enum PDFCommand {
	
	BOLD(".bold"),
	FILL(".fill"),
	INDENT(".indent"),
	LARGE(".large"),
	ITALIC(".italic"),
	NOFILL(".nofill"),
	NORMAL(".normal"),
	PARAGRAPH(".paragraph"),
	REGULAR(".regular");
	
	private String command;
	
	PDFCommand(String command) {
		this.command = command;
	}
	
	@Override
	public String toString() {
		return command;
	}

}