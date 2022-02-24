/* Maanik Gogna
 * Word Frequency Assignment
 * May 24, 2021
 * 
 * Program finds the most frequent words from texts and lists them in a GUI. The texts are inputted by the user through file format and are read in by the program. 
 */

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import javax.swing.*;
import javax.swing.text.*;
public class WordFrequencyAssignmentMain extends JPanel implements ActionListener {

	//Variable initialization
	static ArrayList<String> fileNames = new ArrayList<String>();
	static JComboBox<String> fileOptions = new JComboBox<String>();
	static JTextPane displayText = new JTextPane();
	static StyledDocument formattedDisplayText = displayText.getStyledDocument();
	static JTextPane writtenPieceText = new JTextPane();
	static WordFrequencyAssignmentMain assignmentPanel = new WordFrequencyAssignmentMain();
	static JFrame assignmentFrame = new JFrame("Word Frequency Assignment");
	static long totalTime = 0;

	//Constructor for panel
	public WordFrequencyAssignmentMain() {
		//Adding button
		JButton addFileButton = new JButton("Add Another File");
		add(addFileButton);
		addFileButton.addActionListener(this);
		addFileButton.setActionCommand("add file");
		addFileButton.setPreferredSize(new Dimension(250, 60));
		addFileButton.setFont(new Font(Font.SERIF, Font.BOLD, 20));
		addFileButton.setVisible(true);

		Collections.sort(fileNames);
		
		//Adding the dropdown menu and the two text fields
		fileOptions.setPreferredSize(new Dimension(300, 30));
		fileOptions.setFont(new Font(Font.SANS_SERIF, Font.ITALIC, 15));

		writtenPieceText.setPreferredSize(new Dimension(475, 575));
		writtenPieceText.setEditable(false);
		writtenPieceText.setParagraphAttributes(new SimpleAttributeSet(), true);

		displayText.setPreferredSize(new Dimension(475, 575));
		displayText.setFont(new Font(Font.MONOSPACED, Font.ROMAN_BASELINE, 13));
		displayText.setEditable(false);

		//Updating the text fields whenever the dropdown menu is updated or changed
		fileOptions.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				BufferedReader fileReader = null;
				String totalText = "";
				try {
					//Displaying the user's full text
					fileReader = new BufferedReader(new FileReader((String)fileOptions.getSelectedItem()));
					totalText= fileReader.lines().collect(Collectors.joining(System.lineSeparator()));
					writtenPieceText.setText(totalText);
					
					readFile((String)fileOptions.getSelectedItem());
				} catch (FileNotFoundException e) {
				} catch (IOException e) {
				}
			}			
		});

		//Adding a scrollbar to the text field that displays the user's text
		JScrollPane textScroll = new JScrollPane(writtenPieceText, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		textScroll.setPreferredSize(new Dimension(475, 575));
		textScroll.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 10));
		add(fileOptions);

		add(textScroll);
		add(displayText);
		setPreferredSize(new Dimension(1000, 700));
		setVisible(true);
	}

	public static void main(String[] args) throws IOException {
		//Main class
		fileNames.add("alice.txt");
		fileNames.add("moby.txt");
		Collections.sort(fileNames);

		for (int count = 0; count < fileNames.size(); count++)
			fileOptions.addItem(fileNames.get(count));

		assignmentFrame.add(assignmentPanel);
		assignmentFrame.pack();
		assignmentFrame.setResizable(false);
		assignmentFrame.setVisible(true);
	}

	public static void readFile(String fileName) throws IOException{
		/* Purpose: reads in a file and yields a map containing the frequency of every word in the text. Also used to display the text on the GUI and output the top 20 words.
		 * Params: the name of the file which the user's text is in
		 * Returns void
		 */

		//Reading in the file and adding words to the map
		String currLine = "";
		long startTime = System.currentTimeMillis();
		BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
		while (currLine!=null) {
			currLine = fileReader.readLine();
			if (currLine == null)
				break;
			WordFrequencyAssignment_WordClass.findWords(currLine.toLowerCase().trim());
		}
		totalTime = System.currentTimeMillis()-startTime;
		System.out.println(totalTime);

		SimpleAttributeSet left = new SimpleAttributeSet(), center = new SimpleAttributeSet(), right = new SimpleAttributeSet();
		StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
		StyleConstants.setAlignment(center, StyleConstants.ALIGN_CENTER);
		StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);

		displayText.setText("");
		//Outputing the top 20 words of the text by frequency
		try {
			formattedDisplayText.insertString(formattedDisplayText.getLength(), String.format("TOTAL TIME: %d milliseconds", totalTime), center);
			formattedDisplayText.setParagraphAttributes(formattedDisplayText.getLength(), 1, center, false);
			formattedDisplayText.insertString(formattedDisplayText.getLength(), String.format("%n20 most frequent words:%n%n", totalTime), center);
			formattedDisplayText.setParagraphAttributes(formattedDisplayText.getLength(), 1, center, false);

			formattedDisplayText.insertString(formattedDisplayText.getLength(), String.format("%10s%40s%n", "Words", "Frequency"), left);
			formattedDisplayText.setParagraphAttributes(formattedDisplayText.getLength(), 1, left, false);

			Iterator<String> iterator = WordFrequencyAssignment_WordClass.getWordMap().keySet().iterator();
			for (int count = 1; count <= 20; count++) {
				String currElement = iterator.next();
				formattedDisplayText.insertString(formattedDisplayText.getLength(), String.format("%6s%4s%-36s%d%n", count + ".", "", currElement, WordFrequencyAssignment_WordClass.getWordMap().get(currElement)), left);
				formattedDisplayText.setParagraphAttributes(formattedDisplayText.getLength(), 1, left, false);
			}
			WordFrequencyAssignment_WordClass.voidWordMap();
		}catch (Exception e) {};
	}

	public void actionPerformed(ActionEvent event) {
		/* Purpose: 'listens' for whenever an action is performed, specifically, when a button is clicked
		 * Params: the event associated with the action performed
		 * Returns void
		 */
		
		//Whenever the 'add file' button is clicked
		if (event.getActionCommand().equals("add file")) {
			boolean goAgain = false;
			String fileName = "";
			do {
				try {
					if (!goAgain) 
						fileName = JOptionPane.showInputDialog(this, "Please type in the name of the file you would like to analyze. The input will be case insensitive, but it will not be trimmed. \nDo not forget any extensions, such as '.txt'.", "FILE NAME INPUT", JOptionPane.QUESTION_MESSAGE);
					else 
						if (fileName.length()==0)
							fileName = JOptionPane.showInputDialog(this, "You did not enter anything into the dialog box. Please try again.", "NO INPUT DETECTED", JOptionPane.WARNING_MESSAGE);
						else
							fileName = JOptionPane.showInputDialog(this, "Sorry, that file does not exist, please try again.\nDo not forget any extensions, such as '.txt'.", "FILE NOT FOUND", JOptionPane.ERROR_MESSAGE);
					if (fileName!=null) {
						BufferedReader tempReader = new BufferedReader(new FileReader(fileName));
						System.out.println(Collections.binarySearch(fileNames, fileName));
						if ((Collections.binarySearch(fileNames, fileName, String.CASE_INSENSITIVE_ORDER))<=-1) {
							fileNames.add(fileName.toLowerCase());
							Collections.sort(fileNames);
							fileOptions.removeAllItems();
							for (int count = 0; count < fileNames.size(); count++)
								fileOptions.addItem(fileNames.get(count));
							fileOptions.setSelectedItem(fileName.toLowerCase());
						}else 
							JOptionPane.showMessageDialog(this, "You have already added this file to the database.", "FILE ALREADY ENTERED", JOptionPane.WARNING_MESSAGE);
					}
					goAgain = false;
				}catch (FileNotFoundException e) {
					goAgain = true;
				}catch (IOException e) {
					JOptionPane.showMessageDialog(this, "Sorry, something went wrong while trying to read your file.", "ERROR", JOptionPane.OK_OPTION);
					goAgain = true;
				}
			}while (goAgain);
		}
	}
}
