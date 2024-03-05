package com.mmdc.oop;

import java.time.LocalDateTime;

public class TimeKeeping {
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;

    public void checkIn() {
        checkInTime = LocalDateTime.now();

    }

    public void checkOut() {
        checkOutTime = LocalDateTime.now();
    }

    public double calculateWorkingHours() {
        // get sum of working hours by adding checkIn and checkOut time
        // for now let's return 8.0 hours
        return 8.0;
    }
}
