package com.geukrock.geukrockapiserver.users.user.repository;

import com.geukrock.geukrockapiserver.users.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {

}
