import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;


/**
 * This class updates the visual presentation of the board.
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class RiskVisual extends JFrame{
	private static final long serialVersionUID = 1L;

	Risk risk;

	// Define screen size
	int width = 1920, height = 1080;

	// Define territory radii
	int continentRadius = 50;
	int playerRadius = continentRadius * 3/4;

	boolean drawNames = false;

	public RiskVisual(Risk risk) {
		this.risk = risk;

		// Standard window building code
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setSize(width,height);
		this.setVisible(true);
	}

	public void update() {
		// One draws to buffer, then buffer to screen, to prevent flickering
		BufferedImage buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = buffer.createGraphics();

	    // Background
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, width, height);

		// Draw game
		drawMap(g);
		drawGameStateInfo(g);

		// Draw buffer to screen
		this.getContentPane().getGraphics().drawImage(buffer, 0, 0, null);
	}

	private void drawGameStateInfo(Graphics g) {
		// Draw player names
		int offset = 0;
		for (Player p : risk.getPlayers()) {
			String string = p.getName();
			if (p == risk.getCurrentPlayer()) {
				string += " (Current)";
			}
			g.setColor(p.getColor());
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
				// Draw continent
				g.setColor(continentColor);
				g.fillOval(centerX - continentRadius / 2,
						centerY - continentRadius / 2,
						continentRadius,
						continentRadius);
				// Draw player
				g.setColor(t.getOwner().getColor());
				g.fillOval(centerX - playerRadius / 2,
						centerY - playerRadius / 2,
						playerRadius,
						playerRadius);
				// Draw territory names
				if(drawNames) {
					g.setColor(Color.BLACK);
					g.drawString(t.getName(), centerX, centerY);
				}
				// Draw unit counts
				g.setColor(Color.BLACK);
				g.drawString(Integer.toString(t.getNUnits()), centerX, centerY + 10);
				// Draw edges
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
