package com.ihnat.mikhalkovich.guestservices.repository;

import com.ihnat.mikhalkovich.guestservices.entity.Guest;
import org.springframework.data.repository.CrudRepository;

public interface GuestRepository extends CrudRepository<Guest, Long> {
}
