import java.util.*;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class WordFrequencyAssignment_WordClass{ 
	private static Map <String, Integer> wordMap = new HashMap<>();

	public static void findWords(String currLine) {
		int index = 0;
		String symbols[] = { "\"" , "~", "!", "@", "#", "$", "%", "^", "&", "*", "(", ")", 
				//							"-", "`", 
				"_", "\\\\", "|", "{", "}", "[", "]", ";", ":", ",", ".", "?", "/"};// ' ommited
		
		//Didn't have time to do this
		//ANOTHER SPLIT COULD BE USED FOR -- or for all special chars excluding the three specified below
		//		if (currLine.length()!=0) {

		for (int count = 0; count < currLine.length(); count++) {
			//Converting double dashes to spaces(this was found in Alice text)
			if (count + 2 <= currLine.length()) {
				if (currLine.substring(count, count+2).equals("--")) 
					currLine = currLine.substring(0, count) + " " + currLine.substring(count+2);
				else if (currLine.substring(count, count+2).equals("'s") && 
						(count-3 > 0 && !(currLine.substring(count-3, count).equals(" he") || currLine.substring(count-3, count).equals(" it") || currLine.substring(count-3,count).equals("she")))) { 
					currLine = currLine.substring(0, count) + currLine.substring(count+2);
					count-=2;
				}
			}
			//if any of the above symbols in the array are found anywhere in the line, i remove them
			for (int letterCount = 0; letterCount < symbols.length; letterCount++) {
				if (Character.toString(currLine.charAt(count)).equals(symbols[letterCount])) {
					currLine = currLine.substring(0, count) + currLine.substring(count+1);
					count--;
					break;
				}
			}
		}

		//Eliminating leading and trailing quote marks, apostrophes, and dashes
		currLine = " " + currLine.trim() + " ";
		while ((currLine.length()!=0) && 
				(currLine.indexOf(" '")!= -1 || currLine.indexOf("' ")!= -1 ||
				currLine.indexOf(" -")!= -1 || currLine.indexOf("- ")!= -1 || 
				currLine.indexOf(" `")!= -1 || currLine.indexOf("` ")!= -1)) {

			if (currLine.indexOf(" '")!= -1 || currLine.indexOf(" -")!=-1 || currLine.indexOf(" `")!=-1) {
				index = currLine.indexOf(" '");
				if (index==-1) {
					index = currLine.indexOf(" -");
					if (index==-1)
						index = currLine.indexOf(" `");
				}
				currLine = currLine.substring(0, index+1) + currLine.substring(index+2);
			}else {
				index = currLine.indexOf("' ");
				if (index==-1) {
					index = currLine.indexOf("- ");
					if (index == -1)
						index = currLine.indexOf("` ");
				}
				currLine = currLine.substring(0, index) + currLine.substring(index+1);

			}
		}
		currLine = currLine.trim();
		String [] currWords = currLine.split(" ");
		
		for (int count = 0; count < currWords.length; count++) {
			currWords[count] = currWords[count].trim();
			//If the line is empty
			if (currWords[count].length()==0) 
				break;
			for (int subCount = 0; subCount < currWords[count].length(); subCount++)
				//If any word had a number, i assumed that was an invalid string
				if (Character.isDigit(currWords[count].charAt(subCount)))
					break;
				else if (subCount + 1 == currWords[count].length() && currWords[count].length()!=0) { 
					int wordMapVal;
					if (!wordMap.containsKey(currWords[count])) //If the word is not in the map already
						wordMapVal = 1;
					else //If the word is already in the map
						wordMapVal = wordMap.get(currWords[count]) + 1;
					wordMap.put(currWords[count], wordMapVal);
				}
		}

	}

	//Getter/Setter methods
	public static Map<String, Integer> getWordMap() {
		//Algorithm retrieved from internet - sorts the map by the values of the values, and then i reverse the order so the elements are sorted by their values in descending order(largest values first)
		wordMap = wordMap.entrySet().stream().sorted(Collections.reverseOrder(Entry.comparingByValue())).collect(Collectors.toMap(Entry::getKey, Entry::getValue,
				(e1, e2) -> e2, LinkedHashMap::new));
		return wordMap;
	}
	
	public static void voidWordMap() {//Used to clear the contents of the map
		wordMap.clear();;
	}

	//Didn't use this method
}
