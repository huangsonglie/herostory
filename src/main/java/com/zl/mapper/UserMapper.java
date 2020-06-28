package com.zl.mapper;

import com.zl.model.User;
import com.zl.model.UserDto;

public interface UserMapper {
    UserDto getUserByName(String userName);

    int insertInto(UserDto userDto);

    UserDto getUserByUserId(Integer userId);
}
