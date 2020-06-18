package com.company;
import jdk.jshell.execution.Util;
import processing.core.*;
//import themidibus.MidiBus;
import themidibus.*; //Import the library

import javax.sound.midi.*;
import java.io.File;
import java.io.IOException;
import java.sql.SQLOutput;


public class ShedMidi {
    MidiBus myBus; // The MidiBus
    byte[] buffer1 = new byte[3];

    File file = new File("./MYPROPHET.sf2");
    Soundbank soundbank = MidiSystem.getSoundbank(file);

   // Soundbank soundfont = MidiSystem.getSoundbank(("FluidR3_GM.sf2").getInputStream());
   // Sequencer sequencer = MidiSystem.getSequencer();
    Synthesizer synthesizer = MidiSystem.getSynthesizer();
    Receiver synthRcvr = synthesizer.getReceiver();;
    // List all available Midi devices on STDOUT. This will show each device's index and name.
    //myBus = new MidiBus(this, 0, 0); // Create a new MidiBus object
    ShortMessage a = new ShortMessage();
    ShortMessage vol = new ShortMessage(ShortMessage.CONTROL_CHANGE,7,127);
    ShortMessage rel = new ShortMessage();


    public ShedMidi() throws MidiUnavailableException, InvalidMidiDataException, IOException {

        synthesizer.open();

       Soundbank soundbank = MidiSystem.getSoundbank(file);
        System.out.println( soundbank.getName());
        System.out.println( soundbank.getDescription());
        System.out.println( soundbank.getInstruments().toString());
        System.out.println( soundbank.getResources().toString());
        synthesizer.loadAllInstruments(soundbank);
      // synthRcvr = synthesizer.getReceiver();


 //
   MidiBus.list();

        myBus = new MidiBus(this, 0, 3);    //!!!
       // myBus = new MidiBus(this, -1, "Java Sound Synthesizer");
    }


    public void sendMidiR(int thenote, int thevelocity, int thechannel) {


    }


    public void sendMidi(int thenote, int thevelocity, int thechannel) {
        //Send MIDI note data ... for info .send() is a methid from MidiReceiver class which

        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(0x90 + (thechannel - 1)); // note on
        buffer[numBytes++] = (byte)thenote; // closed hi hat
        buffer[numBytes++] = (byte)thevelocity; // max velocity
        int offset = 0;
        // post is non-blocking
        try {
            //inputPort.send(buffer, offset, numBytes);
            //myBus.sendNoteOn(thechannel, thenote, thevelocity); // Send a Midi noteOn

            a.setMessage(144, thechannel,  thenote, thevelocity);
            synthRcvr.send(a,0);          //TODO

            //println("data sent");
            System.out.println("CHANNEL: "+thechannel );
        }
        catch (Exception e) {
          //  println("error sending midi data");
        }
    }
    public void sendMidioff(int thenote, int thechannel) {
        //Send MIDI note data ... for info .send() is a methid from MidiReceiver class which

        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(0x90 + (thechannel - 1)); // note on
        buffer[numBytes++] = (byte)thenote; // closed hi hat
        buffer[numBytes++] = (byte)0; // max velocity
        int offset = 0;
        // post is non-blocking
        try {
           // inputPort.send(buffer, offset, numBytes);
          //  myBus.sendNoteOff(thechannel, thenote, 0); // Send a Midi nodeOff

            a.setMessage(0x90, thechannel,  thenote, 0);
            synthRcvr.send(a,0);          //TODO
           // println("data sent");
        }
        catch (Exception e) {
          //  println("error sending midi data");
        }
    }
    public void sendMidiPrg(int thechannel,int prgnumber) {

        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(191+thechannel); // Prgchange
        buffer[numBytes++] = (byte)prgnumber; // Prgnumber
        int offset = 0;
        // post is non-blocking
        try {
         //   inputPort.send(buffer, offset, numBytes);
           // myBus.sendMessage(buffer);
//a.set
           // a.setMessage(191, thechannel,  prgnumber );
           // synthRcvr.send(a,0);          //TODO
           // println("data sent");
            System.out.println( "PRDCHANGE");
        }
        catch (Exception e) {
         //   println("error sending midi data");
            System.out.println( "ERROR PRGCHANGE");

        }
    }

