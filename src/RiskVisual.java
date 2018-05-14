import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFrame;


/**
 * This class updates the visual presentation of the board.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class RiskVisual extends JFrame{

	int width = 1920, height = 1080;
	int radii = 50;
	Risk risk;
	
	public RiskVisual(Risk risk) {
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setSize(width,height);
		
		this.setVisible(true);
		this.risk = risk;
	}

	public void update() {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = bufferedImage.createGraphics();

		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);
		
		drawMap(g);
		drawGameStateInfo(g);
		
		this.getContentPane().getGraphics().drawImage(bufferedImage, 0, 0, null);
	}

	private void drawGameStateInfo(Graphics g) {
		g.setColor(Color.BLACK);
		int offset = 0;
		for (Player p : risk.getPlayers()) {
			String string = p.getName();
			if (p == risk.getCurrentPlayer()) {
				string += " (Current)";
			}
			g.drawString(string, 0, offset += 10);
		}
	}

	private void drawMap(Graphics g) {
		Color continentColor;
		for (Continent c : risk.getBoard().getContinents()) {
			continentColor = c.getColor();
			for (Territory t : c.getTerritories()) {
				int centerX = (int) (t.x * (double) width);
				int centerY = height - (int) (t.y * (double) height);
				g.setColor(continentColor);
				g.fillOval(centerX - radii / 2, centerY - radii / 2, radii, radii);
				g.setColor(Color.BLACK);
				g.drawString(t.getName(), centerX, centerY);
				g.drawString(Integer.toString(t.getNUnits()), centerX, centerY + 10);
				for (Territory adjacent : t.getAdjacentTerritories()) {
					g.setColor(Color.BLACK);
					if (t.getName()=="Alaska" && adjacent.getName() == "Kamchatka") {
						g.drawLine(centerX, centerY, 0, centerY);
					} else if (t.getName()== "Kamchatka" && adjacent.getName() == "Alaska") {
						g.drawLine(centerX, centerY, width, centerY);
					} else {
						g.drawLine(centerX, centerY, (int) (adjacent.x * (double) width), height - (int) (adjacent.y*(double) height));
					}
				}
			}
		}
	}
}
