package main.java.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;

import main.java.models.PDFCommand;

public class PDFCreatorService {

	private static PDFCreatorService service;

	// TODO: Implement dependency injection to avoid use of singleton
	public static PDFCreatorService getService() {
		if (service == null) {
			service = new PDFCreatorService();
		}

		return service;
	}

	private Map<String, PDFCommand> pdfCommands;

	private PDFCreatorService() {
		pdfCommands = new HashMap<String, PDFCommand>();

		for (PDFCommand pdfCommand : PDFCommand.values()) {
			pdfCommands.put(pdfCommand.toString(), pdfCommand);
		}
	}

	public Map<String, PDFCommand> getCommands() {
		return pdfCommands;
	}

	public PDFCommand getCommandForName(String commandName) {
		return pdfCommands.get(commandName.trim());
	}

	public boolean isCommand(String string) {
		return string.charAt(0) == '.';
	}

	public boolean isIndentSyntaxValid(String indentCommand) {
		String[] indentCommandParameters = indentCommand.split(" ");

		boolean isIndentSyntaxValid = indentCommandParameters.length > 1;

		if (isIndentSyntaxValid) {
			isIndentSyntaxValid = pdfCommands.get(indentCommandParameters[0]).equals(PDFCommand.INDENT);

			if (isIndentSyntaxValid) {
				isIndentSyntaxValid = (indentCommandParameters[1].charAt(0) == '+' || indentCommandParameters[1].charAt(0) == '-') && isNumeric(indentCommandParameters[1].substring(1).trim());
			}
		}

		return isIndentSyntaxValid;
	}

	private int getIndentValue(String indentCommand) {
		String[] indentCommandParameters = indentCommand.split(" ");

		return Integer.parseInt(indentCommandParameters[1].trim());
	}

	// TODO: Use a proper StringUtils
	private static boolean isNumeric(String string) {
		boolean isAlphanumeric = false;

		for (char character : string.toCharArray()) {
			isAlphanumeric = Character.isDigit(character);
			if (!isAlphanumeric) {
				break;
			}
		}

		return isAlphanumeric;
	}

	public void generatePDFWithInstructions(String filePath, String instructions) {
		generatePDFWithInstructions(filePath, new ArrayList<String>(Arrays.asList(instructions.split("\n"))));
	}

	public void generatePDFWithInstructions(String filePath, List<String> instructions) {
		try {
			PdfDocument pdfDocument = new PdfDocument(new PdfWriter(filePath));
			Document document = new Document(pdfDocument);

			boolean isBold = false;
			boolean isItalic = false;
			boolean isLarge = false;

			int indent = 0;

			PdfFont defaultFont = PdfFontFactory.createFont(StandardFonts.TIMES_ROMAN);
			PdfFont boldFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLD);
			PdfFont italicFont = PdfFontFactory.createFont(StandardFonts.TIMES_ITALIC);
			PdfFont boldItalicFont = PdfFontFactory.createFont(StandardFonts.TIMES_BOLDITALIC);

			PdfFont activeFont = defaultFont;
			TextAlignment activeAlignment = TextAlignment.LEFT;

			Paragraph lastParagraph = new Paragraph();

			for (String instruction : instructions) {
				if (isCommand(instruction)) {
					PDFCommand command = null;

					// TODO: Create an InvalidCommandSyntaxException
					if (isIndentSyntaxValid(instruction)) {
						command = getCommandForName(instruction.split(" ")[0]);
					} else {
						command = getCommandForName(instruction);
					}

					// TODO: Create an InvalidCommandSyntaxException
					switch (command) {
					case BOLD:
						isBold = true;

						if (isItalic) {
							activeFont = boldItalicFont;
						} else {
							activeFont = boldFont;
						}

						break;
					case FILL:
						activeAlignment = TextAlignment.JUSTIFIED_ALL;
						break;
					case INDENT:
						indent += getIndentValue(instruction) * 15;
						lastParagraph.setMarginLeft(indent);
						break;
					case ITALIC:
						isItalic = true;

						if (isBold) {
							activeFont = boldItalicFont;
						} else {
							activeFont = italicFont;
						}

						break;
					case LARGE:
						isLarge = true;
						break;
					case NOFILL:
						activeAlignment = TextAlignment.LEFT;
						break;
					case NORMAL:
						isLarge = false;
						break;
					case PARAGRAPH:
						document.add(lastParagraph);
						lastParagraph = new Paragraph();
						lastParagraph.setMarginLeft(indent);
						break;
					case REGULAR:
						isBold = false;
						isItalic = false;
						activeFont = defaultFont;
						break;
					}
				} else {
					Text text = null;
					if (isLarge) {
						text = new Text(instruction.trim() + " ").setFont(activeFont).setTextAlignment(activeAlignment).setFontSize(24);
					} else {
						text = new Text(instruction.trim() + " ").setFont(activeFont).setTextAlignment(activeAlignment).setFontSize(12);
					}
					lastParagraph.add(text);
				}
			}

			document.add(lastParagraph);

			document.close();
			pdfDocument.close();
		} catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace(System.err);
		} catch (IOException ioe) {
			ioe.printStackTrace(System.err);
		}
	}

}