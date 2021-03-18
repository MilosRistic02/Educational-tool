package nl.tudelft.oopp.demo.controllers;

import java.util.Comparator;
import nl.tudelft.oopp.demo.entities.LectureRoom;

public class RoomComparator implements Comparator<LectureRoom> {
    @Override
    public int compare(LectureRoom o1, LectureRoom o2) {
        return (o2.getCreationDate().compareTo(o1.getCreationDate()));
    }
}
