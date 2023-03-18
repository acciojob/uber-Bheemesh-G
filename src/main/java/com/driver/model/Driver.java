package com.driver.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Entity
public class Driver {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int driverId;
    private String mobile;
    private String password;

    @OneToOne
    @JoinColumn
    Cab cab;

    @OneToMany(mappedBy = "driver",cascade = CascadeType.ALL)
    List<TripBooking> tripBookingList = new ArrayList<>();

    public Driver() {
    }

    public Driver(int driverId, String mobile, String password, Cab cab, List<TripBooking> tripBookingList) {
        this.driverId = driverId;
        this.mobile = mobile;
        this.password = password;
        this.cab = cab;
        this.tripBookingList = tripBookingList;
    }

    public int getDriverId() {
        return driverId;
    }

    public String getMobile() {
        return mobile;
    }

    public String getPassword() {
        return password;
    }

    public Cab getCab() {
        return cab;
    }

    public List<TripBooking> getTripBookingList() {
        return tripBookingList;
    }

    public void setDriverId(int driverId) {
        this.driverId = driverId;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCab(Cab cab) {
        this.cab = cab;
    }

    public void setTripBookingList(List<TripBooking> tripBookingList) {
        this.tripBookingList = tripBookingList;
    }
}
