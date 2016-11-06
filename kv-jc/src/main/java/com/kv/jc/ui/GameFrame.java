package com.kv.jc.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.MapConfiguration;
import com.kv.jc.http.json.Submarine;
import com.kv.jc.http.service.ServiceCallback;

public class GameFrame extends JFrame implements ServiceCallback {
	private static final long serialVersionUID = -3964035058950665891L;

	private final Game game;
	private final MapPanel mapPanel;

	public GameFrame(Game game) {
		super("kaJAVAdászok - Game ID:" + game.getId());
		this.game = game;

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// NOPE
		}

		setDefaultCloseOperation(EXIT_ON_CLOSE);
		MapConfiguration cfg = game.getMapConfiguration();
		setSize(cfg.getWidth() + 26, cfg.getHeight() + 48);
		setResizable(false);
		setLocationRelativeTo(null);
		JPanel wrapper = new JPanel();
		wrapper.setBackground(MapPanel.COLOR_ISLAND);
		wrapper.setLayout(null);
		wrapper.add(mapPanel = new MapPanel(game));
		add(wrapper);
		setVisible(true);
	}

	@Override
	public Long getGameId() {
		return game.getId();
	}

	@Override
	public void onUpdateState() {
		mapPanel.repaint();
		// TODO UPDATE CONFIG DIALOG
	}

	@Override
	public void onMove(Submarine submarine, Double speed, Double turn) {
		// TODO Auto-generated method stub
	}

	@Override
	public void onShoot(Submarine submarine, Double angle) {
		// TODO Auto-generated method stub
	}
}
