package edu.iu.abognar.primesservices.repository;

import java.io.IOException;

import edu.iu.abognar.primesservices.model.Customer;

public interface IAuthenticationRepository {
    boolean save(Customer customer) throws IOException;
    Customer findByUsername(String username) throws IOException;
}
