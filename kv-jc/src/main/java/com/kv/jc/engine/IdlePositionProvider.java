package com.kv.jc.engine;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import com.kv.jc.http.json.Game;
import com.kv.jc.http.json.Position;
import com.kv.jc.http.json.Submarine;

public final class IdlePositionProvider {

	private static final int VERTICAL_SECTORS = 2;
	private static final int HORIZONTAL_SECTORS = 3;

	private final Game game;
	private final int torpedoRange;
	private final int sectorWidth;
	private final int sectorHeight;

	private List<Sector> sectors = new ArrayList<>();
	private List<Sector> nonVisitedSectors = new ArrayList<>();

	private Sector currentIdleSector = null;
	private Position currentIdlePosition;

	public static IdlePositionProvider create(Game game) {
		return new IdlePositionProvider(game);
	}

	private IdlePositionProvider(Game game) {
		this.game = game;
		torpedoRange = game.getMapConfiguration().getTorpedoExplosionRadius();
		sectorWidth = game.getMapConfiguration().getWidth() / HORIZONTAL_SECTORS;
		sectorHeight = game.getMapConfiguration().getHeight() / VERTICAL_SECTORS;
		initialize();
	}

	public Long getGameId() {
		return game == null ? -1 : game.getId();
	}

	public void updateIdlePositions() {
		final Position idlePosition = getIdlePosition();
		game.getSubmarines().forEach(submarine -> submarine.setIdle(idlePosition));
	}

	public Position getIdlePosition() {
		if (currentIdleSector == null) {
			currentIdleSector = getNextIdleSector();
			currentIdlePosition = new Position(currentIdleSector.getCenterX(), currentIdleSector.getCenterY());
		} else if (currentIdleSector.isReached()) {
			if (nonVisitedSectors.size() == 1) {
				nonVisitedSectors.clear();
				nonVisitedSectors.addAll(sectors);
			}
			nonVisitedSectors.remove(currentIdleSector);
			currentIdleSector = getNextIdleSector();
			currentIdlePosition = new Position(currentIdleSector.getCenterX(), currentIdleSector.getCenterY());
		}
		return currentIdlePosition;
	}

	private Sector getNextIdleSector() {
		Sector min = null;
		double minDistance = Double.MAX_VALUE;
		for (Sector sector : nonVisitedSectors) {
			for (Submarine submarine : game.getSubmarines()) {
				Point point = submarine.getPosition().getPoint();
				if (point == null) {
					continue;
				}
				double centerDistance = sector.getAvgCenterDistance();
				if (centerDistance < minDistance) {
					min = sector;
					minDistance = centerDistance;
				}
			}
		}
		return min;
	}

	private void initialize() {
		int number = 0;
		int x = 0;
		for (int w = 0; w < HORIZONTAL_SECTORS; w++) {
			int y = 0;
			for (int h = 0; h < VERTICAL_SECTORS; h++) {
				Sector sector = new Sector(number++, x, y);
				if (!sector.containsSubmarine()) {
					nonVisitedSectors.add(sector);
				}
				sectors.add(sector);
				y += sectorHeight;
			}
			x += sectorWidth;
		}
	}

	private class Sector extends Rectangle {
		private static final long serialVersionUID = 1L;

		@SuppressWarnings("unused")
		int number;

		public Sector(int number, int x, int y) {
			super(x, y, sectorWidth, sectorHeight);
			this.number = number;
		}

		public boolean isReached() {
			for (Submarine submarine : game.getSubmarines()) {
				Point point = submarine.getPosition().getPoint();
				if (point == null) {
					continue;
				}
				if (point.distance(getCenterX(), getCenterY()) < torpedoRange) {
					return true;
				}
			}
			return false;
		}

		public double getAvgCenterDistance() {
			double distance = 0;

			for (Submarine submarine : game.getSubmarines()) {
				Point point = submarine.getPosition().getPoint();
				if (point == null) {
					continue;
				}
				distance += point.distance(getCenterX(), getCenterY());
			}

			return distance / game.getSubmarines().size();
		}

		public boolean containsSubmarine() {
			for (Submarine submarine : game.getSubmarines()) {
				Point point = submarine.getPosition().getPoint();
				if (point == null) {
					continue;
				}
				if (contains(point)) {
					return true;
				}
			}
			return false;
		}
	}
}
