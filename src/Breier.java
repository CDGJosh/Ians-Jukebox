import java.awt.EventQueue;
import java.awt.Image;

import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.plaf.metal.MetalSliderUI;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.SwingConstants;
import javax.swing.JMenuBar;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import java.awt.Toolkit;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JCheckBox;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;
import org.jnativehook.mouse.NativeMouseWheelEvent;
import org.jnativehook.mouse.NativeMouseWheelListener;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


@SuppressWarnings("serial")
public class Breier extends JFrame implements MusicPlayerListener, NativeKeyListener, NativeMouseWheelListener{

	private MusicPlayerListener stupidReference;
	private JPanel contentPane;
	private MP3FileList songs;
	private JList<String> list;
	private int indexPlaying;
	private MusicPlayer mp;
	private boolean autoNext = false;
	private boolean repeat = false;
	private boolean shuffle = false;
	private boolean altPressed = false;
	
	private JSlider slider;
	private JSlider slider_1;
	private JLabel lblNewLabel;
	private JLabel lblNewLabel_1 ;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			GlobalScreen.registerNativeHook();
	    }
	    catch (NativeHookException ex) {
	    	System.out.println("hooking error");
	    }
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Breier frame = new Breier();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Breier() {
		GlobalScreen.getInstance().addNativeKeyListener(this);
		GlobalScreen.getInstance().addNativeMouseWheelListener(this);
		
		stupidReference = this;
		
		songs = new MP3FileList();
		mp = new MusicPlayer();
		mp.addListener(this);
		
		setIconImage(Toolkit.getDefaultToolkit().getImage("note.png"));
		setTitle("Ians Jukebox #better than Benzaiten");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 464, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("");
		btnNewButton.setIconTextGap(0);		
		btnNewButton.setBounds(10, 80, 89, 89);
		Image tmp = new ImageIcon("play.png").getImage().getScaledInstance(btnNewButton.getWidth()-8, btnNewButton.getHeight()-8, java.awt.Image.SCALE_SMOOTH);
		btnNewButton.setIcon(new ImageIcon(tmp));
		contentPane.add(btnNewButton);
		
		slider = new JSlider();
		
		slider.setUI(new MetalSliderUI() {
		    protected void scrollDueToClickInTrack(int direction) {
		        int value = slider.getValue(); 

		        if (slider.getOrientation() == JSlider.HORIZONTAL) {
		            value = this.valueForXPosition(slider.getMousePosition().x);
		            mp.seek(value);
		            mp.setGain(slider_1.getValue());
		        } else if (slider.getOrientation() == JSlider.VERTICAL) {
		            value = this.valueForYPosition(slider.getMousePosition().y);
		        }
		        slider.setValue(value);
		    }
		});
		slider.setValue(0);
		slider.setMajorTickSpacing(10);
		slider.setBounds(0, 46, 448, 23);
		contentPane.add(slider);
		
		slider_1 = new JSlider();
		slider_1.setMajorTickSpacing(5);
		slider_1.setPaintTicks(true);
		slider_1.setOrientation(SwingConstants.VERTICAL);
		slider_1.setBounds(409, 80, 39, 89);
		slider_1.addChangeListener(new ChangeListener() {
		      public void stateChanged(ChangeEvent e) {
		    	  mp.setGain(slider_1.getValue());
		      }
		    });
		contentPane.add(slider_1);
		
		JButton btnNewButton_1 = new JButton("");
		btnNewButton_1.setBounds(109, 80, 89, 89);
		Image tmp2 = new ImageIcon("pause.png").getImage().getScaledInstance(btnNewButton.getWidth()-8, btnNewButton.getHeight()-8, java.awt.Image.SCALE_SMOOTH);
		btnNewButton_1.setIcon(new ImageIcon(tmp2));
		contentPane.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("");
		btnNewButton_2.setBounds(208, 80, 89, 89);
		Image tmp3 = new ImageIcon("stop.png").getImage().getScaledInstance(btnNewButton.getWidth()-8, btnNewButton.getHeight()-8, java.awt.Image.SCALE_SMOOTH);
		btnNewButton_2.setIcon(new ImageIcon(tmp3));
		contentPane.add(btnNewButton_2);
		
		JMenuBar menuBar = new JMenuBar();
		menuBar.setBounds(0, 0, 448, 21);
		contentPane.add(menuBar);
		
		JMenu mnNewMenu = new JMenu("File");
		menuBar.add(mnNewMenu);
		
		JMenuItem mntmNewMenuItem = new JMenuItem("Add File to Playlist");
		
		mntmNewMenuItem.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent arg0) 
			{
				JFileChooser chooser = new JFileChooser();
				chooser.setFileFilter(new FileFilter() {
	                public boolean accept(File f) {
	                    return f.getName().toLowerCase().endsWith(".mp3") || f.isDirectory();
	                }
	                public String getDescription() {
	                    return "MP3 komprimierte Musikdateien (*.mp3)";
	                }
	            });
			    int returnVal = chooser.showOpenDialog(contentPane);
			    
			    if(returnVal == JFileChooser.APPROVE_OPTION)
			    {
			    	File f = chooser.getSelectedFile();
			    	System.out.println(f.getName().toLowerCase().endsWith(".mp3"));
			    	if(f.getName().toLowerCase().endsWith(".mp3") && !f.isDirectory())
			    	{
				    	songs.addFile(new MP3File(f));
			    	}
			    	else
			    	{
			    		String[] tmp = f.getName().split("\\.");
			    		String fileEnd = tmp[tmp.length-1];
			    		JOptionPane.showMessageDialog(contentPane, "Can't open \""+fileEnd+"\" files.", "Error opening file", JOptionPane.WARNING_MESSAGE);
			    	
			    	}
			    }
			}
		});
		mnNewMenu.add(mntmNewMenuItem);
		
		JMenuItem mntmNewMenuItem_1 = new JMenuItem("New menu item");
		mnNewMenu.add(mntmNewMenuItem_1);
		
		JMenuItem mntmNewMenuItem_2 = new JMenuItem("New menu item");
		mnNewMenu.add(mntmNewMenuItem_2);
		
		lblNewLabel = new JLabel("New label");
		lblNewLabel.setBounds(10, 21, 438, 23);
		contentPane.add(lblNewLabel);
		
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(10, 180, 428, 71);
		contentPane.add(scrollPane);
		list = new JList<String>(songs);
		list.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_DELETE)
				{
					songs.removeFile(list.getSelectedIndex());
				}
			}
		});
		scrollPane.setViewportView(list);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox("autoplay");
		
		chckbxNewCheckBox.addChangeListener(new ChangeListener() 
		{
			public void stateChanged(ChangeEvent e) {
				
			}
		});
		chckbxNewCheckBox.setBounds(303, 80, 100, 23);
		contentPane.add(chckbxNewCheckBox);
		
		JCheckBox chckbxNewCheckBox_1 = new JCheckBox("repeat");
		chckbxNewCheckBox_1.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				repeat = !repeat;
			}
		});
		chckbxNewCheckBox_1.setBounds(303, 106, 97, 23);
		contentPane.add(chckbxNewCheckBox_1);
		
		JCheckBox chckbxNewCheckBox_2 = new JCheckBox("shuffle");
		chckbxNewCheckBox_2.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				shuffle = !shuffle;
			}
		});
		chckbxNewCheckBox_2.setBounds(303, 132, 100, 23);
		contentPane.add(chckbxNewCheckBox_2);
		
		lblNewLabel_1 = new JLabel("New label");
		lblNewLabel_1.setBounds(303, 162, 96, 14);
		contentPane.add(lblNewLabel_1);
		
		MouseListener mouseListener = new MouseAdapter() {
		     public void mouseClicked(MouseEvent e) {
		         if (e.getClickCount() == 2) {
		             int index = list.locationToIndex(e.getPoint());
		             indexPlaying = index;
		             mp.stop();
		             mp.removeListener(stupidReference);
		     		 mp = null;
		     		 mp = new MusicPlayer();	
		     		 mp.addListener(stupidReference);
		             mp.open(songs.getFileAt(index).getFile().getAbsolutePath());
		             mp.play();
		             mp.setGain(slider_1.getValue());
		             lblNewLabel.setText(songs.getElementAt(index));
		          }
		     }			
		 };
		 list.addMouseListener(mouseListener);
		 
		 DropTargetListener dropTargetListener = new DropTargetListener() {
			@Override
			public void dragEnter(DropTargetDragEvent arg0) {
				
			}
			@Override
			public void dragExit(DropTargetEvent arg0) {
				
			}
			@Override
			public void dragOver(DropTargetDragEvent arg0) {
				
			}
			@Override
			public void drop(DropTargetDropEvent e) {				
				try {
					Transferable tr = e.getTransferable();
				    DataFlavor[] flavors = tr.getTransferDataFlavors();
				    for (int i = 0; i < flavors.length; i++)
				    	if (flavors[i].isFlavorJavaFileListType()) {
				    		e.acceptDrop (e.getDropAction());
				    		List files = (List) tr.getTransferData(flavors[i]);
				    		for(int x = 0; x < files.size(); x++)
				    		{
				    			File f = new File(files.get(x).toString());
						    	if(f.getName().toLowerCase().endsWith(".mp3") && !f.isDirectory())
						    	{
							    	songs.addFile(new MP3File(f));
						    	}
				    		}
				    		e.dropComplete(true);
				    		return;
				    	}
				} catch (Throwable t) { 
					t.printStackTrace(); 
				}
				e.rejectDrop();
				    
			}
			@Override
			public void dropActionChanged(DropTargetDragEvent dtde) {
				
			}			
		 };
		 
		 DropTarget dropTarget = new DropTarget(list, dropTargetListener);
	}
	
	@Override
	public void positionChanged(int newPosition) {
		slider.setValue(newPosition);
		lblNewLabel_1.setText("time: "+((double)songs.getFileAt(this.indexPlaying).getDuration()/(double)slider.getMaximum()*(double)newPosition));
		if(newPosition == mp.getLength())
			playNextSong();
		
	}
	
	private void playLastSong()
	{
		int newIdx = this.indexPlaying - 1;
		
		if(newIdx <= -1)
		{
			newIdx = songs.getSize()-1;
		}
		
		mp.stop();
		mp.removeListener(this);
		mp = null;
		mp = new MusicPlayer();	
		mp.addListener(this);
		mp.open(songs.getFileAt(newIdx).getFile().getAbsolutePath());
        mp.play();
		mp.setGain(slider_1.getValue());
		
		lblNewLabel.setText(songs.getElementAt(newIdx));
		
		this.indexPlaying = newIdx;
	}

	private void playNextSong()
	{
		if(repeat)
		{
			mp.stop();
			mp.removeListener(this);
			mp = null;
			mp = new MusicPlayer();
			mp.addListener(this);
			mp.open(songs.getFileAt(indexPlaying).getFile().getAbsolutePath());
            mp.play();
			mp.setGain(slider_1.getValue());
		}
		if(shuffle)
		{
			int newIdx = new java.util.Random().nextInt(songs.getSize());
			
			while(newIdx == this.indexPlaying)
			{
				newIdx = new java.util.Random().nextInt(songs.getSize());
			}
			
			mp.stop();
			mp.removeListener(this);
			mp = null;
			mp = new MusicPlayer();	
			mp.addListener(this);
			mp.open(songs.getFileAt(newIdx).getFile().getAbsolutePath());
            mp.play();
			mp.setGain(slider_1.getValue());
			
			lblNewLabel.setText(songs.getElementAt(newIdx));
			
			this.indexPlaying = newIdx;
		}
		else
		{
			int newIdx = this.indexPlaying + 1;
			if(newIdx >= this.songs.getSize())
			{
				newIdx = 0;
			}
			
			mp.stop();
			mp.removeListener(this);
			mp = null;
			mp = new MusicPlayer();	
			mp.addListener(this);
			mp.open(songs.getFileAt(newIdx).getFile().getAbsolutePath());
            mp.play();
			mp.setGain(slider_1.getValue());
			
			lblNewLabel.setText(songs.getElementAt(newIdx));
			
			this.indexPlaying = newIdx;
		}
	}
	
	@Override
	public void lengthChanged(int length) {	
		slider.setMaximum(length);
	}

	@Override
	public void nativeKeyPressed(NativeKeyEvent e) {
		if(e.getRawCode() == 176)
		{
			playNextSong();
		}
		else if(e.getRawCode() == 177)
		{
			int proPer = (int)(100.0/(double)slider.getMaximum()*(double)slider.getValue());
			if(proPer <= 5)
			{
				mp.seek(0);
	            mp.setGain(slider_1.getValue());
			}
			else
			{
				playLastSong();
			}
		}
		else if(e.getRawCode() == 179)
		{
			if(mp.getStatus() == MusicPlayer.PLAYING)
			{
				mp.pause();
			}
			else if(mp.getStatus() == MusicPlayer.PAUSED)
			{
				mp.resume();
			}
			else
			{
				mp.play();
			}
		}
		else if(e.getRawCode() == 178)
		{
			mp.stop();
		}
		
		else if(e.getKeyCode() == NativeKeyEvent.VK_ALT)
		{
			this.altPressed = true;
		}
	}

	@Override
	public void nativeKeyReleased(NativeKeyEvent e) {
		
		if(e.getKeyCode() == NativeKeyEvent.VK_ALT)
		{
			this.altPressed = false;
		}
	}

	@Override
	public void nativeKeyTyped(NativeKeyEvent e) {
		
	}

	@Override
	public void nativeMouseWheelMoved(NativeMouseWheelEvent e) {
		if(altPressed)
		{
			int curVol = slider_1.getValue();
			int maxDif = 100 - (curVol + (e.getScrollAmount() * (-1) *e.getWheelRotation()));
			if(maxDif < 0)
			{
				mp.setGain(100);
				slider_1.setValue(100);
			}
			else if(maxDif < 100)
			{
				mp.setGain(curVol + e.getScrollAmount());
				slider_1.setValue(curVol + (e.getScrollAmount() * (-1) *e.getWheelRotation()));
			}
		}		
	}

	@Override
	public void statusChanged(int status) {
		// TODO Auto-generated method stub
		
	}
}
