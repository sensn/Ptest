package com.company;

import processing.core.PApplet;

public class PWindow extends PApplet {
    PWindow1 owin;
    public int mySize=127;

    PWindow() {
        super();
       // PApplet.runSketch(new String[] {this.getClass().getSimpleName()}, this);

    }
    public void runIt(){
        PApplet.runSketch(new String[] {this.getClass().getSimpleName()}, this);
    }

public void setOwin(PWindow1 rowin){
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
        println("mousePressed in secondary window");
       background(owin.mySize);
    }
}