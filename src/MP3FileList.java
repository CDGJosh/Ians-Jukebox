import java.util.ArrayList;
import java.util.List;

import javax.swing.ListModel;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;


public class MP3FileList implements ListModel<String> {
	
	List<MP3File> files;
	List<ListDataListener> listener;
	
	public MP3FileList()
	{
		files = new ArrayList<MP3File>();
		listener = new ArrayList<ListDataListener>();
	}
	
	public void addFile(MP3File file)
	{
		files.add(file);
		for(int i = 0; i < listener.size(); i++)
		{
			listener.get(i).contentsChanged(new ListDataEvent(file, ListDataEvent.CONTENTS_CHANGED, i, i));
		}
	}
	
	public void removeFile(MP3File file)
	{
		files.remove(file);
		for(int i = 0; i < listener.size(); i++)
		{
			listener.get(i).contentsChanged(new ListDataEvent(file, ListDataEvent.CONTENTS_CHANGED, i, i));
		}
	}
	
	public void removeFile(int pos)
	{
		MP3File tmp = files.get(pos);
		files.remove(pos);
		for(int i = 0; i < listener.size(); i++)
		{
			listener.get(i).contentsChanged(new ListDataEvent(tmp, ListDataEvent.CONTENTS_CHANGED, i, i));
		}
	}
	
	@Override
	public void addListDataListener(ListDataListener lis) {
		listener.add(lis);		
	}

	@Override
	public String getElementAt(int pos) {
		return files.get(pos).getStringRepresentation();
	}
	
	public MP3File getFileAt(int pos) {
		return files.get(pos);
	}

	@Override
	public int getSize() {
		return files.size();
	}

	@Override
	public void removeListDataListener(ListDataListener lis) {	
		listener.remove(lis);
	}

}
