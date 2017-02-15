package files;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class TopicFiles {
	
	private String tag;
	public String getTag() {
		return tag;
	}

	public void setTag(String tag) {
		this.tag = tag;
	}

	public List<StoryFile> getStories() {
		return stories;
	}

	private List<StoryFile> stories;
	
	public TopicFiles (List<StoryFile> storyFiles, String tag){
		this.tag=tag;
		stories = new ArrayList<StoryFile>();
		for(StoryFile f:storyFiles)
			stories.add(f);
	}
	
	public Date getFirstStoryDate(){
		Date date = new Date();
		for(StoryFile f : stories)
			if(f.getStoryDate().compareTo(date)<0)
				date = f.getStoryDate();
		return date;
	}
	
	public Date getAverageDate(){
		long averageSeconds = 0L;
		for(StoryFile f : stories)
			averageSeconds += f.getStoryDate().getTime() / 1000L/stories.size();
		
		Date averageDate = new Date(averageSeconds * 1000L);
		return averageDate;
	}
	
}
