package com.company;

import processing.core.PApplet;   //Download processing add
import processing.data.StringList;
import processing.data.Table;
import processing.data.TableRow;

//
import java.awt.*;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Map;
//
import themidibus.*; //Import the library

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.ShortMessage;
//
import controlP5.*;
//

public class Main extends PApplet{
    //
    int w=640; //non fullscreen width
    int h=360; //non fullscreen height
   // boolean fullscreen=false; //fullscreen

    MidiBus myBus; // The MidiBus
//
    ControlP5 cp5;
    DropdownList d1, d2;
    int cnt = 0;
    //

    int angle = 0;
    private PApplet sketch;

    //MSEQ Globals

    ShedMidi shedMidi;

    //KetaiList selectionlist;
    ArrayList<String> colorlist = new ArrayList<String>();
    String mypath;
    boolean loadfile;
    int filesl;

    StringList inventory;
    // just for testing ... an array to simulate note start times .. starting at t=0 for first note
    long[] noteStartTimes = new long[96];
    //length in nanoseconds of a single cell in the grid
    long cellLength = (long) (0.125f * 1000000000L); // 1/16th note at 120bpm ...
    boolean loopPlay = true;
    boolean start = false;
    int bpm = 120;
    float ms = 125;
    float mms;

    int rx = 0;
    int mx = 0;
    //int prg =0;
    int bblock;
    int chordcount = 0;
    int[][] mychords = {{3, 7, 10, 14},
            {3, 7, 11, 14},
            {4, 7, 10, 14},
            {4, 7, 11, 14}};
    int time;
    House myHouse;
    Table table;
    Table wtable;
    int note;
    BButton[] bbuttons = new BButton[10];
    CButton[] cbuttons = new CButton[8];
    DButton[] dbuttons = new DButton[8];
    EButton[] ebuttons = new EButton[4];
    int num = 0;
    float tempo;
    int mychannel = 1;

    int tabentry = 0;
    int tabcentry = 0;
    int numentries = 8;
    int numchannels = 10;
    boolean midioff = false;
    byte index = 0;
    byte[] buffer = new byte[3];
    byte[] buffer1 = new byte[3];
    int[] ids;
    int[] tick;
    int[] channel;
    int[] notenum;
    int[] velo;
    ByteBuffer buf;
    ByteBuffer buf1;
    ByteBuffer buf2;
    ByteBuffer buf3;
    ByteBuffer ddata;
    ByteBuffer stepbuf;
    ByteBuffer mstat;
    ///
    Test1[] instances1 =  new Test1[1];
//




    @Override
    public void settings() {
      //  size(500, 500);
        w=640;
        h=360;
       size(w, h);
       // size(640, 360);
       // size(displayWidth, displayHeight);
      // fullScreen(2);


        getpath();

    }
    public void pre() {
        //if (mouseReleased())
        if (w != width || h != height) {
            // Sketch window has resized
            w = width;
            h = height;
            println("RESIZE EVENT");
            bblock=w/21;

            for (int x = 0; x < numchannels; x++) {
                bbuttons[x].setX((bblock * 1.2f) + bblock + bblock * (1 * x), (bblock / 2) + bblock * 10.4f, bblock * 0.9f, bblock * 0.9f);
            }
            for (int x = 0; x < 8; x++) {

                cbuttons[x].setX((bblock * 1.2f) + 4 * bblock + bblock * (x), (bblock / 2) + bblock * 8, bblock * 0.9f, bblock * 0.9f);
                dbuttons[x].setX((bblock * 1.2f) + 4 * bblock + bblock * (x), (bblock / 2) + bblock * 9, bblock * 0.9f, bblock * 0.9f);
            }
            ebuttons[0].setX((bblock * 1.2f) + 2 * bblock + bblock, (bblock / 2) + bblock * 8, bblock * 0.9f, bblock * 0.9f);
            ebuttons[1].setX((bblock * 1.2f) + 12 * bblock + bblock, (bblock / 2) + bblock * 8, bblock * 0.9f, bblock * 0.9f);
            ebuttons[2].setX((bblock * 1.2f) + 12 * bblock + bblock, (bblock / 2) + bblock * 9, bblock * 0.9f, bblock * 0.9f);
            ebuttons[3].setX((bblock * 1.2f) + 1 * bblock, (bblock / 2) + bblock * 8, bblock * 0.9f, bblock * 0.9f);


            for (int i = 0; i < myHouse.rooms.length; i++) {
                int mc = 0;
                for (int y = 0; y < 5; y++) {
                    for (int x = 0; x < 16; x++) {

                        myHouse.rooms[i].buttons[mc].setX((bblock * 1.2f) + bblock * x, (bblock / 2) + bblock * y, bblock * 0.9f, bblock * 0.9f);
                        mc += 1;
                    }
                }
                for (int x = 0; x < 8; x++) {
                    myHouse.rooms[i].buttons[x + 80].setX((bblock * 1.2f) + bblock * (2 * x), (bblock / 2) + bblock * 5.5f, bblock * 0.9f, bblock * 0.9f);
                }
                for (int x = 0; x < 8; x++) {
                    myHouse.rooms[i].buttons[x + 88].setX((bblock * 1.2f) + bblock * (2 * x), (bblock / 2) + bblock * 6.7f, bblock * 0.9f, bblock * 0.9f);
                }
                for (int y = 0; y < 5; y++) {
                    myHouse.rooms[i].buttons[y + 96].setX((bblock * 0.125f), (bblock / 2) + bblock * y, bblock * 0.9f, bblock * 0.9f);
                }
                myHouse.rooms[i].instances[0].setX((bblock * 1.5f) + bblock * (15.7f), bblock, bblock, bblock);
                myHouse.rooms[i].instances[1].setX((bblock * 1.5f) + bblock * (16.7f), bblock, bblock, bblock);
                myHouse.rooms[i].instances[2].setX((bblock * 1.5f) + bblock * (17.7f), bblock, bblock, bblock);
                myHouse.rooms[i].instances[3].setX((bblock * 1.5f) + bblock * (18.7f), bblock, bblock, bblock);
            }
            instances1[0].setX((bblock/2) + bblock * 10.4f,(bblock*1.2f) + bblock + bblock * (10),(bblock*1.2f) + bblock + bblock * (10), bblock,bblock+bblock*15.5f);
          //  block=w/21;

           // redraw();

            // reset();
            //  ws = "Size = " +w + " x " + h + " pixels";
            // Do what you need to do here
        }
    }


   public  void gui() {

        cp5 = new ControlP5(this);
        cp5.enableShortcuts();

        cp5 = new ControlP5(this);
        // create a DropdownList
        d1 = cp5.addDropdownList("myList-d1")
               // .setPosition(100, 100)
                .setPosition((bblock * 1.2f) + 2 * bblock + bblock*12, (bblock / 2) + bblock * 8)
        .setHeight(bblock*2)
                .setWidth((int) (bblock*1.5f))
        .setOpen(false)
        .setItemHeight((int) (bblock))

        .setVisible(false)
        ;

        customize(d1); // customize the first list
    }
   public void customize(DropdownList ddl) {
        // a convenience function to customize a DropdownList
        ddl.setBackgroundColor(color(190));
        ddl.setItemHeight(bblock/3);
        ddl.setBarHeight(30);
        ddl.setCaptionLabel("Files...");
       // ddl.cap
       // ddl.setla
        /*ddl.captionLabel().set("dropdown");
        ddl.captionLabel().style().marginTop = 3;
        ddl.captionLabel().style().marginLeft = 3;
        ddl.valueLabel().style().marginTop = 3;*/

     /*   for (int i=0;i<40;i++) {
            ddl.addItem("item "+i, i);
        }*/

        //ddl.scroll(0);

        ddl.setColorBackground(color(60));
        ddl.setColorActive(color(255, 128));
    }
   public void controlEvent(ControlEvent theEvent) {
        // DropdownList is of type ControlGroup.
        // A controlEvent will be triggered from inside the ControlGroup class.
        // therefore you need to check the originator of the Event with
        // if (theEvent.isGroup())
        // to avoid an error message thrown by controlP5.

        if (theEvent.isGroup()) {
            // check if the Event was triggered from a ControlGroup
            println("event from group : "+theEvent.getGroup().getValue()+" from "+theEvent.getGroup());
            System.out.println("EVENT GROUP" );
        }
        else if (theEvent.isController()) {
            println("event from controller : "+theEvent.getController().getValue()+" from "+theEvent.getController());
            System.out.println("EVENT CONTROLLER" );
            Map<String,Object> item = d1.getItem((int)theEvent.getController().getValue());

            System.out.println( item.keySet().toString());

            d1.getItem((int)(int)theEvent.getController().getValue()).get("text");
            System.out.println(  d1.getItem((int)(int)theEvent.getController().getValue()).get("text").toString());
            selectit(d1.getItem((int)(int)theEvent.getController().getValue()).get("text").toString());
        }
    }
    public void selectit(String selection){

         println(selection);
         println("LOADFILE BOOL: "+loadfile);
        if (loadfile){
            if (selection == "Cancel"){println(selection);
            d1.setVisible(false);
            }
            else {
                println("LOAD: "+selection);
                table = loadTable(mypath+"/SENSN/"+selection, "header");
                tabcentry=0;
                tableget();
                updateall();
                dbuttons[0].on = true;
                for (int i = 1; i < dbuttons.length; i++) {
                    dbuttons[i].on = false;}
                loadfile=false;
                d1.setVisible(false);
            }
        }else{
          //  if (selection == "NewFile"){
            if (selection == "New File"){
                //saveTable(table,"/"+mypath+"/SENSN/abc"+filesl+".csv");}
                saveTable(table,mypath+"/SENSN/abc"+filesl+".csv");
                d1.setVisible(false);
            }
            else if (selection == "Cancel"){
                d1.setVisible(false);
            }
            else {
                saveTable(table,mypath+"/SENSN/"+selection);
                d1.setVisible(false);
            }

        }


    }


