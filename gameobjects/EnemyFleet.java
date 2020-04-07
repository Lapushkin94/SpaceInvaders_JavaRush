package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.engine.cell.Game;
import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;
import com.javarush.games.spaceinvaders.SpaceInvadersGame;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {

    public EnemyFleet() {
        createShips();
    }

    private static final int ROWS_COUNT = 3;
    private static final int COLUMNS_COUNT = 10;
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    private void createShips() {
        ships = new ArrayList<>();
        for (int x = 0; x < COLUMNS_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                ships.add(new EnemyShip(x * STEP, y * STEP + 12));
            }
        }
        ships.add(new Boss(STEP*COLUMNS_COUNT / 2 - ShapeMatrix.BOSS_ANIMATION_FIRST.length / 2 - 1, 5));
    }

    public void draw(Game game) {
        for (EnemyShip i : ships) {
            i.draw(game);
        }
    }

    private double getLeftBorder() {
        double minX = ships.get(0).x;
        for (EnemyShip i : ships) {
            if (i.x < minX) {
                minX = i.x;
            }
        }
        return minX;
    }

    private double getRightBorder() {
        double maxX = 0;
        for (EnemyShip i : ships) {
            if (maxX < (i.x + i.width) ) {
                maxX = i.x + i.width;
            }
        }
        return maxX;
    }

    private double getSpeed() {
        return Math.min(2.0, 3.0 / ships.size());
    }

    public void move() {
        if (ships.size() == 0) {}
        else {
            int i = 0;
            if (direction.equals(Direction.LEFT) && (getLeftBorder() < 0)) {
                direction = Direction.RIGHT;
                i = 1;
            }
            if (direction.equals(Direction.RIGHT) && (getRightBorder() > SpaceInvadersGame.WIDTH)) {
                direction = Direction.LEFT;
                i = 1;
            }

            double getSpeedResult = getSpeed();

            if (i == 1) {
                for (EnemyShip k : ships) {
                    k.move(Direction.DOWN, getSpeedResult);
                }
            }
            else {
                for (EnemyShip k : ships) {
                    k.move(direction, getSpeedResult);
                }
            }
            
        }
    }

    public Bullet fire(Game obj) {
        if (ships.size() == 0) {
            return null;
        }
        else {
            if (obj.getRandomNumber(100/SpaceInvadersGame.COMPLEXITY) > 0) {
                return null;
            }
            else {
                return ships.get(obj.getRandomNumber(ships.size())).fire();
            }

        }
    }


    public void deleteHiddenShips() {
        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).isVisible() == false) {
                ships.remove(i);
            }
        }
    }

    public double getBottomBorder() {
        double f = 0;
        if (ships.size() != 0) {
            for (EnemyShip i : ships) {
                if (i.y + i.height > f) {
                    f = i.y + i.height;
                }
            }
        }
        return f;
    }

    public int getShipsCount() {
        return ships.size();
    }

    public int verifyHit(List<Bullet> bullets) {
        if (bullets.isEmpty()) return 0;

        int result = 0;
        for (int i = 0; i < ships.size(); i++) {
            for (int j = 0; j < bullets.size(); j++) {
                if (ships.get(i).isCollision(bullets.get(j))) {
                    if (ships.get(i).isAlive && bullets.get(j).isAlive) {
                        ships.get(i).kill();
                        bullets.get(j).kill();
                        result += ships.get(i).score;
                    }
                }
            }
        }
        return result;
    }


}
