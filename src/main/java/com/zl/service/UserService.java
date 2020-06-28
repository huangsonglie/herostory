package com.zl.service;

import com.zl.GameDbProcessor;
import com.zl.MySqlSessionFactory;
import com.zl.mapper.UserMapper;
import com.zl.model.UserDto;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

public final class UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);

    private static final UserService instance = new UserService();

    public static UserService getInstance() {
        return instance;
    }



    public void login(String userName, String password, Function<UserDto, Void> callback) {
        GameDbProcessor.getInstance().process(()->{
            try (SqlSession session = MySqlSessionFactory.openSession()) {
                UserMapper userMapper = session.getMapper(UserMapper.class);
                UserDto userDto = userMapper.getUserByName(userName);
                if (password.equals(userDto.getPassword())) {
                    callback.apply(userDto);
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        });
    }

    public UserDto getUserByUserId(Integer userId) {
        try (SqlSession session = MySqlSessionFactory.openSession()) {
            UserMapper userMapper = session.getMapper(UserMapper.class);
            UserDto userDto = userMapper.getUserByUserId(userId);
            return userDto;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}
