package com.javarush.games.spaceinvaders.gameobjects;

import com.javarush.games.spaceinvaders.Direction;
import com.javarush.games.spaceinvaders.ShapeMatrix;

public class EnemyShip extends Ship {

    public int score = 15;

    public EnemyShip(double x, double y) {
        super(x, y);
        super.setStaticView(ShapeMatrix.ENEMY);
    }

    public void move(Direction direction, double speed) {
        if (direction.equals(Direction.RIGHT)) {
            this.x = this.x + speed;
        }

        if (direction.equals(Direction.LEFT)) {
            this.x = this.x - speed;
        }

        if (direction.equals(Direction.DOWN)) {
            this.y = this.y + 2;
        }
    }

    @Override
    public Bullet fire() {
        return new Bullet(x + 1, y + height, Direction.DOWN);
    }

    @Override
    public void kill() {
        if (isAlive == false) {}
        else {
            isAlive = false;
            super.setAnimatedView(false, ShapeMatrix.KILL_ENEMY_ANIMATION_FIRST, ShapeMatrix.KILL_ENEMY_ANIMATION_SECOND, ShapeMatrix.KILL_ENEMY_ANIMATION_THIRD);
        }
    }
}