    public void sendMidiBank(int thechannel,int thebank,int theprg) {

        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(175+thechannel); // Prgchange
        buffer[numBytes++] = (byte)0;
        buffer[numBytes++] = (byte)thebank;  // Prgnumber
        buffer[numBytes++] = (byte)32;
        buffer[numBytes++] = (byte)0;
        buffer[numBytes++] = (byte)(191+thechannel);
        buffer[numBytes++] = (byte)theprg;

        //   https://docs.oracle.com/javase/7/docs/api/javax/sound/midi/MidiMessage.html
        // MidiMessage msg=new MidiMessage();
        //MidiEvent msg = new MidiEvent(buffer,7);
       // msg.getMessage(buffer,7);
        int offset = 0;
        // post is non-blocking
        try {
            //inputPort.send(buffer, offset, numBytes);
           // myBus.sendMessage(buffer);
            ShortMessage c1 = new ShortMessage(ShortMessage.CONTROL_CHANGE, thechannel, 0,  thebank);   // = 9
            ShortMessage c2 = new ShortMessage(ShortMessage.CONTROL_CHANGE, thechannel, 32, 0); // = 0
            ShortMessage c3  = new ShortMessage(ShortMessage.PROGRAM_CHANGE, thechannel, theprg, 0);
            synthRcvr.send(c1,0);
            synthRcvr.send(c2,0);
            synthRcvr.send(c3,0);

//            ... = new ShortMessage(ShortMessage.CONTROL_CHANGE, 0, 0,  1152 >> 7);   // = 9
//... = new ShortMessage(ShortMessage.CONTROL_CHANGE, 0, 32, 1152 & 0x7f); // = 0
//... = new ShortMessage(ShortMessage.PROGRAM_CHANGE, 0, 14, 0);
            // println("data sent");
            System.out.println("BANK: " + thebank +"Prg: " +theprg + "Channel:" +thechannel );
        }
        catch (Exception e) {
          //  println("error sending midi data");
        }
    }

    public void sendMidiVol(int thechannel,int thevalue) {

        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(175+thechannel); // Prgchange
        buffer[numBytes++] = (byte)7; // Volume
        buffer[numBytes++] = (byte)thevalue; // Volume
        int offset = 0;
        // post is non-blocking
        try {
           // inputPort.send(buffer, offset, numBytes);
            //myBus.sendMessage(buffer);
            vol.setMessage(0x90, thechannel,  7, thevalue);
            synthRcvr.send(vol,0);          //TODO


           // println("data sent");
        }
        catch (Exception e) {
           // println("error sending midi data");
        }
    }


    public void sendMidiRel(int thechannel,int thevalue) {

        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(175+thechannel); // Prgchange
        buffer[numBytes++] = (byte)99;
        buffer[numBytes++] = (byte)120;
        buffer[numBytes++] = (byte)98;
        buffer[numBytes++] = (byte)38;
        buffer[numBytes++] = (byte)38;
        buffer[numBytes++] = (byte)66;
        buffer[numBytes++] = (byte)6;
        buffer[numBytes++] = (byte)thevalue; // Volume
        int offset = 0;
        // post is non-blocking
        try {
            //inputPort.send(buffer, offset, numBytes);
            myBus.sendMessage(buffer);
         //   println("data sent");
        }
        catch (Exception e) {
          //  println("error sending midi data");
        }
    }

    public void sendMidialloff(int thechannel) {


        buffer1[0] = (byte)(175+thechannel); // Prgchange
        buffer1[1] = (byte)123; // Volume
        buffer1[2] = (byte)0; // Volume

        // post is non-blocking
        try {
           // inputPort.send(buffer1, 0, 3);
            //myBus.sendMessage(buffer1);
            a.setMessage(176, thechannel,  123, 0);
            synthRcvr.send(a,0);          //TODO
          //  println("data sent");
        }
        catch (Exception e) {
          //  println("error sending midi data");
        }
    }
    public void sendMidiStart() {
        //Send MIDI note data ... for info .send() is a methid from MidiReceiver class which
        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(176); // note on
        buffer[numBytes++] = (byte)(116);
        buffer[numBytes++] = (byte)(127);
        int offset = 0;
        // post is non-blocking
        try {
            //inputPort.send(buffer, offset, numBytes);
            myBus.sendMessage(buffer);
           // println("data sent");
        }
        catch (Exception e) {
          //  println("error sending midi data");
        }
    }
    public void sendMidiClock() {
        //Send MIDI note data ... for info .send() is a methid from MidiReceiver class which
        byte[] buffer = new byte[32];
        int numBytes = 0;
        int channel = 1; // MIDI channels 1-16 are encoded as 0-15.
        buffer[numBytes++] = (byte)(248); // note on

        int offset = 0;
        // post is non-blocking
        try {
            //inputPort.send(buffer, offset, numBytes);
            myBus.sendMessage(buffer);
            //println("data sent");
        }
        catch (Exception e) {
           // println("error sending midi data");
        }
    }

}
