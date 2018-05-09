import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;


/**
 * This class updates the visual presentation of the board.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class RiskVisual extends JFrame{

	int width = 800, height = 600;
	int radii = 20;
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
		
		Color continentColor;
		
		for (Continent c : risk.getBoard().GetContinents()) {
			continentColor = c.getColor();
			for (Territory t : c.getTerritories()) {
				int centerX = (int) (t.x * (double) width);
				int centerY = (int) (t.y * (double) height);
				g.setColor(continentColor);
				g.fillOval(centerX - radii / 2, centerY - radii / 2, radii, radii);
				for (Territory adjacent : t.getAdjacentTerritories()) {
					g.setColor(Color.BLACK);
					g.drawLine(centerX, centerY, (int) (adjacent.x * (double) width), (int) (adjacent.y*(double) height));
				}
			}
		}
		
		AffineTransform tx = AffineTransform.getScaleInstance(1, -1);
		tx.translate(0, -bufferedImage.getHeight(null));
		AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_NEAREST_NEIGHBOR);
		bufferedImage = op.filter(bufferedImage, null);
		
		this.getGraphics().drawImage(bufferedImage, 0, 0, null);
	}
}