    public void setup() {
        w = width;
        h = height;
        surface.setSize(w,h);
        surface.setResizable(true);
        registerMethod("pre", this);


        ddata = ByteBuffer.allocateDirect(3);
        stepbuf = ByteBuffer.allocateDirect(1);
        mstat = ByteBuffer.allocateDirect(1);

        reset();
    }
public void reset(){
    background(102);
    noStroke();
    fill(102);

//MSEQ SETUP

    frameRate(24);
    bblock = width / 21;
    gui();   //CP5
    check();  //labels for buttons

    myHouse = new House();
    tablesetup();
    wtablesetup();
    println("setup MIDI");

    try {
        shedMidi = new ShedMidi();
    } catch (MidiUnavailableException e) {
        e.printStackTrace();
    } catch (InvalidMidiDataException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }

//SHEDMIDI_PLACE
    for (int i = 0; i < 96; i++) {
        noteStartTimes[i] = i * cellLength;
    }
    ms = 60000 / bpm;
    bpm = 120;
    ms = 125;

    //INIT
    instances1[0] = new Test1((bblock/2) + bblock * 10.4f,(bblock*1.2f) + bblock + bblock * (10),(bblock*1.2f) + bblock + bblock * (10), bblock,bblock+bblock*15.5f);
    // CHANNEL BUTTONS
    for (int x = 0; x < numchannels; x++) {
        bbuttons[x] = new BButton((bblock * 1.2f) + bblock + bblock * (1 * x), (bblock / 2) + bblock * 10.4f, bblock * 0.9f, bblock * 0.9f);
    }
    for (int x = 0; x < 8; x++) {

        cbuttons[x] = new CButton((bblock * 1.2f) + 4 * bblock + bblock * (x), (bblock / 2) + bblock * 8, bblock * 0.9f, bblock * 0.9f);
        dbuttons[x] = new DButton((bblock * 1.2f) + 4 * bblock + bblock * (x), (bblock / 2) + bblock * 9, bblock * 0.9f, bblock * 0.9f);
    }
    ebuttons[0] = new EButton((bblock * 1.2f) + 2 * bblock + bblock, (bblock / 2) + bblock * 8, bblock * 0.9f, bblock * 0.9f, "save", "savePattern");
    ebuttons[1] = new EButton((bblock * 1.2f) + 12 * bblock + bblock, (bblock / 2) + bblock * 8, bblock * 0.9f, bblock * 0.9f, "save", "saveFile");
    ebuttons[2] = new EButton((bblock * 1.2f) + 12 * bblock + bblock, (bblock / 2) + bblock * 9, bblock * 0.9f, bblock * 0.9f, "", "loadFile");
    ebuttons[3] = new EButton((bblock * 1.2f) + 1 * bblock, (bblock / 2) + bblock * 8, bblock * 0.9f, bblock * 0.9f, "chord", "chord");
    myHouse.rooms[0].name = "Channel 1";
    myHouse.rooms[0].wifi = true;
    myHouse.rooms[0].isDefined = true;

    myHouse.rooms[1].name = "Channel 2";
    myHouse.rooms[1].wifi = false;
    myHouse.rooms[1].isDefined = true;
    myHouse.rooms[1].colour = color(5, 0, 255);

    myHouse.rooms[2].name = "Channel 3";
    myHouse.rooms[2].wifi = false;
    myHouse.rooms[2].isDefined = true;
    myHouse.rooms[2].windows = 5;
    myHouse.rooms[2].colour = color(5, 210, 255);

    myHouse.rooms[3].name = "Channel 4";
    myHouse.rooms[3].wifi = false;
    myHouse.rooms[3].isDefined = true;
    myHouse.rooms[3].colour = color(5, 0, 255);

    myHouse.rooms[4].name = "Channel 5";
    myHouse.rooms[4].wifi = false;
    myHouse.rooms[4].isDefined = true;
    myHouse.rooms[4].colour = color(5, 0, 255);

    myHouse.rooms[5].name = "Channel 6";
    myHouse.rooms[5].wifi = false;
    myHouse.rooms[5].isDefined = true;
    myHouse.rooms[5].colour = color(5, 0, 255);

    myHouse.rooms[6].name = "Channel 7";
    myHouse.rooms[6].wifi = false;
    myHouse.rooms[6].isDefined = true;
    myHouse.rooms[6].colour = color(5, 0, 255);

    myHouse.rooms[7].name = "Channel 8";
    myHouse.rooms[7].wifi = false;
    myHouse.rooms[7].isDefined = true;
    myHouse.rooms[7].colour = color(5, 0, 255);

    myHouse.rooms[8].name = "Channel 9";
    myHouse.rooms[8].wifi = false;
    myHouse.rooms[8].isDefined = true;
    myHouse.rooms[8].colour = color(5, 0, 255);

    myHouse.rooms[9].name = "Channel 10";
    myHouse.rooms[9].wifi = false;
    myHouse.rooms[9].isDefined = true;
    myHouse.rooms[9].colour = color(5, 0, 255);


    for (int i = 0; i < myHouse.rooms.length; i++) {
        myHouse.rooms[i].buttons[88].x = (bblock * 0.125f);
        myHouse.rooms[i].buttons[88].y = (bblock / 2) + bblock * 10.4f;
    }

    buttonset();
    updateall();
    saveempty();



}
    public void draw(){
        background(mychannel * 20 + 20);
        //time=frameCount%61;
        if (num == 0) {

            for (int i = 0; i < bbuttons.length; i++) {
                bbuttons[i].display();
            }
            for (int i = 0; i < cbuttons.length; i++) {
                cbuttons[i].display();
                dbuttons[i].display();
            }//fill(255);
            for (int i = 0; i < ebuttons.length; i++) {
                ebuttons[i].display();
            }
        }

        myHouse.display();

        ///sliderH
        for (Test1 t:instances1)
            t.run();
        ////

        fill(255, 0, 0);
        if (num == 0) {
            text(str(myHouse.rooms[mychannel - 1].bank), (bblock * 1.2f) + bblock * (13.3f), (bblock / 2) + bblock * 7.3f);
            text(str(myHouse.rooms[mychannel - 1].prg), (bblock * 1.2f) + bblock * (13.3f), (bblock / 2) + bblock * 6.1f);
            rect((bblock * 1.2f) + bblock * mx, 0, bblock, bblock / 6);
        } else {
            rect((bblock * 0.250f) + bblock * mx * 1.3f, 0, bblock, bblock / 6);
        }
    }

    // ==========================================
    public void startseq() {
        if (start) {
           shedMidi.sendMidiStart();    //TODO HERE
          //  MainActivity.mysetplaying(start);    //TODO HERE
            thread("playSequence");
            //thread ("worker2");
            // thread("worker");
        }
    }

    public void mousePressed() {
        if (num == 0) {
            for (int i = 0; i < bbuttons.length; i++) {
                bbuttons[i].click(mouseX, mouseY);
            }

            for (int i = 0; i < cbuttons.length; i++) {
                cbuttons[i].click(mouseX, mouseY);
                dbuttons[i].click(mouseX, mouseY);
            }
            for (int i = 0; i < ebuttons.length; i++) {
                ebuttons[i].click(mouseX, mouseY);
            }


            for (int i = 0; i < myHouse.rooms[mychannel - 1].buttons.length; i++) {
                myHouse.rooms[mychannel - 1].buttons[i].click(mouseX, mouseY);
            }
            myHouse.rooms[mychannel - 1].buttons[80].clickStart(mouseX, mouseY);      //PLAYBUTTON calls startseq thread
        } else {
            myHouse.rooms[mychannel - 1].buttons[88].click(mouseX, mouseY);
        }

        myHouse.rooms[mychannel - 1].mhandler();
        // }
        /////SlideH
        for (Test1 t:instances1) {
            if (t.isOver()) {
                t.dragged = true;
                t.lock = true;
            }
        }
    }

    public void mouseReleased() {
        myHouse.rooms[mychannel - 1].mreleasedhandler();
        //unlock
        for (Test1 t:instances1)
        {
            t.lock = false;
            t.dragged = false;
        }
    }

    /////********


    //////*******
    public void getbpm(int thebpm) {
        ms = ((60000 / thebpm) / 4) / 6;
    //                MainActivity.mysetbpm(ms);      //!!!  // TODO SetSWING
        // ms = 60000/thebpm;
        mms = ((ms / 1000.0f) / 4.0f);
        cellLength = (long) (mms * 10000000000L);
        for (int i = 0; i < 96; i++) {
            noteStartTimes[i] = i * cellLength;
        }
//println(cellLength);
//println(ms);
        // println(mms);
    }

    public void getswing(float theswing) {

  //      MainActivity.mysetswing(theswing);   // TODO SetSWING
    }

