package com.example.airbnb_app.requestClasses;

import java.io.Serializable;
import java.util.List;

public class Message implements Serializable {

    private static final long serialVersionUID = 4L;
    private Filter filter;

    public String message;

    private int actionId;

    public Filter getFilter() {
        return filter;
    }

    private int reservations;

    public int getReservations() {
        return reservations;
    }

    public void setReservations(int reservations) {
        this.reservations = reservations;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    private Room room;

    public Message(int actionId, Room room) {
        this.actionId = actionId;
        this.room = room;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    private Integer stars;

    public Message(int actionId) {
        this.actionId = actionId;
    }

    private String roomName;

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    public Message(int actionId, String mapId, List<Room> rooms) {
        this.actionId = actionId;
        this.mapId = mapId;
        this.rooms = rooms;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Message(int actionId, String roomName, Integer stars) {
        this.actionId = actionId;
        this.roomName = roomName;
        this.stars = stars;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    private String mapId;

    public String getMapId() {
        return mapId;
    }

    public void setMapId(String mapId) {
        this.mapId = mapId;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }
//action_id is and identifier that tells each server what to do:
    //1 = action id expected by master from manager in order to receive rooms.json
    //2 = action id expected by worker from master in order to receive his rooms

    private List<Room> rooms;


    public String getMessage() {
        return message;
    }

    public Message(List<Room> rooms, int actionId) {
        this.rooms = rooms;
        this.actionId = actionId;
    }

    public Message(int actionId, Filter filter) {
        this.actionId = actionId;
        this.filter = filter;

    }

    public Message(Filter filter, int actionId, String mapId) {
        this.filter = filter;
        this.actionId = actionId;
        this.mapId = mapId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(String message) {
        this.message = message;
    }

    public int getActionId() {
        return actionId;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
