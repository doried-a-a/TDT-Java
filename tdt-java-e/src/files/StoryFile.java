package files;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URI;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class StoryFile {
	/**
	 * Tag maybe used by us to determine which story belongs to which topic.
	 */
	private String storyTag;
	/**
	 * Title maybe the title provided by the site the story was fetched from
	 */
	private String storyTitle;
	
	private String storyUrl;
	private Date storyDate;
	public String getStoryTag() {
		return storyTag;
	}

	public void setStoryTag(String storyTag) {
		this.storyTag = storyTag;
	}

	public String getStoryTitle() {
		return storyTitle;
	}

	public void setStoryTitle(String storyTitle) {
		this.storyTitle = storyTitle;
	}

	public String getStoryUrl() {
		return storyUrl;
	}

	public void setStoryUrl(String storyUrl) {
		this.storyUrl = storyUrl;
	}

	public Date getStoryDate() {
		return storyDate;
	}
	
	public String getStoryDateAsString(){
    	SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = simpleDateFormat.format(this.getStoryDate());
        return date;
	}
	
	public void setStoryDate(Date storyDate) {
		this.storyDate = storyDate;
	}

	public String getStoryContent() {
		return storyContent;
	}

	public void setStoryContent(String storyContent) {
		this.storyContent = storyContent;
	}

	private String storyContent;
	
	public StoryFile(File file,String tag,boolean processText) throws Exception{
		this.storyTag = tag;
		
		Scanner sc = new Scanner(file);
    	
    	storyUrl = sc.nextLine();

    	storyTitle = sc.nextLine();
    	
    	String dateStr = sc.nextLine();
    	
        if (dateStr.contains("."))
        	dateStr = dateStr.substring(0, dateStr.indexOf('.'));
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        storyDate = simpleDateFormat.parse(dateStr);
    	
        storyContent="";
    	while (sc.hasNextLine()){
    		storyContent += sc.nextLine();
    	}
    	
    	if(processText)
    		storyContent = nlp.TextProcessor.processText(storyContent);
    	

    	if(storyUrl.contains("theaustralian"))
    		storyContent = "";
    	
    	sc.close();
    	
//    	String words[] = storyContent.split(" ");
//    	storyContent="";
//    	for(String word:words)
//    		if(word.length()<3)
//    			continue;
//    		else storyContent += word + " ";
//    	storyContent = storyContent.trim();
    	
	}
	
	public StoryFile(File file,String tag,boolean processText,int titleBoosting) throws Exception{
		this.storyTag = tag;
		
		Scanner sc = new Scanner(file);
    	
    	storyUrl = sc.nextLine();

    	storyTitle = sc.nextLine();
    	
    	String dateStr = sc.nextLine();
    	
        if (dateStr.contains("."))
        	dateStr = dateStr.substring(0, dateStr.indexOf('.'));
        
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        storyDate = simpleDateFormat.parse(dateStr);
    	
        storyContent="";
    	while (sc.hasNextLine()){
    		storyContent += sc.nextLine();
    	}
    	
    	if(processText)
    		storyContent = nlp.TextProcessor.processText(storyContent);
    	
    	String processedStoryTitle = nlp.TextProcessor.processText(storyTitle);
    	
    	while(titleBoosting-->0){
    		storyContent += processedStoryTitle;
    	}
    	
    	if(storyUrl.contains("theaustralian"))
    		storyContent = "";
    	
    	sc.close();

	}
}
