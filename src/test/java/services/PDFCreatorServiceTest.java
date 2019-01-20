package test.java.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import main.java.models.PDFCommand;
import main.java.services.PDFCreatorService;

class PDFCreatorServiceTest {
	
	boolean commandExists(String command) {
		boolean commandExists;
		PDFCommand pdfCommand = PDFCreatorService.getService().getCommandForName(command);
		
		if (pdfCommand == null) {
			commandExists = false;
			System.out.println(command + " does not exist.");
		} else {
			commandExists = true;
			System.out.println(command + " was found.");
		}
		
		return commandExists;
	}
	
	@Test
	void stringIsCommand() {
		// 1. Actors
		boolean isCommand;
		String command = ".bold";
		
		// 2. Action
		isCommand = PDFCreatorService.getService().isCommand(command);
		
		// 3. Asserts
		assertTrue(isCommand);
	}
	
	@Test
	void stringIsNotCommand() {
		// 1. Actors
		boolean isCommand;
		String wrongCommand = "bold";
		
		// 2. Action
		isCommand = PDFCreatorService.getService().isCommand(wrongCommand);
		
		// 3. Asserts
		assertFalse(isCommand);
	}

    @Test
    void recognizedCommands_Should_Work() {
        // 1. Actors
    	boolean commandNotFound = false;
    	String[] commands = {
    			".bold",
    			".fill",
    			".indent",
    			".italic",
    			".large",
    			".nofill",
    			".normal",
    			".paragraph",
    			".regular",
    	};

        // 2. Action
    	for (String command : commands) {
    		commandNotFound = !commandExists(command);
    		
    		if (commandNotFound) {
    			break;
    		}
    	}

        // 3. Asserts
    	assertFalse(commandNotFound);
    }
    
    @Test
    void unrecognizedCommands_ShouldNot_Work() {
        // 1. Actors
    	boolean commandNotFound = false;
    	String[] commands = {
    			".bold",
    			".fill",
    			".indent",
    			".italics",
    			".large",
    			".nofill",
    			".normal",
    			".paragraph",
    			".regular",
    	};

        // 2. Action
    	for (String command : commands) {
    		commandNotFound = !commandExists(command);

    		if (commandNotFound) {
    			break;
    		}
    	}

        // 3. Asserts
    	assertTrue(commandNotFound);
    }

    @Test
    void indentWithSpecifiedAmount_Should_Work() {
    	// 1. Actors
    	boolean isIndentSyntaxValid;
    	String indentCommandWithUnit = ".indent +4";

    	// 2. Action
    	isIndentSyntaxValid = PDFCreatorService.getService().isIndentSyntaxValid(indentCommandWithUnit);

    	// 3. Asserts
    	assertTrue(isIndentSyntaxValid);
    }

    @Test
    void indentWithoutSpecifiedAmount_ShouldNot_Work() {
    	// 1. Actors
    	boolean isIndentSyntaxValid;
    	String indentCommandWithUnit = ".indent";

    	// 2. Action
    	isIndentSyntaxValid = PDFCreatorService.getService().isIndentSyntaxValid(indentCommandWithUnit);

    	// 3. Asserts
    	assertFalse(isIndentSyntaxValid);
    }

    @Test
    void indentWithoutSymbol_ShouldNot_Work() {
    	// 1. Actors
    	boolean isIndentSyntaxValid;
    	String indentCommandWithUnit = ".indent 5";

    	// 2. Action
    	isIndentSyntaxValid = PDFCreatorService.getService().isIndentSyntaxValid(indentCommandWithUnit);

    	// 3. Asserts
    	assertFalse(isIndentSyntaxValid);
    }

    @Test
    void indentWithWrongSymbol_ShouldNot_Work() {
    	// 1. Actors
    	boolean isIndentSyntaxValid;
    	String indentCommandWithUnit = ".indent :5";

    	// 2. Action
    	isIndentSyntaxValid = PDFCreatorService.getService().isIndentSyntaxValid(indentCommandWithUnit);

    	// 3. Asserts
    	assertFalse(isIndentSyntaxValid);
    }

