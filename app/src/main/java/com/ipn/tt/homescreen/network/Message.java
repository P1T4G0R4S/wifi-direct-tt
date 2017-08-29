package com.ipn.tt.homescreen.network;

import java.io.Serializable;

/**
 * Created by osvaldo on 8/27/17.
 */

public class Message implements Serializable {

    public MessageType messageType;
    public byte[] message;
    public ObjectType objectType;

    public Message(MessageType messageType, byte[] message) {
        this.messageType = messageType;
        this.message = message;
    }
}
