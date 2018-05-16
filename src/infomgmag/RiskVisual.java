package infomgmag;

import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.DefaultCaret;

/**
 * This class updates the visual presentation of the board.
 * 
 * @author Games&AgentsGroup8
 * @version FirstPrototype
 * @date 4/5/2018
 */
public class RiskVisual extends JFrame {
	private static final long serialVersionUID = 1L;

	Risk risk;

	// Define screen size
	int width = 1920, height = 1080;
	int gameWidth = 1600, gameHeight = this.height;
	int logWidth = this.width - this.gameWidth - 20;

	JPanel gamePanel;
	JPanel infoPanel;
	JTextArea infoArea;
	JTextArea logArea;
	JScrollPane logPanel;

	// Define territory radii
	int continentRadius = 50;
	int playerRadius = this.continentRadius * 3 / 4;
	int attackMarkerTargetRadius = this.playerRadius * 1 / 2;
	int attackMarkerCrossResolution = this.playerRadius * 1 / 2;

	boolean drawNames = false;

	Image map;

	BufferedImage buffer;

	Graphics2D g;
	long targetFrameDuration = 25;

	long frameDuration = 1000;

	long lastFrameTime;

	public RiskVisual(Risk risk) {
		this.risk = risk;

		try {
			this.map = ImageIO.read(new File("src/map.jpg"));
		} catch (Exception e) {
			System.out.println("Could not read image");
			e.printStackTrace();
		}

		this.gamePanel = new JPanel(new BorderLayout(0, 0));
		this.gamePanel.setPreferredSize(new Dimension(this.gameWidth, this.gameHeight));
		this.gamePanel.setVisible(true);
		this.add(this.gamePanel, BorderLayout.WEST);

		this.infoPanel = new JPanel(new BorderLayout(0, 0));
		this.infoPanel.setSize(this.width - this.gameWidth, this.height);
		this.add(this.infoPanel, BorderLayout.EAST);

		this.logArea = new JTextArea();
		this.logArea.setSize(this.logWidth, this.height * 3 / 4);
		this.logArea.setEditable(false);
		this.logArea.setLineWrap(true);
		this.logArea.setWrapStyleWord(true);

		this.infoArea = new JTextArea();
		this.infoArea.setSize(this.logWidth, this.height);
		this.infoArea.setEditable(false);
		this.infoArea.setLineWrap(true);
		this.infoArea.setWrapStyleWord(true);
		this.infoArea.append("Here should be player info \n hahah");
		this.infoPanel.add(this.infoArea, BorderLayout.NORTH);

		this.logPanel = new JScrollPane(this.logArea);
		this.logPanel.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		this.logPanel.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		this.logPanel.setPreferredSize(new Dimension(this.width - this.gameWidth, this.height * 1 / 4));
		DefaultCaret caret = (DefaultCaret) this.logArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		this.infoPanel.add(this.logPanel, BorderLayout.SOUTH);

		// Standard window building code
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setSize(this.width, this.height);
		this.pack();
		this.setVisible(true);

		this.buffer = new BufferedImage(this.gameWidth, this.gameHeight, BufferedImage.TYPE_INT_ARGB);

		this.lastFrameTime = System.currentTimeMillis();
	}

	public void createBuffer() {
		// One draws to buffer, then buffer to screen, to prevent flickering
		this.g = this.buffer.createGraphics();
		this.g.setStroke(new BasicStroke(5));

		Font currentFont = this.g.getFont();
		Font newFont = currentFont.deriveFont(currentFont.getSize() * 2F);
		this.g.setFont(newFont);

		// Background
		this.g.setColor(Color.WHITE);
		if (this.map == null)
			this.g.fillRect(0, 0, this.gameWidth, this.gameHeight);
		else
			this.g.drawImage(this.map, 0, 0, this.gameWidth, this.gameHeight, null);
	}

	public void drawBuffer() {
		this.frameDuration = System.currentTimeMillis() - this.lastFrameTime;
		if (this.frameDuration < this.targetFrameDuration)
			try {
				Thread.sleep(this.targetFrameDuration - this.frameDuration);
			} catch (InterruptedException e) {
				System.exit(0);
			}
		this.lastFrameTime = System.currentTimeMillis();
		this.gamePanel.getGraphics().drawImage(this.buffer, 0, 0, null);

		this.updateGameInfo();
	}

