package com.console.game.repository;

import com.console.game.model.Address;
import com.console.game.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    // Tìm danh sách địa chỉ của User
    List<Address> findByUser(User user);
}