package com.company;

import controlP5.Accordion;
import controlP5.ControlKey;
import controlP5.ControlP5;
import controlP5.Group;
import processing.core.PApplet;   //Download processing add
import processing.core.*;   //Download processing add

import java.awt.*;

public class Main extends PApplet{

    int angle = 0;
    private PApplet sketch;

    ControlP5 cp5;
    Accordion accordion;
Color c = new Color(123) ;

    @Override
    public void settings() {
      //  size(500, 500);
       // size(640, 360);
        size(1280, 720);
        //size(displayWidth, displayHeight);
      //  fullScreen(0);
    }
    public void setup() {
        background(102);
        noStroke();
        fill(102);

        gui();


    }
    void gui() {

        cp5 = new ControlP5(this);
        cp5.enableShortcuts();
        //cp5.hide();
      //  cp5.
        // group number 1, contains 2 bangs
        Group g1 = cp5.addGroup("myGroup1")
                .setBackgroundColor(color(0, 64))
                .setBackgroundHeight(150)
                .enableCollapse()
                ;

        cp5.addBang("bang")
                .setPosition(10,20)
                .setSize(100,100)
                .moveTo(g1)
                .plugTo(this,"shuffle");
        ;

        // group number 2, contains a radiobutton
        Group g2 = cp5.addGroup("myGroup2")
                .setBackgroundColor(color(0, 64))
                .setBackgroundHeight(150)
                ;

        cp5.addRadioButton("radio")
                .setPosition(10,20)
                .setItemWidth(20)
                .setItemHeight(20)

                .addItem("black", 0)
                .addItem("red", 1)
                .addItem("green", 2)
                .addItem("blue", 3)
                .addItem("grey", 4)
                .setColorLabel(color(255))
                .activate(2)
                .moveTo(g2)
        ;

        // group number 3, contains a bang and a slider
        Group g3 = cp5.addGroup("myGroup3")
                .setBackgroundColor(color(0, 64))
                .setBackgroundHeight(150)
                ;

        cp5.addBang("shuffle")
                .setPosition(10,20)
                .setSize(40,50)
                .moveTo(g3)
        ;

        cp5.addSlider("hello")
                .setPosition(60,20)
                .setSize(100,20)
                .setRange(100,500)
                .setValue(100)
                .moveTo(g3)
        ;

        cp5.addSlider("world")
                .setPosition(60,50)
                .setSize(100,20)
                .setRange(100,500)
                .setValue(200)
                .moveTo(g3)
        ;

        // create a new accordion
        // add g1, g2, and g3 to the accordion.
        accordion = cp5.addAccordion("acc")
                .setPosition(40,40)
                .setWidth(200)
                .addItem(g1)
                .addItem(g2)
                .addItem(g3)
        ;

        cp5.mapKeyFor(new ControlKey() {public void keyEvent() {accordion.open(0,1,2);}}, 'o');
        cp5.mapKeyFor(new ControlKey() {public void keyEvent() {accordion.close(0,1,2);}}, 'c');
        cp5.mapKeyFor(new ControlKey() {public void keyEvent() {accordion.setWidth(300);}}, '1');
        cp5.mapKeyFor(new ControlKey() {public void keyEvent() {accordion.setPosition(0,0);accordion.setItemHeight(190);}}, '2');
        cp5.mapKeyFor(new ControlKey() {public void keyEvent() {accordion.setCollapseMode(ControlP5.ALL);}}, '3');
        cp5.mapKeyFor(new ControlKey() {public void keyEvent() {accordion.setCollapseMode(ControlP5.SINGLE);}}, '4');
        cp5.mapKeyFor(new ControlKey() {public void keyEvent() {cp5.remove("myGroup1");}}, '0');

        accordion.open(0,1,2);

        // use Accordion.MULTI to allow multiple group
        // to be open at a time.
        accordion.setCollapseMode(Accordion.MULTI);

        // when in SINGLE mode, only 1 accordion
        // group can be open at a time.
        // accordion.setCollapseMode(Accordion.SINGLE);
    }


public    void radio(int theC) {
        switch(theC) {
            case(0):c=Color.BLACK;break;
            case(1):c=Color.RED;break;
            case(2):c=Color.GREEN;break;
            case(3):c=Color.BLUE;break;
            case(4):c=Color.GRAY;break;
        }
    }


   public  void shuffle() {
       /* c = color(random(255),random(255),random(255),random(128,255));*/
        c = new Color((int) random(255),(int) random(255),(int) random(255)) ;
       // c = Color.RED ;
       System.out.println(c.getRGB());

    }



    //______DRAW_____
    public void draw(){
         // background(64);
          background(c.getRGB());
          //ellipse(mouseX, mouseY, 20, 20);

        // Draw only when mouse is pressed
        if (mousePressed == true) {
            angle += 5;
            float val = (float) (cos(radians(angle)) * 12.0);
            for (int a = 0; a < 360; a += 75) {
                float xoff = cos(radians(a)) * val;
                float yoff = sin(radians(a)) * val;
                fill(0);
                ellipse(mouseX + xoff, mouseY + yoff, val, val);
            }
            fill(255);
            ellipse(mouseX, mouseY, 2, 2);
        }
    }

    public void mousePressed(){
      //  background(64);
    }

    public static void main(String[] args) {
	// write your code here
        //String[] appletArgs = new String[] { "Main" };
        String[] processingArgs = {"Main"};
        Main mySketch = new Main();

        PApplet.runSketch(processingArgs, mySketch);
    }






}