    public void transposeit(int transpose){
        for (int i = 0; i < 16; i++) {
            myHouse.rooms[mychannel-1].nnote1[i]= myHouse.rooms[mychannel-1].note1[i]+ myHouse.rooms[mychannel-1].transpose;
            myHouse.rooms[mychannel-1].nnote2[i]=myHouse.rooms[mychannel-1].note2[i]+ myHouse.rooms[mychannel-1].transpose;
            myHouse.rooms[mychannel-1].nnote3[i]=myHouse.rooms[mychannel-1].note3[i]+ myHouse.rooms[mychannel-1]. transpose;
            myHouse.rooms[mychannel-1].nnote4[i]=myHouse.rooms[mychannel-1].note4[i]+ myHouse.rooms[mychannel-1].transpose;
            myHouse.rooms[mychannel-1].nnote5[i]=myHouse.rooms[mychannel-1].note5[i]+ myHouse.rooms[mychannel-1].transpose;
        }



        // myHouse.rooms[mychannel-1].rtable.setInt(myHouse.rooms[mychannel-1].rtable.getRowCount()-1, "NoteNumber",note);
        // if (myHouse.rooms[mychannel-1].rtable.getRowCount() > 0){
        for (TableRow row : myHouse.rooms[mychannel-1].rtable.rows()) {
            if (row.getInt("ButtonID") < 16) row.setInt("NoteNumber", myHouse.rooms[mychannel-1].note1[row.getInt("ButtonID")]+ myHouse.rooms[mychannel-1].transpose);
            if  ((row.getInt("ButtonID") >= 16) && (row.getInt("ButtonID") < 32)) row.setInt("NoteNumber", myHouse.rooms[mychannel-1].note2[row.getInt("ButtonID")%16]+ myHouse.rooms[mychannel-1].transpose);
            if  ((row.getInt("ButtonID") >= 32) && (row.getInt("ButtonID") < 48)) row.setInt("NoteNumber", myHouse.rooms[mychannel-1].note3[row.getInt("ButtonID")%16]+ myHouse.rooms[mychannel-1].transpose);
            if  ((row.getInt("ButtonID") >= 48) && (row.getInt("ButtonID") < 64)) row.setInt("NoteNumber", myHouse.rooms[mychannel-1].note4[row.getInt("ButtonID")%16]+ myHouse.rooms[mychannel-1].transpose);
            if  ((row.getInt("ButtonID") >= 64) && (row.getInt("ButtonID") < 80)) row.setInt("NoteNumber", myHouse.rooms[mychannel-1].note5[row.getInt("ButtonID")%16]+ myHouse.rooms[mychannel-1].transpose);



            // row.setInt("NoteNumber", (row.getInt("NoteNumber"))+myHouse.rooms[mychannel-1].transpose);
        }

        //}
        myHouse.updatewtable();
    }
    public void makechord(){
        for (int i = 0; i < 16; i++) {
            myHouse.rooms[mychannel-1].note1[i]= myHouse.rooms[mychannel-1].pitch1[i];
            myHouse.rooms[mychannel-1].note2[i]=myHouse.rooms[mychannel-1].pitch1[i]+ myHouse.rooms[mychannel-1].chord1[0];
            myHouse.rooms[mychannel-1].note3[i]=myHouse.rooms[mychannel-1].pitch1[i]+myHouse.rooms[mychannel-1]. chord1[1];
            myHouse.rooms[mychannel-1].note4[i]=myHouse.rooms[mychannel-1].pitch1[i]+myHouse.rooms[mychannel-1]. chord1[2];
            myHouse.rooms[mychannel-1].note5[i]=myHouse.rooms[mychannel-1].pitch1[i]+myHouse.rooms[mychannel-1]. chord1[3];
        }
    }
    public void makechordall(){
        for (int j = 0; j < numchannels; j++) {
            for (int i = 0; i < 16; i++) {
                if (j==9){
                    myHouse.rooms[j].note1[i]=35;
                    myHouse.rooms[j].note2[i]=40;
                    myHouse.rooms[j].note3[i]=42;
                    myHouse.rooms[j].note4[i]=44;
                    myHouse.rooms[j].note5[i]=46;
                }else{
                    myHouse.rooms[j].note1[i]= myHouse.rooms[j].pitch1[i];
                    myHouse.rooms[j].note2[i]=myHouse.rooms[j].pitch1[i]+ myHouse.rooms[j].chord1[0];
                    myHouse.rooms[j].note3[i]=myHouse.rooms[j].pitch1[i]+myHouse.rooms[j]. chord1[1];
                    myHouse.rooms[j].note4[i]=myHouse.rooms[j].pitch1[i]+myHouse.rooms[j]. chord1[2];
                    myHouse.rooms[j].note5[i]=myHouse.rooms[j].pitch1[i]+myHouse.rooms[j]. chord1[3];
                }}}
    }

    public void transposeall(){
        for (int j = 0; j < numchannels; j++) {
            for (int i = 0; i < 16; i++) {
                myHouse.rooms[j].nnote1[i]= myHouse.rooms[j].note1[i]+ myHouse.rooms[j].transpose;
                myHouse.rooms[j].nnote2[i]=myHouse.rooms[j].note2[i]+ myHouse.rooms[j].transpose;
                myHouse.rooms[j].nnote3[i]=myHouse.rooms[j].note3[i]+ myHouse.rooms[j]. transpose;
                myHouse.rooms[j].nnote4[i]=myHouse.rooms[j].note4[i]+ myHouse.rooms[j].transpose;
                myHouse.rooms[j].nnote5[i]=myHouse.rooms[j].note5[i]+ myHouse.rooms[j].transpose;
            }}


        for (int i = 0; i < numchannels; i++) {
            for (TableRow row : myHouse.rooms[i].rtable.rows()) {
                if (row.getInt("ButtonID") < 16) row.setInt("NoteNumber", myHouse.rooms[i].note1[row.getInt("ButtonID")]+ myHouse.rooms[i].transpose);
                if  ((row.getInt("ButtonID") >= 16) && (row.getInt("ButtonID") < 32)) row.setInt("NoteNumber", myHouse.rooms[i].note2[row.getInt("ButtonID")%16]+ myHouse.rooms[i].transpose);
                if  ((row.getInt("ButtonID") >= 32) && (row.getInt("ButtonID") < 48)) row.setInt("NoteNumber", myHouse.rooms[i].note3[row.getInt("ButtonID")%16]+ myHouse.rooms[i].transpose);
                if  ((row.getInt("ButtonID") >= 48) && (row.getInt("ButtonID") < 64)) row.setInt("NoteNumber", myHouse.rooms[i].note4[row.getInt("ButtonID")%16]+ myHouse.rooms[i].transpose);
                if  ((row.getInt("ButtonID") >= 64) && (row.getInt("ButtonID") < 80)) row.setInt("NoteNumber", myHouse.rooms[i].note5[row.getInt("ButtonID")%16]+ myHouse.rooms[i].transpose);
            }}
    }



    public void allvol(){
        for (int j = 0; j < numchannels; j++) {
            myHouse.rooms[j].vol = PApplet.parseInt(map(myHouse.rooms[j].volume, bblock,(height-bblock-bblock), 127, 0));
            if (myHouse.rooms[j].vol > 127) myHouse.rooms[j].vol=127;
           shedMidi.sendMidiVol((j+1),myHouse.rooms[j].vol); //TODO HERE
            println(myHouse.rooms[j].volume + "VOLUME");
            println(myHouse.rooms[j].vol + "VOL");
        }
    }
    public void allrel(){
        for (int j = 0; j < numchannels; j++) {
            myHouse.rooms[j].rel = PApplet.parseInt(map(myHouse.rooms[j].release, bblock,(height-bblock-bblock), 127, 0));
            shedMidi.sendMidiRel((j+1),myHouse.rooms[j].rel);   //TODO HERE
        }
    }

    public void alltranspose(){
        for (int j = 0; j < numchannels; j++) {
            myHouse.rooms[j].transpose = PApplet.parseInt(map(myHouse.rooms[j].transp, bblock,(height-bblock-bblock), 48, -48));
            //shedMidi.sendMidiVol((j+1),myHouse.rooms[j].vol);
        }
    }
    /////
    public void sendme(byte[] mybuffer){
        // buffer1[0] =  (byte) (mybuffer[0]+256); //Channel
        // buffer1[1] = (byte) (mybuffer[1]);
        // buffer1[2] = (byte) (mybuffer[2]);
        // post is non-blocking
        try {
            //shedMidi.inputPort.send(mybuffer, 0, 3); //TODO HERE
            // println("data sent");
        }
        catch (Exception e) {
            //println("error sending midi data");
        }
    }

    public void setstep(){
        mx=stepbuf.get(0);
        for (int j = 0; j < 10; j++) {        ////NOTE OFF
            //dddata[0] = (unsigned char) (176 + j);
            buffer[0] = (byte) (176 + j);
            buffer[1] = 123;
            buffer[2] = 0;
            try {
              //  shedMidi.inputPort.send(buffer, 0, 3);  //TODO HERE
                // println("data sent");
            }
            catch (Exception e) {
                //println("error sending midi data");
            }


        }
    }

    //call as a thread e.g. as in mousePressed()
    public void playSequence() {
        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
       // android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_URGENT_AUDIO);

        index = 0 ;
        rx= 0;
        //
      //  MainActivity.processData(buf,buf1,buf2,buf3,ddata,stepbuf);
      //  MainActivity.myPlaysequence2();

        //////MainActivity.playit();
        long startTime = System.nanoTime();
        while (start) {
            if (System.nanoTime() > startTime +cellLength) {
                startTime = System.nanoTime();

                 allnotesoff();
              /*  for (int j = 0; j < 10; j++) {
                    buffer1[0] = (byte)(176 + j+1); //Channel
                    buffer1[1] = (byte)(123);
                    buffer1[2] = (byte)(0);
                    // post is non-blocking
                    try {
                       // shedMidi.inputPort.send(buffer1, 0, 3);   //TODO HERE

                        // println("data sent");
                    }
                    catch (Exception e) {
                        //println("error sending midi data");
                    }

                }*/
//myHouse.checknotes();
                for (int x = 0; x < tick.length; x++) {
                    if (tick[x]== index){
                        buffer[0] = (byte)(0x90 + channel[x]-1); //Channel
                        buffer[1] = (byte) notenum[x];
                        buffer[2] = (byte) velo[x];
                        //playit
                        // post is non-blocking
                        try {
                           // shedMidi.inputPort.send(buffer, 0, 3);   //TODO SEND HERE THEMIDIBUS
                            shedMidi.sendMidi(notenum[x], velo[x], channel[x]);    //TODO
                           // shedMidi.myBus.sendMessage(buffer);

                             //println("TRIGGER");
                          //  shedMidi.sendMidi();
                        }
                        catch (Exception e) {
                            //println("error sending midi data");
                        }
                    }
                }
                rx += 1;
                mx= rx % 16;

                index++ ;

            }
            if (index == 16 && loopPlay) { //reset things
                index = 0 ;
                //  rx= 0;

            }

        }

    }  //playsequence

