/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package files;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author doried
 */
public class DocumentDictionary {
    private HashMap<String,StoryFile> stories;
    
    public DocumentDictionary(String path) throws Exception{
        stories = new HashMap<String, StoryFile>();
        List<File> files = DirectoryProcessor.getListOfFiles(path);
        for(File f : files){
            StoryFile sFile = DirectoryProcessor.readStoryFile(f, false);
            stories.put(sFile.getStoryUrl(), sFile);
        }
    }
    
    public StoryFile getStoryFileByUrl(String storyUrl){
        return stories.get(storyUrl);
    }
    
}
