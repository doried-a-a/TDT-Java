package nlp;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class StopWordsRemover {
	
	private String stopwordsPath = "stopwordsList.txt";
	Set<String> stopWords ;
	
	public StopWordsRemover() throws Exception{
		stopWords = new HashSet<String>();
		Scanner sc = new Scanner(new File(stopwordsPath));
		while(sc.hasNextLine()){
			String word = sc.nextLine().trim();
			stopWords.add(word);
		}
		System.out.println(stopWords.size());
	}
	
	public boolean isStopWord(String word){
		return stopWords.contains(word.trim());
	}
}