    public void sendme1() {

        buffer1[0] = (byte) (ddata.get(0) + 256); //Channel
        buffer1[1] = ddata.get(1);
        buffer1[2] = ddata.get(2);
        // post is non-blocking
        try {
            // shedMidi.inputPort.send(buffer1, 0, 3);   //TODO sendHERE
            // println("data sent");
        } catch (Exception e) {
            //println("error sending midi data");
        }
    }
//_________*****
        public void mylist () {
            loadfile = true;
            // Following lists file in a directory
            //    String path = Environment.getExternalStorageDirectory().toString()+"/SENSN";    //TODO HERE PATH
            String path = "./SENSN";        // TODO HERE PATH
            println("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();
            println("Files", "Size: " + files.length);
            colorlist.clear();
            d1.clear();
            int i = 0;
            colorlist.add("Cancel");
            d1.addItem("Cancel",i+1);
            for ( i = 0; i < files.length; i++) {
                colorlist.add(files[i].getName());
                println("Files", "FileName:" + files[i].getName());

                d1.addItem(files[i].getName(), i+1);

            }


            //selectionlist = new KetaiList(this, colorlist);            // TODO HERE SELECTION
        }  // method

        public void saveit () {
            loadfile = false;
             //saveTable(table,"/"+mypath+"/SENSN/abc.csv");
             //saveTable(table,mypath+"/SENSN/abc.csv");
            //  String path = Environment.getExternalStorageDirectory().toString()+"/SENSN";    // TODO HERE PATH
              String path = "./SENSN";    // TODO HERE PATH
            println("Files", "Path: " + path);
            File directory = new File(path);
            File[] files = directory.listFiles();

            println("Files", "Size: " + files.length);
            filesl = PApplet.parseInt(files.length);
            colorlist.clear();
            d1.clear();

            colorlist.add("Cancel");
            d1.addItem("Cancel",0);
            colorlist.add("NewFile");
            d1.addItem("New File",1);
            int i = 0;
            for (i = 0; i < files.length; i++) {
                colorlist.add(files[i].getName());
                d1.addItem(files[i].getName(),i+2);
                println("Files", "FileName:" + files[i].getName());
            }

            //  selectionlist = new KetaiList(this, colorlist);   TODO HERE SELECTION IMPLEMENT

        }

      /*  public void onKetaiListSelection(KetaiList klist)
        {
            String selection = klist.getSelection();
            // println(selection);
            if (loadfile){
                if (selection == "Cancel"){println(selection);}
                else {
                    println(selection);
                    table = loadTable("/"+mypath+"/SENSN/"+selection, "header");
                    tabcentry=0;
                    tableget();
                    updateall();
                    dbuttons[0].on = true;
                    for (int i = 1; i < dbuttons.length; i++) {
                        dbuttons[i].on = false;}
                    loadfile=false;
                }
            }else{
                if (selection == "NewFile"){
                    saveTable(table,"/"+mypath+"/SENSN/abc"+filesl+".csv");}
                else if (selection == "Cancel"){}
                else {
                    saveTable(table,"/"+mypath+"/SENSN/"+selection);}

            }
        }*/
        // =====================================================
//____________***

        //----- HOUSE ROOM ETC  ----
        class House {

            // default values for a house
            int colour = color(0, 255, 0);
            int size = 310;
            int houseNumber = 23;
            String street = "SEQ";
            String name = "Sensn";

            // array of rooms in the house
            Room[] rooms = new Room[10];

            // constructor
            House() {
                // pre init rooms
                for (int i = 0; i < rooms.length; i++) {
                    rooms[i] = new Room();
                }
            }//constructor

            // method
            public void display() {
                // display house
                fill(colour);
                text(name + "\n" + street + " " + houseNumber, 44, 1200);

                float verticalDistanceRooms = 60;

                int i = 0;
                /// for (Room currentRoom : rooms) {

                // if (currentRoom.isDefined) {
                //   currentRoom.display(244, i*verticalDistanceRooms+88 );
                myHouse.rooms[mychannel - 1].display(244, i * verticalDistanceRooms + 88);
                stroke(255);
                // horizontal line
                //  line (66, i*verticalDistanceRooms+88,
                //  210, i*verticalDistanceRooms+88);
                // vertical line
                // line (66, 0*verticalDistanceRooms+88-20,
                //   66, i*verticalDistanceRooms+88);

                // i++;
                //  }
                // }//for
            }//method

            //
            public void checknotes() {


                for (int x = 0; x < tick.length; x++) {
                    if (tick[x] == index) {
                          buffer[0] = (byte)(0x90 + channel[x]-1); //Channel
                          buffer[1] = (byte) notenum[x];
                          buffer[2] = (byte) velo[x];

                        // post is non-blocking
                        try {
                         //  shedMidi.inputPort.send(buffer, 0, 3);    //TODO
                           shedMidi.sendMidi(notenum[x], velo[x], channel[x]);    //TODO
                            // println("data sent");
                        } catch (Exception e) {
                            //println("error sending midi data");
                        }
                    }
                }
            } //method


            public void updatewtable() {
                wtable.clearRows();
                int j = 0;
                for (Room currentRoom : rooms) {
                    if (myHouse.rooms[j].rtable.getRowCount() > 0) {
                        wtable.addRows(myHouse.rooms[j].rtable);
                    }
                    j++;
                }//rooms
                wtable.sort(2);
                ids = new int[wtable.getRowCount()];
                tick = new int[wtable.getRowCount()];
                channel = new int[wtable.getRowCount()];
                notenum = new int[wtable.getRowCount()];
                velo = new int[wtable.getRowCount()];
                buf = ByteBuffer.allocateDirect(wtable.getRowCount());
                buf1 = ByteBuffer.allocateDirect(wtable.getRowCount());
                buf2 = ByteBuffer.allocateDirect(wtable.getRowCount());
                buf3 = ByteBuffer.allocateDirect(wtable.getRowCount());
                buf.clear();
                buf1.clear();
                buf2.clear();
                buf3.clear();
                int i = 0;
                for (TableRow row : wtable.rows()) {
                    // ids[i] = (byte)266;
                    ids[i] = (byte) row.getInt(0);
                    tick[i] = (byte) row.getInt(2);
                    notenum[i] = (byte) row.getInt(3);
                    velo[i] = (byte) row.getInt(4);
                    channel[i] = (byte) row.getInt(5);

                    buf.put(i, (byte) row.getInt(2));
                    buf1.put(i, (byte) row.getInt(3));
                    buf2.put(i, (byte) row.getInt(4));
                    buf3.put(i, (byte) row.getInt(5));
                    i++;

                }
                //// synchronized (tick){
             //   // MainActivity.myarray(tick,notenum,velo,channel);
                if (start) {
                  //  MainActivity.processData(buf, buf1, buf2, buf3, ddata, stepbuf);     //TODO
                    //// buf.flip();

                }


            //    //printArray(tick);

               // // println(row.getInt("Channel") + "Channel");
               // //  println(wtable.getRowCount()+ "Count");
            }


        }//class

        class Room {
            float transp;
            float volume;
            int vol;
            float release;
            int rel;
            int transpose;
            int prg = 0;
            int bank = 0;
            int[] pitch1 = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            int[] note1 = new int[16];
            int[] note2 = new int[16];
            int[] note3 = new int[16];
            int[] note4 = new int[16];
            int[] note5 = new int[16];
            int[] nnote1 = new int[16];
            int[] nnote2 = new int[16];
            int[] nnote3 = new int[16];
            int[] nnote4 = new int[16];
            int[] nnote5 = new int[16];
            int[] chord1 = {3, 7, 10, 14};
            int block = bblock;
            // default values for a room
            int colour = color(255, 0, 0);
            int windows = 2;
            String name = "";
            int mc = 0;
            boolean wifi = false;
            boolean isDefined = false;
            Test[] instances = new Test[4];
            Button[] buttons = new Button[101];
            // no constructor
            Table rtable;


            Room() {
                rtable = new Table();
                rtable.setColumnTitles(new String[]{
                        "ID", "ButtonID", "Tick", "NoteNumber", "Velocity", "Channel"
                });
                rtable.setColumnType("ID", Table.INT);
                rtable.setColumnType("ButtonID", Table.INT);
                rtable.setColumnType("Tick", Table.INT);
                rtable.setColumnType("NoteNumber", Table.INT);
                rtable.setColumnType("Velocity", Table.INT);
                rtable.setColumnType("Channel", Table.INT);


                // A loop to evenly space out the buttons along the window
                for (int y = 0; y < 5; y++) {
                    for (int x = 0; x < 16; x++) {

                        buttons[mc] = new Button((block * 1.2f) + block * x, (block / 2) + block * y, block * 0.9f, block * 0.9f, inventory.get(mc), x, x + (16 * y));
                        mc += 1;
                    }
                }
                for (int x = 0; x < 8; x++) {
                    buttons[x + 80] = new Button((block * 1.2f) + block * (2 * x), (block / 2) + block * 5.5f, block * 0.9f, block * 0.9f, inventory.get(x + 80), -1, -1);
                }
                for (int x = 0; x < 8; x++) {
                    buttons[x + 88] = new Button((block * 1.2f) + block * (2 * x), (block / 2) + block * 6.7f, block * 0.9f, block * 0.9f, inventory.get(x + 88), -1, -1);
                }
                for (int y = 0; y < 5; y++) {
                    buttons[y + 96] = new Button((block * 0.125f), (block / 2) + block * y, block * 0.9f, block * 0.9f, inventory.get(y + 96), y + 96, y + 96);
                }
                instances[0] = new Test((block * 1.5f) + block * (15.7f), block, block, block, 1);
                instances[1] = new Test((block * 1.5f) + block * (16.7f), block, block, block, 2);
                instances[2] = new Test((block * 1.5f) + block * (17.7f), block, block, block, 3);
                instances[3] = new Test((block * 1.5f) + block * (18.7f), block, block, block, 4);
            }

            // method
            public void display(float x, float y) {
                // background(100);
                if (num == 0) {
                    for (int i = 0; i < buttons.length; i++) {
                        buttons[i].display();
                    }
                } else {
                    buttons[88].display();
                }

                //call run method...
                for (Test t : instances) {
                    t.run();
                }
                // check wifi
                String hasWifiText = "has no wifi"; // default text
                if (wifi) {
                    hasWifiText = "has wifi"; // text for room that has wifi
                }

                // display room using color, name and wifi text etc.
                fill(colour);
                text(name, (x + 60), (y + 1200));

            }

            public void mhandler() {
                //lock if clicked
                for (Test t : instances) {
                    if (t.isOver())

                        t.lock = true;
                    t.dragged = true;
                }
            }

            public void mreleasedhandler() {
                //unlock
                for (Test t : instances) {
                    t.lock = false;
                    t.dragged = false;
                }

            }

            public void sliderset() {
                //float lowerY = height - h - initialY;
                tempo = instances[0].y;
                transp = instances[1].y;
                volume = instances[2].y;
                release = instances[3].y;
                instances = new Test[16];
                for (int i = 0; i < 16; i++) {
                    instances[i] = new Test((block * 0.250f) + block * i * 1.3f, block, block * 1.3f, block, i + 8);
                    //instances[i] = new Test(i*160+25, 200, 160, 200,i+8);
                    instances[i].y = PApplet.parseInt(map(pitch1[i], 127, 0, block, (height - block - block)));

                }


            }

            public void sliderreset() {
                instances = new Test[4];
                instances[0] = new Test((block * 1.5f) + block * (15.7f), block, block, block, 1);
                instances[1] = new Test((block * 1.5f) + block * (16.7f), block, block, block, 2);
                instances[2] = new Test((block * 1.5f) + block * (17.7f), block, block, block, 3);
                instances[3] = new Test((block * 1.5f) + block * (18.7f), block, block, block, 4);
                instances[0].y = tempo;
                instances[1].y = transp;
                instances[2].y = volume;
                instances[3].y = release;

            }

            public void sliderget() {
                transp = instances[1].y;
            }

            public void slidergetv() {
                volume = instances[2].y;
            }

            public void slidergetr() {
                release = instances[3].y;
                //
            }
        } //class

        //// WURSCHT

        public void allnotesoff () {
            for (int j = 0; j < numchannels; j++) {
                shedMidi.sendMidialloff(j + 1);    //TODO
            }
            //for (int j = 0; j < numchannels; j++) {
            // for (int i = 0; i < 127; i++) {
            // shedMidi.sendMidioff(i,j+1);
            // }}

        }
        class BButton {

            public void setX(float x,float y,float w,float h) {
                this.x = x;
                this.y = y;
                this.w = w;
                this.h = h;
            }

            // Button location and size
            float x;
            float y;
            float w;
            float h;
            // Is the button on or off?
            boolean on;
            int col;

            // Constructor initializes all variables
            BButton(float tempX, float tempY, float tempW, float tempH) {
                x = tempX;
                y = tempY;
                w = tempW;
                h = tempH;
                on = false;  // Button always starts as off
            }

            public void click(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
                    for (int j = 0; j < bbuttons.length; j++) {
                        bbuttons[j].on = false;
                    }
                    on = !on;
                    checkbuttons();
                }
            }

            public void clickStart(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
   /* if (start) {
     start = false;
     loopPlay = false ;}
   else{
   start=true;
   loopPlay = true ;
    startseq();
  }
 */
                }
            }

