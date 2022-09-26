package com.example.httpjsoncodingchallenge.controller;

import com.example.httpjsoncodingchallenge.entity.User;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping(produces="application/json")
public class UserController {

    private static JSONArray mainJsonArray = new JSONArray();
    private static final String KEY_NAME = "result";
    private static final int TOP20 = 20;

    @GetMapping("/userinfo/{user_id}")
    public String getUsersById(@PathVariable String user_id) throws JSONException {
        return Objects.requireNonNull(findUsersById(Integer.parseInt(user_id))).toString();
    }

    @GetMapping("/levelinfo/{level_id}")
    public String getUsersByLevel(@PathVariable String level_id) throws JSONException {
        return Objects.requireNonNull(findUsersByLevel(Integer.parseInt(level_id))).toString();
    }

    @RequestMapping(value = "/setinfo", method = RequestMethod.POST)
    public String setUser(@RequestBody User user) throws JSONException {
        mainJsonArray.put(newUser(user.getUser_id(), user.getLevel_id(), user.getResult()));
        return "User has been added.";
    }

    private static JSONObject newUser(Object user_id, Object level_id, Object result) throws JSONException {
        JSONObject userDetails = new JSONObject();
        userDetails.put("user_id", user_id);
        userDetails.put("level_id", level_id);
        userDetails.put("result", result);

        return userDetails;
    }

    private static JSONArray findUsersById(int user_id) throws JSONException {
        JSONArray arrayFindedUsers = new JSONArray();

        for (int i = 0; i < mainJsonArray.length(); i++) {
            JSONObject user = mainJsonArray.getJSONObject(i);

            if (user.get("user_id").equals(user_id)) {
                arrayFindedUsers.put(newUser(user.get("user_id"), user.get("level_id"), user.get("result")));
            }
        }

        return sortList(arrayFindedUsers);
    }

    private static JSONArray findUsersByLevel(int level_id) throws JSONException {
        JSONArray arrayFindedUsers = new JSONArray();

        for (int i = 0; i < mainJsonArray.length(); i++) {
            JSONObject user = mainJsonArray.getJSONObject(i);

            if (user.get("level_id").equals(level_id)) {
                arrayFindedUsers.put(newUser(user.get("user_id"), user.get("level_id"), user.get("result")));
            }
        }

        return sortList(arrayFindedUsers);
    }

    private static JSONArray sortList(JSONArray arrayFindedUsers) throws JSONException {
        List<JSONObject> sortedList = new ArrayList<>();
        JSONArray result = new JSONArray();

        for (int i = 0; i < arrayFindedUsers.length(); i++) {
            sortedList.add(arrayFindedUsers.getJSONObject(i));
        }
        sortedList.sort((lhs, rhs) -> {
            Integer lid;
            Integer rid;
            try {
                lid = (Integer) lhs.get(KEY_NAME);
                rid = (Integer) rhs.get(KEY_NAME);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            return rid.compareTo(lid);
        });

        int lengthOfArrayFindedUsers = arrayFindedUsers.length();
        if (lengthOfArrayFindedUsers < TOP20) {
            for(int i = 0; i < lengthOfArrayFindedUsers; i++) {
                result.put(sortedList.get(i));
            }
        } else {
            for(int i = 0; i < TOP20; i++) {
                result.put(sortedList.get(i));
            }
        }
        return result;
    }
}
