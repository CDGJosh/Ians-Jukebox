import java.io.File;
import java.io.IOException;
import java.util.Map;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.blinkenlights.jid3.*;
import org.blinkenlights.jid3.v1.*;
import org.blinkenlights.jid3.v2.*;
import org.tritonus.share.sampled.file.TAudioFileFormat;

public class MP3File {

	private File file;
	private String title;
	private String author;
	private String year;
	private String album;	
	private int duration;
	
	public File getFile() {
		return file;
	}


	public String getTitle() {
		return title;
	}


	public String getAuthor() {
		return author;
	}


	public String getYear() {
		return year;
	}


	public String getAlbum() {
		return album;
	}


	public int getDuration() {
		return duration;
	}
	
	public String getStringRepresentation() {
		if(this.title == null)
		{
			return this.author;
		}
		else
		{
			return this.author + " - " + this.title;
		}
	}


	public MP3File(File file) 
	{
		this.file = file;		
		String[] name = file.getName().split("-");
		
		MediaFile aF = new org.blinkenlights.jid3.MP3File(file);

        ID3Tag[] tags = null;
		try {
			tags = aF.getTags();
		} catch (ID3Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
        for (int i=0; i < tags.length; i++)
        {
            if (tags[i] instanceof ID3V1_0Tag)
            {
                ID3V1_0Tag tag = (ID3V1_0Tag)tags[i];
                if (tag.getTitle() != null)
                {
                	this.title = tag.getTitle();
                }
                else
                {
                	if(name.length > 1)
        			{
        				this.title = name[1];
        			}
                }
                
                if(tag.getArtist() != null)
                {
                	this.author = tag.getArtist();
                }
                else
                {
                	if(name.length > 1)
        			{
        				this.author = name[0];
        			}
                }
                
                this.album = tag.getAlbum();
        		this.year = tag.getYear();
            }
            else if (tags[i] instanceof ID3V2_3_0Tag)
            {
                ID3V2_3_0Tag tag = (ID3V2_3_0Tag)tags[i];
                
                this.title = tag.getTitle();                
                if(this.title == null)
                {
                	if(name.length > 1)
        			{
        				this.title = name[1];
        			}
                }
                
                this.author = tag.getArtist();
                if(this.author == null)
                {
                	if(name.length > 1)
        			{
        				this.author = name[0];
        			}
                }
                
                this.album = tag.getAlbum();
        		try {
					this.year = ""+tag.getYear();
				} catch (ID3Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        
        AudioFileFormat fileFormat = null;
		try {
			fileFormat = AudioSystem.getAudioFileFormat(file);
		} catch (UnsupportedAudioFileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        if (fileFormat instanceof TAudioFileFormat) {
            Map<?, ?> properties = ((TAudioFileFormat) fileFormat).properties();
            String key = "duration";
            Long microseconds = (Long) properties.get(key);
            int mili = (int) (microseconds / 1000);
            this.duration = (int)((double)mili / 1000.0);
            System.out.println(this.duration);
            
        }
        
        if(this.title == null)
        {
        	if(name.length > 1)
			{
				this.title = name[1].replaceAll("\\.mp3", "");
			}
        }
        
        if(this.author == null)
        {
			this.author = name[0].replaceAll("\\.mp3", "");
        }
	}
}
