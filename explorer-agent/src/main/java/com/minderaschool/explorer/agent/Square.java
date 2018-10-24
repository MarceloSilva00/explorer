package com.minderaschool.explorer.agent;

public class Square {
    private  boolean top = false;
    private  boolean bottom = false;
    private  boolean right = false;
    private  boolean left= false;
    private int count;

    public Square() {
    }

    public int getCount() {
        return count;
    }

    public void incCount() {
        this.count++;
    }

    public void setTop(boolean top) {
        this.top = top;
    }

    public void setBottom(boolean bottom) {
        this.bottom = bottom;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isTop() {
        return top;
    }

    public boolean isBottom() {
        return bottom;
    }

    public boolean isRight() {
        return right;
    }

    public boolean isLeft() {
        return left;
    }
}
