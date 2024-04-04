package edu.iu.abognar.primesservices.service;

import java.io.IOException;

import edu.iu.abognar.primesservices.model.Customer;

public interface IAuthenticationService {
    Customer register(Customer customer) throws IOException;
    boolean login(String username, String password) throws IOException;
}
