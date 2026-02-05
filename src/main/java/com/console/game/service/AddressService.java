package com.console.game.service;

import java.util.List;

import com.console.game.model.Address;
import com.console.game.model.User;

public interface AddressService {
    List<Address> findAddressByUser(User user);
}
