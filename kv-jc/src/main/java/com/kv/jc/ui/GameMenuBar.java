package com.kv.jc.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import com.kv.jc.http.json.Game;
import com.kv.jc.http.service.ServiceCallback;

public class GameMenuBar extends JMenuBar {

  private static final long serialVersionUID = 565444334296047059L;

  //private final Game game;
  private final DisplayConfiguration cfg;
  private final ServiceCallback callback;

  public GameMenuBar(Game game, DisplayConfiguration cfg, ServiceCallback callback) {
    super();
    //this.game = game;
    this.cfg = cfg;
    this.callback = callback;

    add(createUIMenu());
  }

  private JMenu createUIMenu() {
    JMenu menu = new JMenu("Show/Hide");

    JCheckBoxMenuItem round = new JCheckBoxMenuItem("Show Round");
    round.setSelected(cfg.showRound);
    round.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showRound = round.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem scores = new JCheckBoxMenuItem("Show Scores");
    scores.setSelected(cfg.showScores);
    scores.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showScores = scores.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem id = new JCheckBoxMenuItem("Show ID");
    id.setSelected(cfg.showId);
    id.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showId = id.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem hp = new JCheckBoxMenuItem("Show HP");
    hp.setSelected(cfg.showHp);
    hp.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showHp = hp.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem speed = new JCheckBoxMenuItem("Show speed");
    speed.setSelected(cfg.showSpeed);
    speed.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showSpeed = speed.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem cooldown = new JCheckBoxMenuItem("Show Torpedo Cooldown");
    cooldown.setSelected(cfg.showTorpedoCooldown);
    cooldown.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showTorpedoCooldown = cooldown.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem sonarRange = new JCheckBoxMenuItem("Show Sonar range");
    sonarRange.setSelected(cfg.showSonarRange);
    sonarRange.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showSonarRange = sonarRange.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem sonarCooldown = new JCheckBoxMenuItem("Show Sonar cooldown");
    sonarCooldown.setSelected(cfg.showSonarCooldown);
    sonarCooldown.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showSonarCooldown = sonarCooldown.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem sonarActive = new JCheckBoxMenuItem("Show Sonar active");
    sonarActive.setSelected(cfg.showSonarActive);
    sonarActive.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showSonarActive = sonarActive.isSelected();
        callback.onUpdateState();
      }
    });

    JCheckBoxMenuItem torpedoRange = new JCheckBoxMenuItem("Show Torpedo range");
    torpedoRange.setSelected(cfg.showTorpedoRange);
    torpedoRange.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        cfg.showTorpedoRange = torpedoRange.isSelected();
        callback.onUpdateState();
      }
    });

    menu.add(round);
    menu.add(scores);
    menu.addSeparator();
    menu.add(id);
    menu.add(hp);
    menu.add(cooldown);
    menu.addSeparator();
    menu.add(torpedoRange);
    menu.addSeparator();
    menu.add(sonarRange);
    menu.add(sonarActive);
    menu.add(torpedoRange);
    return menu;
  }

}
