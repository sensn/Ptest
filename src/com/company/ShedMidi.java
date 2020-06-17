package com.company;

import themidibus.MidiBus;

import javax.sound.midi.MidiMessage; //Import the MidiMessage classes http://java.sun.com/j2se/1.5.0/docs/api/javax/sound/midi/MidiMessage.html
import javax.sound.midi.SysexMessage;
import javax.sound.midi.ShortMessage;


public class ShedMidi {
    MidiBus myBus; // The MidiBus
    byte[] buffer1 = new byte[3];

    MidiBus.list(); // List all available Midi devices on STDOUT. This will show each device's index and name.
    myBus = new MidiBus(this, 0, 0); // Create a new MidiBus object



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
            inputPort.send(buffer, offset, numBytes);
            //println("data sent");
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
            inputPort.send(buffer, offset, numBytes);
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
            inputPort.send(buffer, offset, numBytes);
           // println("data sent");
        }
        catch (Exception e) {
         //   println("error sending midi data");
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
        int offset = 0;
        // post is non-blocking
        try {
            inputPort.send(buffer, offset, numBytes);
           // println("data sent");
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
            inputPort.send(buffer, offset, numBytes);
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
            inputPort.send(buffer, offset, numBytes);
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
            inputPort.send(buffer1, 0, 3);
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
            inputPort.send(buffer, offset, numBytes);
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
            inputPort.send(buffer, offset, numBytes);
            //println("data sent");
        }
        catch (Exception e) {
           // println("error sending midi data");
        }
    }

}
