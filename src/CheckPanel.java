import java.awt.Graphics;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class CheckPanel extends JPanel {

	private ImageIcon normal;
	private ImageIcon highlighted;
	private boolean autosize;
	private boolean selected = false;
	
	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
	
	public void toggleSelected() {
		this.selected = !this.selected;
		paint(this.getGraphics());
	}
	

	public ImageIcon getNormal() {
		return normal;
	}

	public void setNormal(ImageIcon normal) {
		this.normal = normal;
	}

	public ImageIcon getHighlighted() {
		return highlighted;
	}

	public void setHighlighted(ImageIcon highlighted) {
		this.highlighted = highlighted;
	}

	public boolean isAutosize() {
		return autosize;
	}

	public void setAutosize(boolean autosize) {
		this.autosize = autosize;
	}
	
	public CheckPanel(ImageIcon normal) {
		this.normal = normal;
		autosize = true;
	}
	
	public CheckPanel(ImageIcon normal, ImageIcon highlighted) {
		this.normal = normal;
		this.highlighted = highlighted;
		autosize = true;
	}
	
	@Override
	public void paint(Graphics g) {
		g.setColor(super.getBackground());
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		if(autosize)
		{
			if(selected)
			{
				Image tmp = this.highlighted.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);				
				g.drawImage(new ImageIcon(tmp).getImage(), 0, 0, null);
			}
			else
			{
				Image tmp = this.normal.getImage().getScaledInstance(this.getWidth(), this.getHeight(), java.awt.Image.SCALE_SMOOTH);				
				g.drawImage(new ImageIcon(tmp).getImage(), 0, 0, null);
			}
		
		}
		else
		{
			if(selected)
				g.drawImage(this.highlighted.getImage(), 0, 0, null);
			else
				g.drawImage(this.normal.getImage(), 0, 0, null);
		}
	}
	

}
