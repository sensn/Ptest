package com.company;

import processing.core.PApplet;

public class PWindow1 extends PApplet {
    public int mySize=30;
    PWindow owin;

    PWindow1() {
        super();

    }

    public void runIt(){
        PApplet.runSketch(new String[] {this.getClass().getSimpleName()}, this);
    }

    public void setOwin(PWindow rowin){
        owin = rowin;

    }

   public void settings() {
        size(500, 200);
    }

   public void setup() {
        background(150);
    }

  public  void draw() {
      background(owin.mySize);
        ellipse(random(width), random(height), random(50), random(50));
    }

   public void mousePressed() {
       background(owin.mySize);
        println("mousePressed in secondary window");
    }
}