            // Draw the rectangle
            public void display() {
                rectMode(CORNER);
                stroke(0);
                // The color changes based on the state of the button
                if (on) {
                    fill(175);
                } else {
                    fill(0);

                }
                rect(x, y, w, h);
            }

            public void checkbuttons() {
                if (bbuttons[0].on == true) {
                    mychannel = 1;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[1].on == true) {
                    mychannel = 2;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[2].on == true) {
                    mychannel = 3;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[3].on == true) {
                    mychannel = 4;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[4].on == true) {
                    mychannel = 5;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[5].on == true) {
                    mychannel = 6;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[6].on == true) {
                    mychannel = 7;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[7].on == true) {
                    mychannel = 8;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[8].on == true) {
                    mychannel = 9;
                    settempy();
                    println(mychannel);
                }
                if (bbuttons[9].on == true) {
                    mychannel = 10;
                    settempy();
                    println(mychannel);
                }


            }//method

            public void settempy() {
                for (int i = 0; i < myHouse.rooms.length; i++) {
                    myHouse.rooms[i].instances[0].y = tempo;
                }
            }

        }
        class Button {
            public void setX(float x,float y,float w,float h) {
                this.x = x;
                this.y = y;
                this.w = w;
                this.h = h;
            }
            // Button location and size
            float x;
            float y;
            float w;
            float h;
            // Is the button on or off?
            boolean on;
            String bname;
            int tick;
            int buttonid;

            // Constructor initializes all variables
            Button(float tempX, float tempY, float tempW, float tempH, String tempstr, int tempTick, int tempButtonid) {
                x = tempX;
                y = tempY;
                w = tempW;
                h = tempH;
                on = false;  // Button always starts as off
                bname = tempstr;
                tick = tempTick;
                buttonid = tempButtonid;
            }

            public void click(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
                    on = !on;
                    if ((buttonid > -1) && (buttonid < 80)) {
                        if (on) {
                            println("on");
                            for (int j = 0; j < 5; j++) {
                                //     if (myHouse.rooms[mychannel-1].buttons[j+96].on){
                                if ((buttonid >= 0 + (16 * j)) && (buttonid < 16 + (16 * j)) && (myHouse.rooms[mychannel - 1].buttons[j + 96].on)) {

                                    myHouse.rooms[mychannel - 1].rtable.addRow();
                                    myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ID", myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1);
                                    myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Tick", tick);
                                    myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ButtonID", buttonid);
                                    //note = ((buttonid/16)%16)+50;
                                    if (buttonid < 16) note = myHouse.rooms[mychannel - 1].note1[buttonid];
                                    if ((buttonid >= 16) && (buttonid < 32))
                                        note = myHouse.rooms[mychannel - 1].note2[buttonid % 16];
                                    if ((buttonid >= 32) && (buttonid < 48))
                                        note = myHouse.rooms[mychannel - 1].note3[buttonid % 16];
                                    if ((buttonid >= 48) && (buttonid < 64))
                                        note = myHouse.rooms[mychannel - 1].note4[buttonid % 16];
                                    if ((buttonid >= 64) && (buttonid < 80))
                                        note = myHouse.rooms[mychannel - 1].note5[buttonid % 16];
                                    myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "NoteNumber", note);
                                    myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Velocity", 100);
                                    myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Channel", mychannel);
                                    // println(myHouse.rooms[mychannel-1].table.getInt(myHouse.rooms[mychannel-1].table.getRowCount()-1, "Tick")+" tick ADDED" + " - TOTAL ROWS "+ myHouse.rooms[mychannel-1].table.getRowCount());   //DEBUG PRINT
                                    // println(myHouse.rooms[mychannel-1].table.getInt(myHouse.rooms[mychannel-1].table.getRowCount()-1, "NoteNumber"));
                                    transposeit(1);
                                }
                            }

                        }
                        if (!on) {
                            println("off");
                            for (int i = 0; i < 5; i++) {
                                if ((buttonid >= 0 + (16 * i)) && (buttonid < 16 + (16 * i)) && (myHouse.rooms[mychannel - 1].buttons[i + 96].on)) {
                                    TableRow result = myHouse.rooms[mychannel - 1].rtable.findRow(str(buttonid), "ButtonID");
                                    int removeit = result.getInt("ID");        //int removeit = table.findRowIndex(str(buttonid),"ButtonID" );
                                    myHouse.rooms[mychannel - 1].rtable.removeRow(removeit);
                                    // println("Removed row ID" +removeit+ " - TOTAL ROWS "+ myHouse.rooms[mychannel-1].table.getRowCount());   //DEBUG PRINT
                                    int j = 0;                              //write new indices to the table since we removed a line from it
                                    for (TableRow row : myHouse.rooms[mychannel - 1].rtable.rows()) {
                                        myHouse.rooms[mychannel - 1].rtable.setInt(j, "ID", j);
                                        j++;
                                    }
                                }
                            }

                            // for (TableRow row : myHouse.rooms[mychannel-1].table.rows()) {
                            //println(row.getInt("Tick")+" Tick");
                            //println(table.getRowCount()+"count");
                            // }

                        }

                    } // if buttonid>-1


                    checkMute();
                    checkbuttons();
                    myHouse.updatewtable();
                }
            }

            public void clickStart(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
                    if (start) {
                        for (int i = 0; i < numchannels; i++) {
                            myHouse.rooms[i].buttons[80].on = false;
                        }
                        start = false;
                       // MainActivity.mysetplaying(start);    //TODO
                        loopPlay = false;
                        // allnotesoff();
                        for (int j = 0; j < 10; j++) {
                            buffer1[0] = (byte) (176 + j); //Channel
                            buffer1[1] = (byte) (123);
                            buffer1[2] = (byte) (0);
                            // post is non-blocking
                            try {
                            //    shedMidi.inputPort.send(buffer1, 0, 3);     //TODO
                                // println("data sent");
                            } catch (Exception e) {
                                //println("error sending midi data");
                            }

                        }


                    } else {
                        for (int i = 0; i < numchannels; i++) {
                            myHouse.rooms[i].buttons[80].on = true;
                        }
                        start = true;
                       // MainActivity.mysetplaying(start);    //TODO
                        loopPlay = true;
                        startseq();
                    }

                }
            }

            // Draw the rectangle
            public void display() {
                rectMode(CORNER);
                stroke(0);
                // The color changes based on the state of the button
                if (on) {
                    fill(175);
                } else {
                    fill(0);

                }
                rect(x, y, w, h);
                textSize(bblock / 4);
                fill(200);
                text(bname, x + bblock / 5, y + bblock / 2);
            }

            public void checkbuttons() {
                for (int i = 0; i < numchannels; i++) {
                    if (myHouse.rooms[i].buttons[88].on == true) {
                        num += 1;
                        myHouse.rooms[i].buttons[88].on = false;
                    }
                    if (num == 1) {
                        myHouse.rooms[i].sliderset();
                        num += 1;
                    }
                    if (num == 3) {
                        myHouse.rooms[i].sliderreset();
                        num = 0;
                    }

                    //TRANSPOSE Buttons
                    if (myHouse.rooms[i].buttons[81].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note1[j] += 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[81].on = false;
                    }
                    if (myHouse.rooms[i].buttons[82].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note2[j] += 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[82].on = false;
                    }
                    if (myHouse.rooms[i].buttons[83].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note3[j] += 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[83].on = false;
                    }
                    if (myHouse.rooms[i].buttons[84].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note4[j] += 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[84].on = false;
                    }
                    if (myHouse.rooms[i].buttons[85].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note5[j] += 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[85].on = false;
                    }

                    if (myHouse.rooms[i].buttons[86].on == true) {
                        myHouse.rooms[mychannel - 1].prg += 1;
                        shedMidi.sendMidiBank(mychannel, myHouse.rooms[mychannel - 1].bank, myHouse.rooms[mychannel - 1].prg);   //TODO HERE
                        myHouse.rooms[i].buttons[86].on = false;
                    }

                    if (myHouse.rooms[i].buttons[87].on == true) {
                        myHouse.rooms[mychannel - 1].prg -= 1;
                        shedMidi.sendMidiBank(mychannel, myHouse.rooms[mychannel - 1].bank, myHouse.rooms[mychannel - 1].prg);   //TODO HERE
                        myHouse.rooms[i].buttons[87].on = false;
                    }

                    if (myHouse.rooms[i].buttons[89].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note1[j] -= 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[89].on = false;
                    }
                    if (myHouse.rooms[i].buttons[90].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note2[j] -= 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[90].on = false;
                    }
                    if (myHouse.rooms[i].buttons[91].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note3[j] -= 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[91].on = false;
                    }
                    if (myHouse.rooms[i].buttons[92].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note4[j] -= 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[92].on = false;
                    }
                    if (myHouse.rooms[i].buttons[93].on == true) {
                        for (int j = 0; j < 16; j++) {
                            myHouse.rooms[i].note5[j] -= 12;
                            transposeit(PApplet.parseInt(myHouse.rooms[i].transpose));
                        }
                        myHouse.rooms[i].buttons[93].on = false;
                    }

                    if (myHouse.rooms[i].buttons[94].on == true) {
                        myHouse.rooms[mychannel - 1].bank += 1;
                       shedMidi.sendMidiBank(mychannel, myHouse.rooms[mychannel - 1].bank, myHouse.rooms[mychannel - 1].prg);  //TODO
                        myHouse.rooms[i].buttons[94].on = false;
                    }

                    if (myHouse.rooms[i].buttons[95].on == true) {
                        myHouse.rooms[mychannel - 1].bank -= 1;
                        shedMidi.sendMidiBank(mychannel, myHouse.rooms[mychannel - 1].bank, myHouse.rooms[mychannel - 1].prg);  //TODO
                        myHouse.rooms[i].buttons[95].on = false;
                    }

                    //Mute buttons


                }
            }


            public void checkMute() {

                if (buttonid == 96) {
                    if (!on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x].on) {
                                TableRow result = myHouse.rooms[mychannel - 1].rtable.findRow(str(x), "ButtonID");
                                int removeit = result.getInt("ID");        //int removeit = table.findRowIndex(str(buttonid),"ButtonID" );
                                myHouse.rooms[mychannel - 1].rtable.removeRow(removeit);
                                // println("Removed row ID" +removeit+ " - TOTAL ROWS "+ myHouse.rooms[mychannel-1].table.getRowCount());   //DEBUG PRINT
                                int j = 0;                              //write new indices to the table since we removed a line from it
                                for (TableRow row : myHouse.rooms[mychannel - 1].rtable.rows()) {
                                    myHouse.rooms[mychannel - 1].rtable.setInt(j, "ID", j);
                                    j++;
                                }
                            }
                        }

                    }
                    if (on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x].on) {
                                myHouse.rooms[mychannel - 1].rtable.addRow();
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ID", myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Tick", myHouse.rooms[mychannel - 1].buttons[x].tick);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ButtonID", myHouse.rooms[mychannel - 1].buttons[x].buttonid);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "NoteNumber", 60);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Velocity", 80);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Channel", mychannel);
                                makechordall();
                                transposeall();
                            }
                        }

                    }

                } //96
                if (buttonid == 97) {
                    if (!on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x + 16].on) {
                                TableRow result = myHouse.rooms[mychannel - 1].rtable.findRow(str(x + 16), "ButtonID");
                                int removeit = result.getInt("ID");        //int removeit = table.findRowIndex(str(buttonid),"ButtonID" );
                                myHouse.rooms[mychannel - 1].rtable.removeRow(removeit);
                                // println("Removed row ID" +removeit+ " - TOTAL ROWS "+ myHouse.rooms[mychannel-1].table.getRowCount());   //DEBUG PRINT
                                int j = 0;                              //write new indices to the table since we removed a line from it
                                for (TableRow row : myHouse.rooms[mychannel - 1].rtable.rows()) {
                                    myHouse.rooms[mychannel - 1].rtable.setInt(j, "ID", j);
                                    j++;
                                }
                            }
                        }

                    }
                    if (on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x + 16].on) {
                                myHouse.rooms[mychannel - 1].rtable.addRow();
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ID", myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Tick", myHouse.rooms[mychannel - 1].buttons[x + 16].tick);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ButtonID", myHouse.rooms[mychannel - 1].buttons[x + 16].buttonid);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "NoteNumber", 60);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Velocity", 80);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Channel", mychannel);
                                makechordall();
                                transposeall();
                            }
                        }

                    }

                } //97
                if (buttonid == 98) {
                    if (!on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x + 32].on) {
                                TableRow result = myHouse.rooms[mychannel - 1].rtable.findRow(str(x + 32), "ButtonID");
                                int removeit = result.getInt("ID");        //int removeit = table.findRowIndex(str(buttonid),"ButtonID" );
                                myHouse.rooms[mychannel - 1].rtable.removeRow(removeit);
                                // println("Removed row ID" +removeit+ " - TOTAL ROWS "+ myHouse.rooms[mychannel-1].table.getRowCount());   //DEBUG PRINT
                                int j = 0;                              //write new indices to the table since we removed a line from it
                                for (TableRow row : myHouse.rooms[mychannel - 1].rtable.rows()) {
                                    myHouse.rooms[mychannel - 1].rtable.setInt(j, "ID", j);
                                    j++;
                                }
                            }
                        }

                    }
                    if (on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x + 32].on) {
                                myHouse.rooms[mychannel - 1].rtable.addRow();
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ID", myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Tick", myHouse.rooms[mychannel - 1].buttons[x + 32].tick);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ButtonID", myHouse.rooms[mychannel - 1].buttons[x + 32].buttonid);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "NoteNumber", 60);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Velocity", 80);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Channel", mychannel);
                                makechordall();
                                transposeall();
                            }
                        }

                    }

                } //98

                if (buttonid == 99) {
                    if (!on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x + 48].on) {
                                TableRow result = myHouse.rooms[mychannel - 1].rtable.findRow(str(x + 48), "ButtonID");
                                int removeit = result.getInt("ID");        //int removeit = table.findRowIndex(str(buttonid),"ButtonID" );
                                myHouse.rooms[mychannel - 1].rtable.removeRow(removeit);
                                // println("Removed row ID" +removeit+ " - TOTAL ROWS "+ myHouse.rooms[mychannel-1].table.getRowCount());   //DEBUG PRINT
                                int j = 0;                              //write new indices to the table since we removed a line from it
                                for (TableRow row : myHouse.rooms[mychannel - 1].rtable.rows()) {
                                    myHouse.rooms[mychannel - 1].rtable.setInt(j, "ID", j);
                                    j++;
                                }
                            }
                        }

                    }
                    if (on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x + 48].on) {
                                myHouse.rooms[mychannel - 1].rtable.addRow();
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ID", myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Tick", myHouse.rooms[mychannel - 1].buttons[x + 48].tick);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ButtonID", myHouse.rooms[mychannel - 1].buttons[x + 48].buttonid);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "NoteNumber", 60);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Velocity", 80);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Channel", mychannel);
                                makechordall();
                                transposeall();
                            }
                        }

                    }

                } //99

                if (buttonid == 100) {
                    if (!on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x + 64].on) {
                                TableRow result = myHouse.rooms[mychannel - 1].rtable.findRow(str(x + 64), "ButtonID");
                                int removeit = result.getInt("ID");        //int removeit = table.findRowIndex(str(buttonid),"ButtonID" );
                                myHouse.rooms[mychannel - 1].rtable.removeRow(removeit);
                                // println("Removed row ID" +removeit+ " - TOTAL ROWS "+ myHouse.rooms[mychannel-1].table.getRowCount());   //DEBUG PRINT
                                int j = 0;                              //write new indices to the table since we removed a line from it
                                for (TableRow row : myHouse.rooms[mychannel - 1].rtable.rows()) {
                                    myHouse.rooms[mychannel - 1].rtable.setInt(j, "ID", j);
                                    j++;
                                }
                            }
                        }

                    }
                    if (on) {
                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[mychannel - 1].buttons[x + 64].on) {
                                myHouse.rooms[mychannel - 1].rtable.addRow();
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ID", myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Tick", myHouse.rooms[mychannel - 1].buttons[x + 64].tick);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "ButtonID", myHouse.rooms[mychannel - 1].buttons[x + 64].buttonid);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "NoteNumber", 60);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Velocity", 80);
                                myHouse.rooms[mychannel - 1].rtable.setInt(myHouse.rooms[mychannel - 1].rtable.getRowCount() - 1, "Channel", mychannel);
                                makechordall();
                                transposeall();
                            }
                        }

                    }

                } //100


            }// method

        }
        public void buttonset () {

            bbuttons[0].on = true;
            cbuttons[0].on = true;
            dbuttons[0].on = true;

            for (int i = 0; i < numchannels; i++) {
                if (i != 9) myHouse.rooms[i].transpose = 48;
                for (int j = 0; j < 5; j++) {
                    myHouse.rooms[i].buttons[j + 96].on = true;
                }
            }
            for (int j = 0; j < numchannels; j++) {
                myHouse.rooms[j].instances[3].y = PApplet.parseInt(map(76, 127, 0, myHouse.rooms[j].instances[3].initialY, myHouse.rooms[j].instances[3].lowerY));
            }
            myHouse.rooms[9].instances[1].y = map(9, +48, -48, myHouse.rooms[9].instances[1].initialY, myHouse.rooms[9].instances[1].lowerY);
        }
        class CButton {
            public void setX(float x,float y,float w,float h) {
                this.x = x;
                this.y = y;
                this.w = w;
                this.h = h;
            }
            // Button location and size
            float x;
            float y;
            float w;
            float h;
            // Is the button on or off?
            boolean on;
            int col;

            // Constructor initializes all variables
            CButton(float tempX, float tempY, float tempW, float tempH) {
                x = tempX;
                y = tempY;
                w = tempW;
                h = tempH;
                on = false;  // Button always starts as off
            }

            public void click(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
                    for (int j = 0; j < cbuttons.length; j++) {
                        cbuttons[j].on = false;
                    }
                    on = !on;
                    checkbuttons();
                }
            }

            public void clickStart(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
   /* if (start) {
     start = false;
     loopPlay = false ;}
   else{
   start=true;
   loopPlay = true ;
    startseq();
  }
 */
                }
            }

            // Draw the rectangle
            public void display() {
                rectMode(CORNER);
                stroke(0);
                // The color changes based on the state of the button
                if (on) {
                    fill(175);
                } else {
                    fill(0);

                }
                rect(x, y, w, h);
            }

            public void checkbuttons() {
                if (cbuttons[0].on == true) {
                    tabentry = 0;

                }
                if (cbuttons[1].on == true) {
                    tabentry = 1;

                }
                if (cbuttons[2].on == true) {
                    tabentry = 2;

                }
                if (cbuttons[3].on == true) {
                    tabentry = 3;

                }
                if (cbuttons[4].on == true) {
                    tabentry = 4;

                }
                if (cbuttons[5].on == true) {
                    tabentry = 5;

                }
                if (cbuttons[6].on == true) {
                    tabentry = 6;

                }
                if (cbuttons[7].on == true) {
                    tabentry = 7;

                }


            }
        }
        public void check () {
            inventory = new StringList();
            for (int j = 0; j < 101; j++) {
                if (j > 80 && j <= 85) {
                    inventory.append("+12");
                } else {
                    if (j == 86) {
                        inventory.append("prg+");
                    } else {
                        if (j == 87) {
                            inventory.append("prg-");
                        } else {
                            if (j > 88 && j <= 93) {
                                inventory.append("-12");
                            } else {
                                if (j == 94) {
                                    inventory.append("bnk+");
                                } else {
                                    if (j == 95) {
                                        inventory.append("bnk-");
                                    } else {
                                        inventory.append("");
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        public void chordcheck () {

            for (int i = 0; i < 4; i++) {
                myHouse.rooms[mychannel - 1].chord1[i] = mychords[chordcount][i];
            }


        }
        class DButton {
            public void setX(float x,float y,float w,float h) {
                this.x = x;
                this.y = y;
                this.w = w;
                this.h = h;
            }
            // Button location and size
            float x;
            float y;
            float w;
            float h;
            // Is the button on or off?
            boolean on;
            int col;

            // Constructor initializes all variables
            DButton(float tempX, float tempY, float tempW, float tempH) {
                x = tempX;
                y = tempY;
                w = tempW;
                h = tempH;
                on = false;  // Button always starts as off
            }

            public void click(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
                    for (int j = 0; j < cbuttons.length; j++) {
                        dbuttons[j].on = false;
                    }
                    on = !on;
                    checkbuttons();
                }
            }

            public void clickStart(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
   /* if (start) {
     start = false;
     loopPlay = false ;}
   else{
   start=true;
   loopPlay = true ;
    startseq();
  }
 */
                }
            }

            // Draw the rectangle
            public void display() {
                rectMode(CORNER);
                stroke(0);
                // The color changes based on the state of the button
                if (on) {
                    fill(175);
                } else {
                    fill(0);

                }
                rect(x, y, w, h);
            }

            public void checkbuttons() {

                if (dbuttons[0].on == true) {
                    tabcentry = 0;
                    tableget();

                }
                if (dbuttons[1].on == true) {
                    tabcentry = 1;
                    tableget();

                }
                if (dbuttons[2].on == true) {
                    tabcentry = 2;
                    tableget();

                }
                if (dbuttons[3].on == true) {
                    tabcentry = 3;
                    tableget();

                }
                if (dbuttons[4].on == true) {
                    tabcentry = 4;
                    tableget();

                }
                if (dbuttons[5].on == true) {
                    tabcentry = 5;
                    tableget();

                }
                if (dbuttons[6].on == true) {
                    tabcentry = 6;
                    tableget();

                }
                if (dbuttons[7].on == true) {
                    tabcentry = 7;
                    tableget();

                }


            }
        }
        class EButton {
            public void setX(float x,float y,float w,float h) {
                this.x = x;
                this.y = y;
                this.w = w;
                this.h = h;
            }
            // Button location and size
            float x;
            float y;
            float w;
            float h;
            // Is the button on or off?
            boolean on;
            int col;
            String bname;
            String bname1;
            boolean sms = false;
          //  KetaiList selectionlist;   //TODO
            ArrayList<String> colorlist = new ArrayList<String>();

            // Constructor initializes all variables
            EButton(float tempX, float tempY, float tempW, float tempH, String tempstr, String tempstr1) {
                x = tempX;
                y = tempY;
                w = tempW;
                h = tempH;
                on = false;  // Button always starts as off
                bname = tempstr;
                bname1 = tempstr1;
                ArrayList<String> colorlist = new ArrayList<String>();
            }

            public void click(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {

                    on = !on;
                    checkbuttons();
                    sms = true;
                }
            }

            public void clickStart(int mx, int my) {
                // Check to see if a point is inside the rectangle
                if (mx > x && mx < x + w && my > y && my < y + h) {
   /* if (start) {
     start = false;
     loopPlay = false ;}
   else{
   start=true;
   loopPlay = true ;
    startseq();
  }
 */
                }
            }

            // Draw the rectangle
            public void display() {
                rectMode(CORNER);
                stroke(0);
                // The color changes based on the state of the button
                if (on) {
                    fill(175);
                } else {
                    fill(0);

                }
                rect(x, y, w, h);
                textSize(bblock / 4);
                fill(200);
                text(bname1, x - bblock, y + bblock / 2);
                if ((time < 60) && (sms)) {
                    textSize(bblock / 4);     //   make responsive
                    fill(255, 0, 0);
                   // text(bname, (bblock * 1.2f) + bblock + bblock, (bblock / 2) + bblock * 8.8f);
                } else {
                    sms = false;
                }
            }

            public void checkbuttons() {

                if (ebuttons[0].on == true) {
                    tablefill();
                    ebuttons[0].on = false;
                }
                if (ebuttons[1].on == true) {

                    saveit();
                    d1.setVisible(true);
                    d1.setCaptionLabel("save File...");
                    Color c = Color.RED;
                    d1.setColorCaptionLabel(c.getRGB());
                    d1.open();
                    ebuttons[1].on = false;
                }
                if (ebuttons[2].on == true) {
                    // Following lists file in a directory
                    mylist();
                    d1.setVisible(true);
                    d1.setCaptionLabel("load File...");
                    Color c = Color.GREEN;

                    d1.setColorCaptionLabel(c.getRGB());
                    d1.open();
                    ebuttons[2].on = false;
                }
                if (ebuttons[3].on == true) {
                    // Following lists file in a directory

                    chordcount += 1;

                    if (chordcount == 4) {
                        chordcount = 0;
                    }
                    ebuttons[3].bname1 = str(chordcount);
                    chordcheck();
                    makechord();
                    transposeit(0);
                    ebuttons[3].on = false;
                }

            }

        }
        public void getpath() {

           // mypath = Environment.getExternalStorageDirectory().getAbsolutePath(); //TODO
            mypath = "./";    //TODO
            println(mypath);
            //--
           // File mydummyFile = new File("/" + mypath + "/SENSN");    //create a directory
            File mydummyFile = new File(  mypath + "/SENSN");    //create a directory
            System.out.println("MKDIR" + mydummyFile.getPath());
            mydummyFile.mkdirs();
        }


        class Test {
            public void setX(float x,float y,float w,float h) {
                this.x = x;
                this.y = y;
                this.w = w;
                this.h = h;
            }

           

            //class vars
            float x;
            float y;
            float w, h;
            float initialY;
            boolean lock = false;
            float lowerY = height - h - initialY;
            float value = map(y, initialY, lowerY, 120, 255);
            float value2 = map(value, 120, 255, 100, 0);
            boolean dragged = true;
            int id;
            int m1val1;
            //constructors

            //default
            Test() {
            }

            Test(float _x, float _y, float _w, float _h, int _id) {
                id = _id;
                m1val1 = 0;
                x = _x;
                y = _y;
                initialY = y;
                w = _w;
                h = _h;
            }


            public void run() {


                //set color as it changes
                int c = color(value);
                fill(c);

                // draw base line
                rect(x, initialY, 4, lowerY);

                // draw knob
                fill(200);
                rect(x, y, w, h);

                // display text
                fill(0);
                textSize(bblock/1.5f);
                text(PApplet.parseInt(m1val1), x + 5, y + 15);
                if (dragged) {
                    over();
                }

            }

            // is mouse ove knob?
            public boolean isOver() {
                return (x + w >= mouseX) && (mouseX >= x) && (y + h >= mouseY) && (mouseY >= y);
            }

            public void over() {


                //get mouseInput and map it
                float my = constrain(mouseY, initialY, height - h - initialY);
                if (lock) y = my;
                // bad practice have all stuff done in one method...
                lowerY = height - h - initialY;

                // map value to change color..
                value = map(y, initialY, lowerY, 120, 255);

                // map value to display
                value2 = map(value, 120, 255, 100, 0);

                if (id == 1) {
                    float mvalue = map(y, initialY, lowerY, 300, 30);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        getbpm(m1val);
                        //sendMidiAF(m1val,id);
                        m1val1 = m1val;
                        tempo = myHouse.rooms[mychannel - 1].instances[0].y;
                    }
                }
                if (id == 2) {
                    float mvalue = map(y, initialY, lowerY, +48, -48);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].transpose = m1val;
                        transposeit(m1val);

                        //sendMidiAF(m1val,id);
                        m1val1 = m1val;
                    }
                }
                if (id == 3) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].vol = m1val;
                        // transposeit(m1val);
                        shedMidi.sendMidiVol(mychannel, m1val); //TODO
                        m1val1 = m1val;
                    }
                }
                if (id == 4) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].rel = m1val;
                        //// transposeit(m1val);
                       shedMidi.sendMidiRel(mychannel, m1val);       //TODO
                        m1val1 = m1val;
                    }
                }


                if (id == 8) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[0] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 9) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[1] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 10) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[2] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 11) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[3] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 12) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[4] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 13) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[5] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 14) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[6] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 15) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[7] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 16) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[8] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 17) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[9] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 18) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[10] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 19) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[11] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 20) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[12] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 21) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[13] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 22) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[14] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }
                if (id == 23) {
                    float mvalue = map(y, initialY, lowerY, 127, 0);
                    int m1val = PApplet.parseInt(mvalue);
                    if (m1val != m1val1) {
                        myHouse.rooms[mychannel - 1].pitch1[15] = m1val;
                        makechord();
                        transposeit(PApplet.parseInt(myHouse.rooms[mychannel - 1].transpose));
                        m1val1 = m1val;
                    }
                }


            }
        }//end of class
        //CLASS HSLIDER
        class Test1 {
            public void setX(float x,float y,float w,float h,float o) {
                this.x = x;
                this.y = y;
                this.w = w;
                this.h = h;
                this.offset = o;
                this.initialY = y;
            }
            //class vars
            float x;
            float y;
            float w, h;
            float initialY;
            float offset;
            boolean lock = false;
            float lowerY = width - h - initialY;
            float value = map(y, initialY, lowerY - 20, 120, 255);
            float value2 = map(value, 120, 255, 100, 0);
            boolean dragged = true;

            //constructors

            //default
            Test1() {
            }

            Test1(float _x, float _y, float _w, float _h, float _offset) {    //Swing Slider

                x = _x;
                y = _y;
                initialY = y;
                w = _w;
                h = _h;
                offset = _offset;
            }


            public void run() {


                //set color as it changes
                //color c = color(value);
                //fill(c);

                // draw base line
                rect(initialY, x, offset - initialY, 4);

                // draw knob
                fill(200);
                rect(y, x, h, bblock);

                // display text
                fill(0);
                text(PApplet.parseInt(value-120) , y + 5, x + 15);

                if (dragged) {
                    over();

                }
            }

            // is mouse ove knob?
            public boolean isOver() {
                return (y + w >= mouseX) && (mouseX >= y) && (x + h >= mouseY) && (mouseY >= x);
            }

            public void over() {


                //get mouseInput and map it
                float my = constrain(mouseX, initialY, offset);
                if (lock) y = my;
                // bad practice have all stuff done in one method...
                lowerY = offset;

                // map value to change color..
                value = map(y, initialY, lowerY, 120, 255);

                // map value to display
                value2 = map(value, 120, 255, 0f, 0.33f);
                getswing(value2);

            }


        }// Class eND


        public void wtablesetup () {

            wtable = new Table();
            wtable = new Table();
            wtable.setColumnTitles(new String[]{
                    "ID", "ButtonID", "Tick", "NoteNumber", "Velocity", "Channel"
            });
            wtable.setColumnType("ID", Table.INT);
            wtable.setColumnType("ButtonID", Table.INT);
            wtable.setColumnType("Tick", Table.INT);
            wtable.setColumnType("NoteNumber", Table.INT);
            wtable.setColumnType("Velocity", Table.INT);
            wtable.setColumnType("Channel", Table.INT);
        }

        public void tablesetup () {

            table = new Table();
            for (int i = 0; i < numchannels; i++) {
                table.addColumn("Channel" + str(i + 1));
                table.addColumn("Transpose" + str(i + 1));
                table.addColumn("Volume" + str(i + 1));
                table.addColumn("Release" + str(i + 1));
                table.addColumn("Pslider" + str(i + 1));
                table.addColumn("Bank" + str(i + 1));
                table.addColumn("Program" + str(i + 1));
            }


            for (int i = 0; i < 101 * numentries; i++) {
                table.addRow();
            }
        }

        public void tablefill () {

            for (int i = 0; i < numchannels; i++) {
                for (int j = 0; j < 80; j++) {
                    if (myHouse.rooms[i].buttons[j].on == true) {
                        table.setInt(j + 101 * tabentry, "Channel" + str(i + 1), 1);
                    } else {
                        table.setInt(j + 101 * tabentry, "Channel" + str(i + 1), 0);
                    }
                }
            }
            for (int i = 0; i < numchannels; i++) {
                for (int j = 0; j < 5; j++) {
                    if (myHouse.rooms[i].buttons[j + 96].on == true) {
                        table.setInt(j + (101 * tabentry) + 96, "Channel" + str(i + 1), 1);
                    } else {
                        table.setInt(j + (101 * tabentry) + 96, "Channel" + str(i + 1), 0);
                    }
                }
            }
            for (int i = 0; i < numchannels; i++) {
                myHouse.rooms[i].sliderget();
                table.setFloat(i + 1 * tabentry, "Transpose" + str(i + 1), myHouse.rooms[i].transp);
            }
            for (int i = 0; i < numchannels; i++) {
                myHouse.rooms[i].slidergetv();
                table.setFloat(i + 1 * tabentry, "Volume" + str(i + 1), myHouse.rooms[i].volume);
            }
            for (int i = 0; i < numchannels; i++) {
                myHouse.rooms[i].slidergetr();
                table.setFloat(i + 1 * tabentry, "Release" + str(i + 1), myHouse.rooms[i].release);
            }
            for (int i = 0; i < numchannels; i++) {
                table.setInt(i + 1 * tabentry, "Bank" + str(i + 1), myHouse.rooms[i].bank);
                table.setInt(i + 1 * tabentry, "Program" + str(i + 1), myHouse.rooms[i].prg);
            }


            for (int i = 0; i < numchannels; i++) {
                for (int j = 0; j < 16; j++) {
                    //myHouse.rooms[i].sliderget();
                    table.setInt(j + 16 * tabentry, "Pslider" + str(i + 1), myHouse.rooms[i].pitch1[j]);
                }
            }

        }


        public void tableget () {
            for (int i = 0; i < numchannels; i++) {
                myHouse.rooms[i].rtable.clearRows();
                for (int j = 0; j < 80; j++) {
                    myHouse.rooms[i].buttons[j].on = PApplet.parseBoolean(table.getInt(j + (101 * tabcentry), "Channel" + str(i + 1)));
                    if (myHouse.rooms[i].buttons[j].on) {
                        myHouse.rooms[i].rtable.addRow();
                        myHouse.rooms[i].rtable.setInt(myHouse.rooms[i].rtable.getRowCount() - 1, "ID", myHouse.rooms[i].rtable.getRowCount() - 1);
                        myHouse.rooms[i].rtable.setInt(myHouse.rooms[i].rtable.getRowCount() - 1, "Tick", myHouse.rooms[i].buttons[j].tick);
                        myHouse.rooms[i].rtable.setInt(myHouse.rooms[i].rtable.getRowCount() - 1, "ButtonID", myHouse.rooms[i].buttons[j].buttonid);
                        myHouse.rooms[i].rtable.setInt(myHouse.rooms[i].rtable.getRowCount() - 1, "NoteNumber", 60);
                        myHouse.rooms[i].rtable.setInt(myHouse.rooms[i].rtable.getRowCount() - 1, "Velocity", 80);
                        myHouse.rooms[i].rtable.setInt(myHouse.rooms[i].rtable.getRowCount() - 1, "Channel", i + 1);
                    }
                }
            }
            for (int i = 0; i < numchannels; i++) {
                for (int j = 0; j < 5; j++) {
                    myHouse.rooms[i].buttons[j + 96].on = PApplet.parseBoolean(table.getInt(j + (101 * tabcentry) + 96, "Channel" + str(i + 1)));
                    if (myHouse.rooms[i].buttons[j + 96].on == false) {

                        for (int x = 0; x < 16; x++) {
                            if (myHouse.rooms[i].rtable.getRowCount() > 0) {
                                if (myHouse.rooms[i].buttons[x + (16 * j)].on) {
                                    TableRow result = myHouse.rooms[i].rtable.findRow(str(x + (16 * j)), "ButtonID");
                                    int removeit = result.getInt("ID");        //int removeit = table.findRowIndex(str(buttonid),"ButtonID" );
                                    myHouse.rooms[i].rtable.removeRow(removeit);
                                    // println("Removed row ID" +removeit+ " - TOTAL ROWS "+ myHouse.rooms[mychannel-1].table.getRowCount());   //DEBUG PRINT
                                    int y = 0;                              //write new indices to the table since we removed a line from it
                                    for (TableRow row : myHouse.rooms[i].rtable.rows()) {
                                        myHouse.rooms[i].rtable.setInt(y, "ID", y);
                                        y++;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < numchannels; i++) {
                myHouse.rooms[i].transp = table.getFloat(i + 1 * tabcentry, "Transpose" + str(i + 1));
                myHouse.rooms[i].instances[1].y = myHouse.rooms[i].transp;
            }
            for (int i = 0; i < numchannels; i++) {
                myHouse.rooms[i].volume = table.getFloat(i + 1 * tabcentry, "Volume" + str(i + 1));
                myHouse.rooms[i].instances[2].y = myHouse.rooms[i].volume;
            }
            for (int i = 0; i < numchannels; i++) {
                myHouse.rooms[i].release = table.getFloat(i + 1 * tabcentry, "Release" + str(i + 1));
                myHouse.rooms[i].instances[3].y = myHouse.rooms[i].release;
            }
            for (int i = 0; i < numchannels; i++) {
                myHouse.rooms[i].bank = table.getInt(i + 1 * tabcentry, "Bank" + str(i + 1));
                myHouse.rooms[i].prg = table.getInt(i + 1 * tabcentry, "Program" + str(i + 1));
                shedMidi.sendMidiBank(i + 1, myHouse.rooms[i].bank, myHouse.rooms[i].prg);   //TODO
            }

            for (int i = 0; i < numchannels; i++) {
                for (int j = 0; j < 16; j++) {
                    //myHouse.rooms[i].sliderget();
                    myHouse.rooms[i].pitch1[j] = table.getInt(j + 16 * tabcentry, "Pslider" + str(i + 1));
                }
            }
            updateit();
            myHouse.updatewtable();
// for (TableRow row : table.rows()) {

//    int id = row.getInt("Pslider1");


//println(id);}
        }

        public void saveempty () {
            for (int i = 0; i < numentries; i++) {
                tablefill();
                tabentry += 1;
            }
            tabentry = 0;
        }
        public void updateall () {


            for (int i = 0; i < numchannels; i++) {
                mychannel = i + 1;
                myHouse.display();     //Workaround don't know why but otherwise not all channels play after loadtable (loadfile)
                transposeit(myHouse.rooms[mychannel - 1].transpose);

            }

//
            makechordall();
            transposeall();
            allvol();
            allrel();

//
            bbuttons[0].on = true;
            for (int i = 1; i < bbuttons.length; i++) {
                bbuttons[i].on = false;
            }
            mychannel = 1;

        }
        public void updateit () {
            // for (int i = 0; i < numchannels; i++) {
            // mychannel= i+1;
            // myHouse.display();     //Workaround don't know why but otherwise not all channels play after loadtable (loadfile)
            // transposeit(myHouse.rooms[mychannel-1].transpose);
            alltranspose();
            makechordall();
            transposeall();
            allvol();
            allrel();

            //transposeit(myHouse.rooms[i].transpose);
//}
            //MainActivity.mc.getbpm2();
// mychannel= 1;
        }

        public void me(){

            //Log.v("aaa", "CCCCCCCC" );
        }




//  MAINI


    public static void main(String[] args) {
	// write your code here
        //String[] appletArgs = new String[] { "Main" };
        String[] processingArgs = {"Main"};
        Main mySketch = new Main();
//PApplet pa=new PApplet();


        PApplet.runSketch(processingArgs, mySketch);

    }




}
