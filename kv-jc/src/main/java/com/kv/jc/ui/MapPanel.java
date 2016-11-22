package com.kv.jc.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

import com.kv.jc.engine.Engine;
import com.kv.jc.http.json.Entity;
import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.MapConfiguration;
import com.kv.jc.http.json.Position;
import com.kv.jc.http.json.Submarine;

public class MapPanel extends JPanel {

  private static final long serialVersionUID = 5033134092633647572L;

  public static final Color COLOR_BACKGROUND = new Color(28, 163, 236);
  public static final Color COLOR_ISLAND = new Color(250, 206, 157);
  public static final Color COLOR_SUBMARINE = Color.GREEN;
  public static final Color COLOR_ENEMY = Color.ORANGE;
  public static final Color COLOR_TORPEDO = Color.RED;

  private final Game game;
  private final DisplayConfiguration displayConfiguration;
  private MapConfiguration cfg;

  public MapPanel(Game game, DisplayConfiguration displayConfiguration) {
    super();
    this.game = game;
    this.displayConfiguration = displayConfiguration;
    cfg = game.getMapConfiguration();
    MapConfiguration cfg = game.getMapConfiguration();
    setBounds(10, 10, cfg.getWidth(), cfg.getHeight());
    setBackground(COLOR_BACKGROUND);
  }

  @Override
  protected void paintComponent(Graphics graphics) {
    super.paintComponent(graphics);
    Graphics2D g = Graphics2D.class.cast(graphics);

    cfg = game.getMapConfiguration();
    // draw islands
    g.setColor(COLOR_ISLAND);
    cfg.getIslandPositions().forEach(island -> fillOval(g, island, cfg.getIslandSize() * 2));

    // draw submarines
    g.setColor(COLOR_SUBMARINE);
    game.getSubmarines().forEach(submarine -> drawSubmarine(g, submarine));

    // draw enemies
    g.setColor(COLOR_ENEMY);
    game.getEnemies().forEach(enemy -> drawEnemy(g, enemy));

    // draw torpedos
    g.setColor(COLOR_TORPEDO);
    game.getTorpedos().forEach(torpedo -> drawTorpedo(g, torpedo));

    // draw rounds & scores
    g.setColor(Color.WHITE);
    drawRoundsAndScores(g);
  }

  private void drawRoundsAndScores(Graphics2D g) {
    Position position = new Position(10.0, 20.0);
    if (displayConfiguration.showRound) {
      drawChars(g, "Round: " + game.getRound() + " / " + cfg.getRounds(), position);
      position.setY(position.getY() + 5);
    }
    if (displayConfiguration.showScores) {
      game.getScores().getOrderedScores().forEach(score -> {
        drawChars(g, score.getKey() + ": " + score.getValue(), position);
      });
    }
  }

  private void drawTorpedo(Graphics2D g, Entity torpedo) {
    fillOval(g, torpedo.getPosition(), 5);
    drawVector(g, torpedo.getPosition(), torpedo.getAngle(), 5);
    if (displayConfiguration.showTorpedoRange) {
      drawOval(g, torpedo.getPosition(), cfg.getTorpedoExplosionRadius() * 2);
    }

    Position displayPosition = normalizeCenter(torpedo.getPosition(), 5);
    displayPosition.setX(displayPosition.getX() - 15);
    displayPosition.setY(displayPosition.getY() + 20);
    if (displayConfiguration.showId) {
      drawChars(g, getIdString(torpedo.getId()), displayPosition);
    }
    if (displayConfiguration.showSpeed) {
      drawChars(g, getSpeedString(torpedo.getVelocity()), displayPosition);
    }
  }

  private void drawEnemy(Graphics2D g, Entity enemy) {
    fillOval(g, enemy.getPosition(), cfg.getSubmarineSize());
    drawVector(g, enemy.getPosition(), enemy.getAngle(), cfg.getSubmarineSize());

    if (displayConfiguration.showSonarRange) {
      drawOval(g, enemy.getPosition(), cfg.getSonarRange());
    }

    Position displayPosition = normalizeCenter(enemy.getPosition(), cfg.getSubmarineSize());
    displayPosition.setX(displayPosition.getX() - 15);
    displayPosition.setY(displayPosition.getY() + cfg.getSubmarineSize() + 15);
    if (displayConfiguration.showId) {
      drawChars(g, getIdString(enemy.getId()), displayPosition);
    }
    if (displayConfiguration.showSpeed) {
      drawChars(g, getSpeedString(enemy.getVelocity()), displayPosition);
    }
  }

