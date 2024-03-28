package edu.iu.abognar.primesservices.service;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import edu.iu.abognar.primesservices.model.Customer;
import edu.iu.abognar.primesservices.repository.IAuthenticationRepository;

@Service
public class AuthenticationService implements IAuthenticationService, UserDetailsService{
    IAuthenticationRepository authenticationRepository;

    public AuthenticationService(IAuthenticationRepository authenticationRepository) {
        this.authenticationRepository = authenticationRepository;
    }

    @RestController
    public class AuthenticationController {
        private final IAuthenticationService authenticationService;
        private final AuthenticationManager authenticationManager;
        private TokenService tokenService;
        
        public AuthenticationController(AuthenticationManager authenticationManager, 
                                        IAuthenticationService authenticationService,
                                        TokenService tokenService) {
            this.authenticationManager = authenticationManager;
            this.authenticationService = authenticationService;
            this.tokenService = tokenService;
        }

        @PostMapping("/register")
        public boolean register(@RequestBody Customer customer) {
            try {
                return authenticationService.register(customer);
            } catch(IOException e) {
                throw new RuntimeException(e);
            }
        }

        @PostMapping("/login")
        public String login(@RequestBody Customer customer) {
            Authentication authentication = authenticationManager
                .authenticate(
                    new UsernamePasswordAuthenticationToken(
                        customer.getUsername(),
                        customer.getPassword()));
            return tokenService.generateToken(authentication);
        }
    }

    @Override
    public boolean register(Customer customer) throws IOException {
        BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
        String passwordEncoded = bc.encode(customer.getPassword());
        customer.setPassword(passwordEncoded);
        return authenticationRepository.save(customer);
    }

    @Override
    public boolean login(String username, String password) throws IOException {
        Customer customer = authenticationRepository.findByUsername(username);
        if(customer != null) {
            BCryptPasswordEncoder bc = new BCryptPasswordEncoder();
            if(bc.matches(password, customer.getPassword())) {
                return true;
            }
            return false;
        }
        return false;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Customer customer = authenticationRepository.findByUsername(username);
            if(customer == null) {
                throw new UsernameNotFoundException("");
            }
            return User
                .withUsername(username)
                .password(customer.getPassword())
                .build();
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public AuthenticationManager authManager(UserDetailsService userDetailsService) {
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return new ProviderManager(authProvider);
    }
}
