package files;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import nlp.TextProcessor;

public class DirectoryProcessor {

	public static String extractTopicTag(String storyFileName){
		return storyFileName.substring(0,storyFileName.indexOf('-'));
	}
	
	public static String extractTopic_StoryTag(String storyFileName){
		return storyFileName.substring(0,storyFileName.indexOf('.'));
	}
	
    public static List<File> getListOfFiles(String directoryPath) {
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        List<File> files = new ArrayList<File>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isFile()) {
                 files.add(listOfFiles[i]);
            }
        }
        return files;
    }
    
    
    /**
     * @throws Exception 
     */
    public static StoryFile readStoryFile(File file,boolean processText) throws Exception{
    	StoryFile sfile = new StoryFile(file, extractTopic_StoryTag(file.getName()),processText);
    	return sfile;
    }
    
    /**
     * @throws Exception 
     */
    public static StoryFile readStoryFile(File file,boolean processText,int titleBoosting) throws Exception{
    	StoryFile sfile = new StoryFile(file, extractTopic_StoryTag(file.getName()),processText,titleBoosting);
    	return sfile;
    }
    
    /**
     * Creates a topic, putting its tag as the directtory name (last directory name in its path). Ex: /home/doried/22 so topicTag=22
     */
    public static TopicFiles readTopicFiles(File topicDir,boolean processText) throws Exception{
    	
    	String topicDirStr = topicDir.getAbsolutePath();
    	if(topicDirStr.endsWith("/"))
    		topicDirStr = topicDirStr.substring(0,topicDirStr.length()-1);
    	
    	String topicTag = topicDirStr.substring(topicDirStr.lastIndexOf("/")+1);
    	
    	return readTopicFiles(topicDir, topicTag,processText); 
    }
    
   
    public static TopicFiles readTopicFiles(File topicDir,String topicTag,boolean processText) throws Exception{
    	
    	List<File> storyFiles = getListOfFiles(topicDir.getAbsolutePath());
    	
    	List<StoryFile> stories = new ArrayList<StoryFile>();
    	
    	for(File f:storyFiles){
    		StoryFile sFile = new StoryFile(f,extractTopic_StoryTag(f.getName()),processText);
    		
    		if (sFile.getStoryContent().split(" ").length < 200){
        		continue;
        	}
    		
    		stories.add(sFile);
    	}
    	
    	TopicFiles topicFiles = new TopicFiles(stories,topicTag);
    	return topicFiles;
    }
  
    public static List<File> getListOfDirectories(String directoryPath) {
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        List<File> dirs = new ArrayList<File>();
        for (int i = 0; i < listOfFiles.length; i++) {
            if (listOfFiles[i].isDirectory()) {
                 dirs.add(listOfFiles[i]);
            }
        }
        return dirs;
    }
    
    //TODO Need test
    static int skipped=0;
    public static void copyTopicFilesToOneDirectory() throws Exception{
    	skipped=0;
        String topicsPath = "/home/doried/tdt/data/test2/text/";
        String outputPath = "/home/doried/tdt/test/all/";
        
        List<File> topicDirs = getListOfDirectories(topicsPath);
        
        
        for (File topicDir : topicDirs){
            List<File> topicFiles = getListOfFiles(topicDir.getAbsolutePath());
            
            for(File storyFile : topicFiles){
            	StoryFile f = readStoryFile(storyFile,false);
            	String processedContent = TextProcessor.processText(f.getStoryContent());
            	if (processedContent.split(" ").length < 200){
            		System.out.println(++skipped);
            		continue;
            	}
                 
                String output = f.getStoryUrl() + "\n" + f.getStoryTitle() + "\n" + f.getStoryDateAsString() + "\n" + processedContent;

                String output_story_name = topicDir.getName() + "-" + storyFile.getName();
                PrintWriter wr = new PrintWriter(new File(outputPath + output_story_name ));
                wr.write(output);
                wr.close();
            }
        }    
    }
    
    
    public static void copyTopicsAndTextProcessThem() throws Exception{
    	skipped=0;
        String topicsPath = "/home/doried/tdt/test/modified/topics/";
        String outputPath = "/home/doried/tdt/test/modified/allStories/";
        
        List<File> topicDirs = getListOfDirectories(topicsPath);
        
        
        for (File topicDir : topicDirs){
        	String dirName = topicDir.getName();
        	
        	File newDir = new File(outputPath + dirName);
        	if(!newDir.exists())
        		newDir.mkdir();
        	
            List<File> topicFiles = getListOfFiles(topicDir.getAbsolutePath());
            
            for(File storyFile : topicFiles){
            	StoryFile f = readStoryFile(storyFile,false);
            	String processedContent = TextProcessor.processText(f.getStoryContent());
            	if (processedContent.split(" ").length < 200){
            		System.out.println(++skipped);
            		continue;
            	}
                 
                String output = f.getStoryUrl() + "\n" + f.getStoryTitle() + "\n" + processedContent + "\n" + f.getStoryContent();

                String output_story_name = topicDir.getName() + "-" + storyFile.getName();
                PrintWriter wr = new PrintWriter(new File( newDir.getPath() +"/" + output_story_name ));
                wr.write(output);
                wr.close();
            }
        }    
    }
    
    public static void copyTopicsToADirectory() throws Exception{
    	skipped=0;
        String topicsPath = "/home/doried/tdt/test/modified/no-date/topics/";
        String outputPath = "/home/doried/tdt/test/modified/no-date/all/";
        List<File> topicDirs = getListOfDirectories(topicsPath);

        for (File topicDir : topicDirs){
        			
            List<File> topicFiles = getListOfFiles(topicDir.getAbsolutePath());
            
            for(File storyFile : topicFiles){
            	StoryFile f = readStoryFile(storyFile,false);
            	String processedContent =  f.getStoryContent(); // TextProcessor.processText(f.getStoryContent());
            	if (processedContent.split(" ").length < 200){
            		System.out.println(++skipped);
            		continue;
            	}
                 
                String output = f.getStoryUrl() + "\n" + f.getStoryTitle() + "\n" + f.getStoryDateAsString() + "\n" + processedContent;

                String output_story_name =  /*topicDir.getName() + "-" +*/ storyFile.getName();
                PrintWriter wr = new PrintWriter(new File( outputPath +"/" + output_story_name ));
                wr.write(output);
                wr.close();
            }
        }    
    }
    
    public static void copyOriginalFilesToOneDirectory() throws Exception{

        String topicsPath = "/home/doried/tdt/data/test2/text/";
        String outputPath = "/home/doried/tdt/data/test2/all-original/";
        List<File> topicDirs = getListOfDirectories(topicsPath);

        int fileNumber=0;
        for (File topicDir : topicDirs){
        			
            List<File> topicFiles = getListOfFiles(topicDir.getAbsolutePath());
            
            for(File storyFile : topicFiles){
            	StoryFile f = readStoryFile(storyFile,false);
            	String processedContent =  f.getStoryContent();
                
                // some processsing
                
                while(processedContent.contains("\n\n")){
                    processedContent = processedContent.replace("\n\n", "\n");
                }
                while(processedContent.contains("  ")){
                    processedContent = processedContent.replace("  ", " ");
                }
                 
                String output = f.getStoryUrl() + "\n" + f.getStoryTitle() + "\n" + f.getStoryDateAsString() + "\n" + processedContent;

                String output_story_name =  fileNumber++ + ".txt";
                PrintWriter wr = new PrintWriter(new File( outputPath +"/" + output_story_name ));
                wr.write(output);
                wr.close();
            }
        }    
    }
    
    
    
    public static void main(String[] args) throws Exception {
        //copyOriginalFilesToOneDirectory();
    	//copyTopicsToADirectory();
        //copyTopicFilesToOneDirectory();
    }
}
