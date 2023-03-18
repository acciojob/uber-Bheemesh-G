package com.driver.services.impl;

import com.driver.model.*;
import com.driver.repository.CabRepository;
import com.driver.services.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.driver.repository.CustomerRepository;
import com.driver.repository.DriverRepository;
import com.driver.repository.TripBookingRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

	@Autowired
	CustomerRepository customerRepository2;

	@Autowired
	DriverRepository driverRepository2;

	@Autowired
	TripBookingRepository tripBookingRepository2;
	@Autowired
	CabRepository cabRepository;

	@Override
	public void register(Customer customer) {
		//Save the customer in database
		customerRepository2.save(customer);
	}

	@Override
	public void deleteCustomer(Integer customerId) {
		// Delete customer without using deleteById function
		customerRepository2.deleteById(customerId);

	}

	@Override
	public TripBooking bookTrip(int customerId, String fromLocation, String toLocation, int distanceInKm) throws Exception {
		//Book the driver with lowest driverId who is free (cab available variable is Boolean.TRUE). If no driver is available, throw "No cab available!" exception

        Customer customer = customerRepository2.findById(customerId).get();
		TripBooking tripBooking = new TripBooking();
		tripBooking.setFromLocation(fromLocation);
		tripBooking.setToLocation(toLocation);
		tripBooking.setDistanceInKm(distanceInKm);
		List<Driver> driverList = driverRepository2.findAll();
		int driverId = Integer.MAX_VALUE;
		for (Driver i : driverList){
			Cab cab = i.getCab();
			if(cab.getAvailable()==true && i.getDriverId()<driverId)
			{
				driverId = i.getDriverId();
			}
		}
		try{
			if(driverId==Integer.MAX_VALUE)
			{
				throw new Exception("No cab available!");
			}
		}
		catch(Exception e)
		{
			tripBooking.setStatus(TripStatus.CANCELED);
			tripBookingRepository2.save(tripBooking);
			System.out.println(e);
		}

		Driver driverGot = driverRepository2.findById(driverId).get();

		tripBooking.setStatus(TripStatus.CONFIRMED);
		tripBooking.setDriver(driverGot);
		tripBooking.setCustomer(customer);

		customer.getTripBookingList().add(tripBooking);
		driverGot.getTripBookingList().add(tripBooking);

		customerRepository2.save(customer);
		//Avoid using SQL query

		return tripBooking;

	}

	@Override
	public void cancelTrip(Integer tripId){
		//Cancel the trip having given trip Id and update TripBooking attributes accordingly
		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.CANCELED);
		Customer customer = tripBooking.getCustomer();
		customer.getTripBookingList().remove(tripBooking);
		Driver driver = tripBooking.getDriver();
		driver.getTripBookingList().remove(tripBooking);

	}

	@Override
	public void completeTrip(Integer tripId){
		//Complete the trip having given trip Id and update TripBooking attributes accordingly

		TripBooking tripBooking = tripBookingRepository2.findById(tripId).get();
		tripBooking.setStatus(TripStatus.COMPLETED);
		int totalPrice = tripBooking.getDistanceInKm()*10;
		tripBooking.setBill(totalPrice);

	}
}
