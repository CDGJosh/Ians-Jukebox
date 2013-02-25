import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class SwitchButtonPanel extends JPanel {

	
	private ImageIcon icon;
	private ImageIcon icon_hover;
	private ImageIcon alt_icon;
	private ImageIcon alt_icon_hover;
	private boolean mouseOver;
	private boolean autosize;
	private boolean selected;
	/**
	 * Create the panel.
	 */
	public SwitchButtonPanel(ImageIcon icon, ImageIcon icon_hover, ImageIcon alt_icon, ImageIcon alt_icon_hover) {
		this.icon = icon;
		this.icon_hover = icon_hover;
		this.alt_icon = alt_icon;
		this.alt_icon_hover = alt_icon_hover;
		this.mouseOver = false;
		this.autosize = true;
		this.selected = false;
		
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
	
	public ImageIcon getIcon() {
		return icon;
	}

	public void setIcon(ImageIcon icon) {
		this.icon = icon;
	}

	public ImageIcon getIcon_hover() {
		return icon_hover;
	}

	public void setIcon_hover(ImageIcon icon_hover) {
		this.icon_hover = icon_hover;
	}

	public ImageIcon getAlt_icon() {
		return alt_icon;
	}

	public void setAlt_icon(ImageIcon alt_icon) {
		this.alt_icon = alt_icon;
	}

	public ImageIcon getAlt_icon_hover() {
		return alt_icon_hover;
	}

	public void setAlt_icon_hover(ImageIcon alt_icon_hover) {
		this.alt_icon_hover = alt_icon_hover;
	}

	public boolean isAutosize() {
		return autosize;
	}

	public void setAutosize(boolean autosize) {
		this.autosize = autosize;
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setSelected(boolean selected) {
		this.selected = selected;
		this.refresh();
	}
	
	public void toggleSelected() {
		this.selected = !this.selected;
		this.refresh();
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
				if(!selected)
				{
					Image tmp = this.icon_hover.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);				
					g.drawImage(new ImageIcon(tmp).getImage(), 0, 0, null);
				}
				else
				{
					Image tmp = this.alt_icon_hover.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);				
					g.drawImage(new ImageIcon(tmp).getImage(), 0, 0, null);
				}
			}
			else
			{
				if(!selected)
				{
					Image tmp = this.icon.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);				
					g.drawImage(new ImageIcon(tmp).getImage(), 0, 0, null);
				}
				else
				{
					Image tmp = this.alt_icon.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);				
					g.drawImage(new ImageIcon(tmp).getImage(), 0, 0, null);
				}
			}
		
		}
		else
		{
			if(mouseOver)
			{
				g.drawImage(this.icon_hover.getImage(), 0, 0, null);
			}
			else
			{
				g.drawImage(this.icon.getImage(), 0, 0, null);
			}
		}
	}

}
