package com.training.abarsukov.helpdesk.converter.impl;

import com.training.abarsukov.helpdesk.converter.UserConverter;
import com.training.abarsukov.helpdesk.dto.UserDto;
import com.training.abarsukov.helpdesk.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverterV1 implements UserConverter {

  @Override
  public UserDto convertToDto(User user) {
    return UserDto.builder()
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .build();
  }

  @Override
  public User convertToEntity(UserDto userDto) {
    return User.builder()
        .firstName(userDto.getFirstName())
        .lastName(userDto.getLastName())
        .build();
  }
}
