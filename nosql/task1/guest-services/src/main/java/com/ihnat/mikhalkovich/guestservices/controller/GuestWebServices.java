package com.ihnat.mikhalkovich.guestservices.controller;

import com.ihnat.mikhalkovich.guestservices.entity.Guest;
import com.ihnat.mikhalkovich.guestservices.repository.GuestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/guests")
@RequiredArgsConstructor
public class GuestWebServices {
    private final GuestRepository guestRepository;

    @GetMapping
    public Iterable<Guest> getGuests() {
        return guestRepository.findAll();
    }

    @GetMapping("/{id}")
    public Guest getGuest(@PathVariable("id") long id) {
        return this.guestRepository.findById(id).get();
    }
}
