package com.week6.login.service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.week6.login.model.UserDtls;
import com.week6.login.repository.UserRepository;


@Service
public class UserServiceImpl implements UserService {

	public String code="Y2KMO93";

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncode;

	@Override
	public UserDtls createUser(UserDtls user) {

		user.setPassword(passwordEncode.encode(user.getPassword()));
		if(user.getRole().equals("ROLE_ADMIN")){
			user.setRole("ROLE_ADMIN");
		}
		else if(user.getRole().equals(code)){
			user.setRole("ROLE_ADMIN");
		}
		else{
			user.setRole("ROLE_USER");
		}
		return userRepo.save(user);
	}

	@Override
	public boolean checkUsername(String username) {

		return userRepo.existsByUsername(username);
	}

	@Override
	public UserDtls getUserByUsername(String username) {
		return userRepo.findByUsername(username);
	}

	@Override
	public void deleteUserById(int id) {
		UserDtls user=userRepo.findById(id);
       
        if(user!=null){
            userRepo.delete(user);
        }
	}

	@Override
	public void changeUserRole(int id, String newRole) {
		UserDtls user=userRepo.findById(id);
        if(user!=null){
            user.setRole(newRole);
            userRepo.save(user);
        }
	}

	@Override
	public List<UserDtls> getAllUser() {
		return userRepo.findAll();
	}

	@Override
	public List<UserDtls> getUserByName(String name) {
		return userRepo.findByName(name);
	}

}