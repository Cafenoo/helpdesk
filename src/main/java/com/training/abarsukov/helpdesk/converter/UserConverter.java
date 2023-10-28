package com.training.abarsukov.helpdesk.converter;

import com.training.abarsukov.helpdesk.dto.UserDto;
import com.training.abarsukov.helpdesk.model.User;

public interface UserConverter {
  UserDto convertToDto(User user);

  User convertToEntity(UserDto userDto);
}
