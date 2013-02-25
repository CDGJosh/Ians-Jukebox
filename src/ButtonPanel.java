import javax.swing.ImageIcon;
import javax.swing.JPanel;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


@SuppressWarnings("serial")
public class ButtonPanel extends JPanel {

	private ImageIcon icon;
	private ImageIcon icon_hover;
	private boolean mouseOver;
	private boolean autosize;
	
	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}
	
	public ImageIcon getIconHover() {
		return icon_hover;
	}

	public void setIconHover(ImageIcon icon_hover) {
		this.icon_hover = icon_hover;
	}

	public boolean isAutosize() {
		return autosize;
	}

	public void setAutosize(boolean autosize) {
		this.autosize = autosize;
	}

	public ButtonPanel(ImageIcon icon) {
		this.icon = icon;
		this.mouseOver = false;
		this.autosize = true;
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mouseOver = true;
				refresh();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				mouseOver = false;
				refresh();
			}
		});

	}
	
	public ButtonPanel(ImageIcon icon, ImageIcon icon_hover) {
		this.icon = icon;
		this.icon_hover = icon_hover;
		this.mouseOver = false;
		this.autosize = true;
		
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				mouseOver = true;
				refresh();
			}
			
			@Override
			public void mouseExited(MouseEvent e) {
				mouseOver = false;
				refresh();
			}
		});

	}
	
	private void refresh()
	{
		paint(this.getGraphics());
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(super.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if(autosize)
		{
			if(mouseOver)
			{
				Image tmp = this.icon_hover.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);				
				g.drawImage(new ImageIcon(tmp).getImage(), 0, 0, null);
			}
			else
			{
				Image tmp = this.icon.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);				
				g.drawImage(new ImageIcon(tmp).getImage(), 0, 0, null);
			}
		
		}
		else
		{
			if(mouseOver)
				g.drawImage(this.icon_hover.getImage(), 0, 0, null);
			else
				g.drawImage(this.icon.getImage(), 0, 0, null);
		}
	}

	

}
