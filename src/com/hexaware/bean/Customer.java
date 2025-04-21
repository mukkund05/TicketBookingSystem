
package com.hexaware.bean;

import java.util.Objects;


public class Customer {
    private int customerId;
    private String customerName;
    private String email;
    private String phoneNumber;

  
    public Customer() {}

   
    public Customer(int customerId, String customerName, String email, String phoneNumber) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    
    public Customer(String customerName, String email, String phoneNumber) {
        this.customerName = customerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

   
    public void displayCustomerDetails() {
        System.out.println("Customer: " + customerName + " | Email: " + email +
                " | Phone: " + phoneNumber);
    }

  
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId &&
               Objects.equals(customerName, customer.customerName) &&
               Objects.equals(email, customer.email) &&
               Objects.equals(phoneNumber, customer.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, customerName, email, phoneNumber);
    }
}
