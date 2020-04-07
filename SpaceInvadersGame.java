package com.javarush.games.spaceinvaders;

import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.Game;
import com.javarush.engine.cell.Key;
import com.javarush.games.spaceinvaders.gameobjects.Bullet;
import com.javarush.games.spaceinvaders.gameobjects.EnemyFleet;
import com.javarush.games.spaceinvaders.gameobjects.PlayerShip;
import com.javarush.games.spaceinvaders.gameobjects.Star;

import java.util.ArrayList;
import java.util.List;

public class SpaceInvadersGame extends Game {

    public static final int WIDTH = 64;
    public static final int HEIGHT = 64;
    public static final int COMPLEXITY = 5;
    private List<Star> stars;
    private EnemyFleet enemyFleet;
    private List<Bullet> enemyBullets;
    private PlayerShip playerShip;
    private boolean isGameStopped;
    private int animationsCount;
    private List<Bullet> playerBullets;
    private static final int PLAYER_BULLETS_MAX = 1;
    private int score;

    @Override
    public void initialize() {
        setScreenSize(WIDTH, HEIGHT);
        createGame();
    }

    private void createGame() {
        score = 0;
        createStars();
        enemyFleet = new EnemyFleet();
        enemyBullets = new ArrayList<>();
        playerShip = new PlayerShip();
        isGameStopped = false;
        animationsCount = 0;
        playerBullets = new ArrayList<>();
        drawScene();
        setTurnTimer(40);
    }

    private void drawScene() {
        drawField();
        for (Bullet i : playerBullets) {
            i.draw(this);
        }
        playerShip.draw(this);
        for (Bullet i : enemyBullets) {
            i.draw(this);
        }
        enemyFleet.draw(this);
    }

    private void drawField() {
        for (int x = 0; x < HEIGHT; x++) {
            for (int y = 0; y < WIDTH; y++) {
                setCellValueEx(x, y, Color.BLACK, "");
            }
        }
        for (Star i : stars) {
            i.draw(this);
        }

    }

    private void createStars() {
        stars = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            int a = getRandomNumber(64);
            int b = getRandomNumber(64);
            stars.add(new Star(a, b));
        }
    }

    private void moveSpaceObjects() {
        for (Bullet i : playerBullets) {
            i.move();
        }
        enemyFleet.move();
        for (Bullet i : enemyBullets) {
            i.move();
        }
        playerShip.move();
    }

    private void removeDeadBullets() {
        for (int i = 0; i < enemyBullets.size(); i++) {
            if ((enemyBullets.get(i).y >= (HEIGHT - 1)) || (enemyBullets.get(i).isAlive == false)) {
                enemyBullets.remove(i);
            }
        }
        for (int i = playerBullets.size()-1; i>=0; i--) {
            if (!playerBullets.get(i).isAlive || (playerBullets.get(i).y + playerBullets.get(i).height) < 0) {
                playerBullets.remove(i);
                //playerBullets.clear();
            }
        }
    }

    private void check() {
        if (enemyFleet.getBottomBorder() >= playerShip.y) {
            playerShip.kill();
        }
        if (enemyFleet.getShipsCount() == 0) {
            playerShip.win();
            stopGameWithDelay();
        }
        playerShip.verifyHit(enemyBullets);
        int sc = enemyFleet.verifyHit(playerBullets);
        enemyFleet.deleteHiddenShips();
        removeDeadBullets();
        if (playerShip.isAlive == false) {
            stopGameWithDelay();
        }
        score = score + sc;
    }

    @Override
    public void onTurn(int i) {
        moveSpaceObjects();
        check();
        Bullet fire = enemyFleet.fire(this);
        if (fire != null) {
            enemyBullets.add(fire);
        }
        setScore(score);
        drawScene();
    }

    private void stopGame(boolean isWin) {
        isGameStopped = true;
        stopTurnTimer();
        if (isWin) {
            showMessageDialog(Color.BROWN, "Gratz!", Color.GREEN, 60);
        }
        else {
            showMessageDialog(Color.BROWN, "Ты не триумфатор", Color.RED, 60);
        }
    }

    private void stopGameWithDelay() {
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }

    @Override
    public void onKeyPress(Key key) {
        if ((key == Key.SPACE) && (isGameStopped == true)) {
            createGame();
        }
        if (key == Key.LEFT) {
            playerShip.setDirection(Direction.LEFT);
        }
        if (key == Key.RIGHT) {
            playerShip.setDirection(Direction.RIGHT);
        }
        if (key == Key.SPACE) {
            Bullet b = playerShip.fire();
            if ((b != null) && (playerBullets.size() < PLAYER_BULLETS_MAX)) {
                playerBullets.add(b);
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {
        if ((key == Key.LEFT) && (playerShip.getDirection() == Direction.LEFT)) {
            playerShip.setDirection(Direction.UP);
        }
        if ((key == Key.RIGHT) && (playerShip.getDirection() == Direction.RIGHT)) {
            playerShip.setDirection(Direction.UP);
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if ((x >= WIDTH) || (y >= HEIGHT) || (x < 0) || (y < 0)) {}
        else {
            super.setCellValueEx(x, y, cellColor, value);
        }
    }
}