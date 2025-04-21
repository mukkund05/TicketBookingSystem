
package com.hexaware.bean;

import java.util.Comparator;

/**
 * Comparator for sorting events by name and venue name.
 */
public class EventComparator implements Comparator<Event> {
    /**
     * Compares two events based on event name and venue name.
     * @param e1 First event
     * @param e2 Second event
     * @return Comparison result
     */
    @Override
    public int compare(Event e1, Event e2) {
        int nameCompare = e1.getEventName().compareTo(e2.getEventName());
        if (nameCompare != 0) return nameCompare;
        return e1.getVenue().getVenueName().compareTo(e2.getVenue().getVenueName());
    }
}