  private void drawSubmarine(Graphics2D g, Submarine submarine) {
    fillOval(g, submarine.getPosition(), cfg.getSubmarineSize());
    drawVector(g, submarine.getPosition(), submarine.getAngle(), cfg.getSubmarineSize());
    if (displayConfiguration.showSonarRange) {
      drawOval(g, submarine.getPosition(), submarine.getSonarExtended() > 0 ? cfg.getExtendedSonarRange() * 2 : cfg.getSonarRange() * 2);
    }

    Position displayPosition = normalizeCenter(submarine.getPosition(), cfg.getSubmarineSize());
    displayPosition.setX(displayPosition.getX() - 15);
    displayPosition.setY(displayPosition.getY() + cfg.getSubmarineSize() + 15);
    if (displayConfiguration.showId) {
      drawChars(g, getIdString(submarine.getId()), displayPosition);
    }
    if (displayConfiguration.showHp) {
      drawChars(g, getHpString(submarine.getHp()), displayPosition);
    }
    if (displayConfiguration.showSpeed) {
      drawChars(g, getSpeedString(submarine.getVelocity()), displayPosition);
    }
    if (displayConfiguration.showTorpedoCooldown) {
      drawChars(g, getCDString(submarine.getTorpedoCooldown()), displayPosition);
    }
    if (displayConfiguration.showSonarCooldown) {
      drawChars(g, getSonarCooldown(submarine.getSonarCooldown()), displayPosition);
    }
    if (displayConfiguration.showSonarActive) {
      drawChars(g, getSonarActive(submarine.getSonarExtended()), displayPosition);
    }
  }

  private void drawChars(Graphics2D g, String string, Position position) {
    g.drawChars(string.toCharArray(), 0, string.length(), position.getX().intValue(), position.getY().intValue());
    position.setY(position.getY() + 15);
  }

  private void drawVector(Graphics2D g, Position position, Double angle, Integer baseSize) {
    Position normalized = normalize(position);
    Position endPos = Engine.getEndPos(game, normalized, -angle, baseSize + 30);
    g.drawLine(normalized.getX().intValue(), normalized.getY().intValue(), endPos.getX().intValue(), endPos.getY().intValue());
    Position endPos2 = Engine.getEndPos(game, endPos, (-angle - 180) + 20, 10);
    Position endPos3 = Engine.getEndPos(game, endPos, (-angle - 180) - 20, 10);
    g.drawLine(endPos.getX().intValue(), endPos.getY().intValue(), endPos2.getX().intValue(), endPos2.getY().intValue());
    g.drawLine(endPos.getX().intValue(), endPos.getY().intValue(), endPos3.getX().intValue(), endPos3.getY().intValue());
  }

  private void fillOval(Graphics2D g, Position position, Integer size) {
    Position normalised = normalizeCenter(position, size);
    g.fillOval(normalised.getX().intValue(), normalised.getY().intValue(), size, size);
  }

  private void drawOval(Graphics2D g, Position position, Integer size) {
    Position normalised = normalizeCenter(position, size);
    g.drawOval(normalised.getX().intValue(), normalised.getY().intValue(), size, size);
  }

  private Position normalize(Position position) {
    return new Position(position.getX(), cfg.getHeight() - position.getY());
  }

  private Position normalizeCenter(Position position, Integer r) {
    Position p = normalize(position);
    Integer rp2 = r / 2;
    p.setX(p.getX() - rp2);
    p.setY(p.getY() - rp2);
    return p;
  }

  private String getIdString(Long id) {
    return "ID: " + id;
  }

  private String getHpString(Integer hp) {
    return "HP: " + hp;
  }

  private String getSpeedString(Double speed) {
    return "S: " + speed;
  }

  private String getCDString(Integer cd) {
    return "CD: " + cd;
  }

  private String getSonarCooldown(Integer cd) {
    return "CD(s): " + cd;
  }

  private String getSonarActive(Integer active) {
    return "Sonar: " + active;
  }
  
}
