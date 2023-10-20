package com.week6.login.service;

import java.util.List;

import com.week6.login.model.UserDtls;

public interface UserService {

    public UserDtls createUser(UserDtls user);

    public boolean checkUsername(String username);

    public UserDtls getUserByUsername(String username);

    public void deleteUserById(int id);

    public void changeUserRole(int id,String newRole);

    public List<UserDtls> getAllUser();

    public List<UserDtls> getUserByName(String name);
}