	public void drawCombatArrow(CombatMove cm) {
		this.g.setColor(Color.RED);
		int attackingX = this.rasterX(cm.getAttackingTerritory().x);
		int attackingY = this.rasterY(cm.getAttackingTerritory().y);
		int defendingX = this.rasterX(cm.getDefendingTerritory().x);
		int defendingY = this.rasterY(cm.getDefendingTerritory().y);
		this.g.drawLine(attackingX, attackingY, defendingX, defendingY);
		this.g.fillOval(defendingX - this.attackMarkerTargetRadius / 2, defendingY - this.attackMarkerTargetRadius / 2,
				this.attackMarkerTargetRadius, this.attackMarkerTargetRadius);
		this.g.drawLine(attackingX - this.attackMarkerCrossResolution / 2,
				attackingY - this.attackMarkerCrossResolution / 2, attackingX + this.attackMarkerCrossResolution / 2,
				attackingY + this.attackMarkerCrossResolution / 2);
		this.g.drawLine(attackingX - this.attackMarkerCrossResolution / 2,
				attackingY + this.attackMarkerCrossResolution / 2, attackingX + this.attackMarkerCrossResolution / 2,
				attackingY - this.attackMarkerCrossResolution / 2);
	}

	private void drawMap(Graphics2D g) {
		for (Continent c : this.risk.getBoard().getContinents())
			for (Territory t : c.getTerritories()) {
				int centerX = (int) (t.x * this.gameWidth);
				int centerY = this.gameHeight - (int) (t.y * this.gameHeight);
				// Draw edges
				for (Territory adjacent : t.getAdjacentTerritories()) {
					g.setColor(Color.BLACK);
					if (t.getName() == "Alaska" && adjacent.getName() == "Kamchatka")
						g.drawLine(centerX, centerY, 0, centerY);
					else if (t.getName() == "Kamchatka" && adjacent.getName() == "Alaska")
						g.drawLine(centerX, centerY, this.gameWidth, centerY);
					else
						g.drawLine(centerX, centerY, (int) (adjacent.x * this.gameWidth),
								this.gameHeight - (int) (adjacent.y * this.gameHeight));
				}
			}

		Color continentColor;
		for (Continent c : this.risk.getBoard().getContinents()) {
			continentColor = c.getColor();
			for (Territory t : c.getTerritories()) {
				int centerX = (int) (t.x * this.gameWidth);
				int centerY = this.gameHeight - (int) (t.y * this.gameHeight);

				// Draw continent
				g.setColor(continentColor);
				g.fillOval(centerX - this.continentRadius / 2, centerY - this.continentRadius / 2, this.continentRadius,
						this.continentRadius);
				// Draw player
				g.setColor(t.getOwner().getColor());
				g.fillOval(centerX - this.playerRadius / 2, centerY - this.playerRadius / 2, this.playerRadius,
						this.playerRadius);
				// Draw territory names
				if (this.drawNames) {
					g.setColor(Color.BLACK);
					g.drawString(t.getName(), centerX, centerY);
				}
				// Draw unit counts
				g.setColor(Color.WHITE);
				g.drawString(Integer.toString(t.getNUnits()), centerX - 5, centerY + 10);
			}
		}

		int offset = 10;
		for (Player p : this.risk.getPlayers()) {
			g.setColor(p.getColor());
			g.fillRect(0, offset + 10, 20, 20);
			g.setColor(Color.WHITE);
			g.drawString(p.getName(), 30, offset += 25);
		}
	}

	private int rasterX(double x) {
		return (int) (x * this.gameWidth);
	}

	private int rasterY(double y) {
		return this.gameHeight - (int) (y * this.gameHeight);
	}

	public void update() {
		this.createBuffer();
		this.drawMap(this.g);
		this.drawBuffer();
	}

	public void update(CombatMove cm) {
		this.createBuffer();
		this.drawMap(this.g);
		this.drawCombatArrow(cm);
		this.drawBuffer();
	}

	private void updateGameInfo() {
		String info = new String();

		info += "Turn " + this.risk.getTurn() + "\n";

		info += "Current state:\n\n";

		for (Player p : this.risk.getPlayers()) {
			info += p.getName();
			if (this.risk.getCurrentPlayer() == p)
				info += " (current)";
			info += "\nHand:\n";

			info += "Infantry: " + p.hand.getInfantry() + "\n";
			info += "Cavalry: " + p.hand.getCavalry() + "\n";
			info += "Artillery: " + p.hand.getArtillery() + "\n";

			info += "\n";
		}

		this.infoArea.setText(info);
	}
}
