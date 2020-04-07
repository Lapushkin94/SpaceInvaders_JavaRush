package com.javarush.games.spaceinvaders.gameobjects;
import com.javarush.engine.cell.Color;
import com.javarush.engine.cell.*;

public class Star extends GameObject {

    public Star(double x, double y) {
        super(x, y);
    }

    private static final String STAR_SIGN = "★";

    public void draw(Game obj) {
        obj.setCellValueEx((int) x, (int) y, Color.NONE, STAR_SIGN, Color.GOLD, 100);
    }



}
