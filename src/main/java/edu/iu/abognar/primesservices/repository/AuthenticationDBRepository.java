package edu.iu.abognar.primesservices.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import edu.iu.abognar.primesservices.model.Customer;

@Repository
public interface AuthenticationDBRepository extends CrudRepository<Customer, String>{
    Customer findByUsername(String username);
}