    @Test
    void indentWithSpaceBetweenSymbolAndNumber_ShouldNot_Work() {
    	// 1. Actors
    	boolean isIndentSyntaxValid;
    	String indentCommandWithUnit = ".indent + 5";

    	// 2. Action
    	isIndentSyntaxValid = PDFCreatorService.getService().isIndentSyntaxValid(indentCommandWithUnit);

    	// 3. Asserts
    	assertFalse(isIndentSyntaxValid);
    }

    @Test
    void indentWithBigNumber_Should_Work() {
    	// 1. Actors
    	boolean isIndentSyntaxValid;
    	String indentCommandWithUnit = ".indent +2147483647";

    	// 2. Action
    	isIndentSyntaxValid = PDFCreatorService.getService().isIndentSyntaxValid(indentCommandWithUnit);

    	// 3. Asserts
    	assertTrue(isIndentSyntaxValid);
    }
    
    @Test
    void generateAssessmentPDF_Should_GeneratePDFFile() {
    	// 1. Actors
    	String filePath = "generatedAssessmentPDF.pdf";
    	String instructions =
    			".large\r\n" + 
    			"My PDF Document\r\n" + 
    			".normal\r\n" + 
    			".paragraph\r\n" + 
    			"This is my\r\n" + 
    			".italic\r\n" + 
    			"very first\r\n" + 
    			".regular\r\n" + 
    			"pdf document, and the output is formatted really well. While this paragraph is not filled, the following one has automatic filling set:\r\n" + 
    			".paragraph\r\n" + 
    			".indent +2\r\n" + 
    			".fill\r\n" + 
    			"“Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.”\r\n" + 
    			".paragraph\r\n" + 
    			".nofill\r\n" + 
    			".indent -2\r\n" + 
    			"Well that was\r\n" + 
    			".bold\r\n" + 
    			"exciting\r\n" + 
    			".regular\r\n" + 
    			", good luck!";
    	
    	// 2. Action
    	PDFCreatorService.getService().generatePDFWithInstructions(filePath, instructions);
    }
    
    @Test
    void generateRepeatedAssessmentPDF_Should_GenerateMultiplePagesPDFFile() {
    	// 1. Actors
    	String filePath = "generatedRepeatedAssessmentPDF.pdf";
    	String instructions =
    			".large\r\n" + 
    			"My PDF Document\r\n" + 
    			".normal\r\n" + 
    			".paragraph\r\n" + 
    			"This is my\r\n" + 
    			".italic\r\n" + 
    			"very first\r\n" + 
    			".regular\r\n" + 
    			"pdf document, and the output is formatted really well. While this paragraph is not filled, the following one has automatic filling set:\r\n" + 
    			".paragraph\r\n" + 
    			".indent +2\r\n" + 
    			".fill\r\n" + 
    			"“Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.”\r\n" + 
    			".paragraph\r\n" + 
    			".nofill\r\n" + 
    			".indent -2\r\n" + 
    			"Well that was\r\n" + 
    			".bold\r\n" + 
    			"exciting\r\n" + 
    			".regular\r\n" + 
    			", good luck!\r\n" +
    			".paragraph\r\n";
    	instructions += instructions; // 2 times the instructions
    	instructions += instructions; // 4 times the instructions
    	instructions += instructions; // 8 times the instructions
    	instructions += instructions; // 16 times the instructions
    	
    	// 2. Action
    	PDFCreatorService.getService().generatePDFWithInstructions(filePath, instructions);
    }
    
