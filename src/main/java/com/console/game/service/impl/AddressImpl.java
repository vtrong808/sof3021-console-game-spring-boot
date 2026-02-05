package com.console.game.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.console.game.model.Address;
import com.console.game.model.User;
import com.console.game.repository.AddressRepository;
import com.console.game.service.AddressService;

@Service
public class AddressImpl implements AddressService{
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public List<Address> findAddressByUser(User user){
        return addressRepository.findByUser(user);
    }
    
}
