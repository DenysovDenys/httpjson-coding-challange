package com.example.httpjsoncodingchallenge.controller;

import com.example.httpjsoncodingchallenge.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(produces="application/json")
public class UserController {

    private static final List<User> USERS = new ArrayList<>();
    private static final int TOP20 = 20;

    @GetMapping("/userinfo/{user_id}")
    public List<User> getUsersById(@PathVariable String user_id) {
        return findUsersById(Integer.parseInt(user_id));
    }

    @GetMapping("/levelinfo/{level_id}")
    public List<User> getUsersByLevel(@PathVariable String level_id) {
        return findUsersByLevel(Integer.parseInt(level_id));
    }

    @RequestMapping(value = "/setinfo", method = RequestMethod.POST)
    public String setUser(@RequestBody User user) {
        USERS.add(new User(user.getUser_id(), user.getLevel_id(), user.getResult()));
        return "User has been added.";
    }

    private static List<User> findUsersById(int user_id) {
        List<User> foundUsers = new ArrayList<>();

        for (User user : USERS) {
            if (user.getUser_id() == user_id) {
                foundUsers.add(new User(user.getUser_id(), user.getLevel_id(), user.getResult()));
            }
        }

        return sortList(foundUsers);
    }

    private static List<User> findUsersByLevel(int level_id) {
        List<User> foundUsers = new ArrayList<>();

        for (User user : USERS) {
            if (user.getLevel_id() == level_id) {
                foundUsers.add(new User(user.getUser_id(), user.getLevel_id(), user.getResult()));
            }
        }

        return sortList(foundUsers);
    }

    private static List<User> sortList(List<User> foundUsers) {
        foundUsers.sort((lhs, rhs) -> {
            Integer lid = lhs.getResult();
            Integer rid = rhs.getResult();
            return rid.compareTo(lid);
        });

        List<User> result = new ArrayList<>();
        if (foundUsers.size() < TOP20) {
            result.addAll(foundUsers);
        } else {
            for(int i = 0; i < TOP20; i++) {
                result.add(foundUsers.get(i));
            }
        }
        return result;
    }
}
