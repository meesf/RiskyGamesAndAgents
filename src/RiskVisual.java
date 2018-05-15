import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
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
	int attackMarkerTargetRadius = playerRadius * 1/2;
	int attackMarkerCrossResolution = playerRadius * 1/2;

	boolean drawNames = false;

	Image map;

	public RiskVisual(Risk risk) {
		this.risk = risk;

		try {
			map = ImageIO.read(new File("src/map.jpg"));
		} catch(Exception e) {
			System.out.println("Could not read image");
			e.printStackTrace();
		}

		// Standard window building code
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.pack();
		this.setSize(width,height);
		this.setVisible(true);

		this.buffer = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

		this.lastFrameTime = System.currentTimeMillis();
	}

	BufferedImage buffer;
	Graphics2D g;

	private int rasterX(double x) {
		return (int) (x * width);
	}

	private int rasterY(double y) {
		return height - (int) (y * height);
	}

	public void createBuffer() {
		// One draws to buffer, then buffer to screen, to prevent flickering
	    g = buffer.createGraphics();
	    g.setStroke(new BasicStroke(5));

	    Font currentFont = g.getFont();
	    Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
	    g.setFont(newFont);

	    // Background
		g.setColor(Color.WHITE);
		if (map == null) {
			g.fillRect(0, 0, width, height);
		} else {
			g.drawImage(map, 0, 0, width, height, null);
		}
	}


	long targetFrameDuration = (long) (25);
	long frameDuration = 1000;
	long lastFrameTime;

	public void drawBuffer() {
		frameDuration = System.currentTimeMillis() - lastFrameTime;
		if (frameDuration < targetFrameDuration) {
			try {
				Thread.sleep(targetFrameDuration - frameDuration);
			} catch (InterruptedException e) {
				System.exit(0);
			}
		}
		lastFrameTime = System.currentTimeMillis();
		this.getContentPane().getGraphics().drawImage(buffer, 0, 0, null);
	}

	public void update() {
		createBuffer();
		drawMap(g);
		drawGameStateInfo(g);
		drawBuffer();
	}

	public void update(CombatMove cm) {
		createBuffer();
		drawMap(g);
		drawGameStateInfo(g);
		drawCombatArrow(cm);
		drawBuffer();
	}

	public void drawCombatArrow(CombatMove cm) {
		g.setColor(Color.RED);
		int attackingX = rasterX(cm.getAttackingTerritory().x);
		int attackingY = rasterY(cm.getAttackingTerritory().y);
		int defendingX = rasterX(cm.getDefendingTerritory().x);
		int defendingY = rasterY(cm.getDefendingTerritory().y);
		g.drawLine(attackingX, attackingY, defendingX, defendingY);
		g.fillOval(defendingX - attackMarkerTargetRadius / 2,
				defendingY - attackMarkerTargetRadius / 2,
				attackMarkerTargetRadius,
				attackMarkerTargetRadius);
		g.drawLine(attackingX - attackMarkerCrossResolution/2,
				attackingY - attackMarkerCrossResolution/2,
				attackingX + attackMarkerCrossResolution/2,
				attackingY + attackMarkerCrossResolution/2);
		g.drawLine(attackingX - attackMarkerCrossResolution/2,
				attackingY + attackMarkerCrossResolution/2,
				attackingX + attackMarkerCrossResolution/2,
				attackingY - attackMarkerCrossResolution/2);
	}

	private void drawGameStateInfo(Graphics2D g) {
		// Draw player names
		int offset = 0;
		for (Player p : risk.getPlayers()) {
			String string = p.getName();
			if (p == risk.getCurrentPlayer()) {
				string += " (Current)";
			}
			g.setColor(p.getColor());
			g.drawString(string, 0, offset += 20);
		}
	}

	private void drawMap(Graphics2D g) {
		for (Continent c : risk.getBoard().getContinents()) {
			for (Territory t : c.getTerritories()) {
				int centerX = (int) (t.x * (double) width);
				int centerY = height - (int) (t.y * (double) height);
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
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(t.getNUnits()), centerX-5, centerY + 10);
			}
		}
	}
}
