package com.shokichi.gamelathinh;

import android.widget.Button;

public class Card {
    private int x,y;
    private Button card;

    public Card(int x,int y, Button card){
        this.x = x;
        this.y = y;
        this.card = card;
    }

    public Button getButton() {
        return card;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
