package edu.iu.abognar.primesservices.controller;

import edu.iu.abognar.primesservices.service.IPrimesService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/primes")
public class PrimesController {
    IPrimesService primesService;
    public PrimesController(IPrimesService primesService)
    {
        this.primesService = primesService;
    }

    @GetMapping("/{n}")
    public boolean isPrime(@PathVariable Long n)
    {
        return primesService.isPrime(n);
    }
}