    @Test
    void generateAssessmentInstructionPDF_Should_GenerateFileSimilarToAssessmentPDF() {
    	// 1. Actors
    	String filePath = "Generated gearset PDF creator assessment.pdf";
    	String instructions =
    			".large\r\n" + 
    			"PDF creator\r\n" + 
    			".paragraph\r\n" + 
    			"The challenge\r\n" + 
    			".normal\r\n" + 
    			".paragraph\r\n" + 
    			".indent +1\r\n" +
    			"For this assessment, we'd like you to produce some C# or Java which takes input from a text file and produces a PDF output file. We'd recommend that you produce your solution in Visual Studio, Visual Studio Code, or IntelliJ. You can use third party libraries in your code to help wherever needed - for example, iText for C# and iText for Java are good options for helping create a PDF document.\r\n" +
    			".paragraph\r\n" +
    			"The input file will contain a series of commands and text which describe what text should be in the output file and how it should be formated.\r\n" +
    			".paragraph\r\n" + 
    			"Example input:\r\n" +
    			".paragraph\r\n" + 
    			".indent +2\r\n" + 
    			" .large\r\n" + 
    			".paragraph\r\n" + 
    			"My PDF Document\r\n" + 
    			".paragraph\r\n" + 
    			" .normal\r\n" + 
    			".paragraph\r\n" + 
    			" .paragraph\r\n" + 
    			".paragraph\r\n" + 
    			"This is my\r\n" + 
    			".paragraph\r\n" + 
    			" .italic\r\n" + 
    			".paragraph\r\n" + 
    			"very first\r\n" + 
    			".paragraph\r\n" + 
    			" .regular\r\n" + 
    			".paragraph\r\n" + 
    			"pdf document, and the output is formatted really well. While this paragraph is not filled, the following one has automatic filling set:\r\n" + 
    			".paragraph\r\n" + 
    			" .paragraph\r\n" + 
    			".paragraph\r\n" + 
    			" .indent +2\r\n" + 
    			".paragraph\r\n" + 
    			" .fill\r\n" + 
    			".paragraph\r\n" + 
    			"“Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.”\r\n" + 
    			".paragraph\r\n" + 
    			" .paragraph\r\n" + 
    			".paragraph\r\n" + 
    			" .nofill\r\n" + 
    			".paragraph\r\n" + 
    			" .indent -2\r\n" + 
    			".paragraph\r\n" + 
    			"Well that was\r\n" + 
    			".paragraph\r\n" + 
    			" .bold\r\n" + 
    			".paragraph\r\n" + 
    			"exciting\r\n" + 
    			".paragraph\r\n" + 
    			" .regular\r\n" + 
    			".paragraph\r\n" + 
    			", good luck!\r\n" + 
    			".paragraph\r\n" + 
    			".paragraph\r\n" +
    			".paragraph\r\n" +  
    			".indent -2\r\n" + 
    			"The above input text should be rendered roughly as follows:\r\n" + 
    			".paragraph\r\n" + 
    			".indent +2\r\n" + 
    			".large\r\n" + 
    			"My PDF Document\r\n" + 
    			".normal\r\n" + 
    			".paragraph\r\n" + 
    			"This is my\r\n" + 
    			".italic\r\n" + 
    			"very first\r\n" + 
    			".regular\r\n" + 
    			"pdf document, and the output is formatted really well. While this paragraph is not filled, the following one has automatic filling set:\r\n" + 
    			".paragraph\r\n" + 
    			".indent +2\r\n" + 
    			".fill\r\n" + 
    			"“Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.”\r\n" + 
    			".paragraph\r\n" + 
    			".nofill\r\n" + 
    			".indent -2\r\n" + 
    			"Well that was\r\n" + 
    			".bold\r\n" + 
    			"exciting\r\n" + 
    			".regular\r\n" + 
    			", good luck!\r\n" +
    			".paragraph\r\n" + 
    			".paragraph\r\n" + 
    			".indent -2\r\n" + 
    			"In your solution you should repeat the above text over and over to make sure that it includes pagination, and should render at least 3 pages of content.\r\n" +
    			".paragraph\r\n" + 
    			"The commands are:\r\n" + 
    			".paragraph\r\n" + 
    			".indent +1\r\n" + 
    			"• \r\n" + 
    			".bold\r\n" + 
    			" .paragraph: \r\n" + 
    			".regular\r\n" + 
    			"starts a new paragraph\r\n" + 
    			".paragraph\r\n" + 
    			"• \r\n" + 
    			".bold\r\n" + 
    			" .fill: \r\n" + 
    			".regular\r\n" + 
    			"sets indentation to fill for paragraphs, where the last character of a line must end at the end of the margin (except for the last line of a paragraph)\r\n" + 
    			".paragraph\r\n" + 
    			"• \r\n" + 
    			".bold\r\n" + 
    			" .nofill: \r\n" + 
    			".regular\r\n" + 
    			"the default, sets the formatter to regular formatting\r\n" + 
    			".paragraph\r\n" + 
    			"• \r\n" + 
    			".bold\r\n" + 
    			" .regular: \r\n" + 
    			".regular\r\n" + 
    			"resets the font to the normal font\r\n" + 
    			".paragraph\r\n" + 
    			"• \r\n" + 
    			".bold\r\n" + 
    			" .italic: \r\n" + 
    			".regular\r\n" + 
    			"sets the font to italic\r\n" + 
    			".paragraph\r\n" + 
    			"• \r\n" + 
    			".bold\r\n" + 
    			" .bold: \r\n" + 
    			".regular\r\n" + 
    			"sets the font to bold\r\n" + 
    			".paragraph\r\n" + 
    			"• \r\n" + 
    			".bold\r\n" + 
    			" .indent <number>: \r\n" + 
    			".regular\r\n" + 
    			"indents the specified amount (each unit is probably about the length of the string \"WWWW\", but other values would work)\r\n" + 
    			".indent -2\r\n" + 
    			".paragraph\r\n" + 
    			".large\r\n" + 
    			"NOTES\r\n" + 
    			".normal\r\n" + 
    			".indent +1\r\n" + 
    			".paragraph\r\n" + 
    			"At Gearset, we strive for good design and code that's well unit tested, or at least code that is written in a testable way.\r\n" + 
    			".bold\r\n" + 
    			"We'll be closely assessing the quality of the code you've ritten in your solution.\r\n" + 
    			".regular\r\n" + 
    			"This means we'll be expecting to see code which is easy for another developer to read through and digest; which has sensible abstractions within it by splitting out classes and methods with distinct responsibilities; with well named classes, methods and variables.\r\n" + 
    			".paragraph\r\n" + 
    			"Not got time to implement the refactorings you'd like to make your code as good as you'd like? Tell us about it! Are there some bits of your code that you're not particularly proud of but can't find a better approach? Tell us about it! As software engineers we have to make trade-offs between \r\n" + 
    			".italic\r\n" + 
    			"time to get a job done\r\n" + 
    			".regular\r\n" + 
    			"and\r\n" + 
    			".italic\r\n" + 
    			"perfect code design\r\n" + 
    			"all the time.\r\n" + 
    			".regular\r\n" +
    			"Identifying the trade-offs we're making is important.\r\n" + 
    			".paragraph\r\n" + 
    			"Getting a good solution should take around half a day's work assuming you don't hit any significant roadblocks and depending on how many similar sorts of problem like this you've solved before.\r\n" + 
    			".indent -1\r\n" + 
    			".paragraph\r\n" + 
    			".large\r\n" + 
    			"What to submit\r\n" + 
    			".normal\r\n" + 
    			".indent +1\r\n" + 
    			".paragraph\r\n" + 
    			"Send your solution and anything else useful for reviewing your solution - for example a supporting document detailing any assumptions you've made - over to us in a\r\n" + 
    			".bold\r\n" + 
    			" .zip\r\n" + 
    			".regular\r\n" + 
    			"archive. Your solution needs to be easy for us to load up in an IDE, build, and run - if it isn't then that'll make your solution more difficult to assess!\r\n" + 
    			".paragraph\r\n" + 
    			"Ideally you'll send us a Visual Studio, Visual Studio Code, or IntelliJ project which we can load up and that reads from a text file which you've provided with your solution and then writes a new pdf to disk. We should then be able to open the PDF and see the output as we described above.\r\n";
    	
    	// 2. Action
    	PDFCreatorService.getService().generatePDFWithInstructions(filePath, instructions);
    }

}