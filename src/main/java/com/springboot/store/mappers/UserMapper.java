package com.springboot.store.mappers;

import com.springboot.store.dtos.RegisterUserRequest;
import com.springboot.store.dtos.UpdateUserRequest;
import com.springboot.store.dtos.UserDto;
import com.springboot.store.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

// componentModel = "spring" - spring can create beans of this type at runtime
@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface UserMapper {
    UserDto toDto(User user);
    User toEntity(RegisterUserRequest registerUserRequest);

    @Mapping(target = "id", ignore = true) // don't change the ID
    void updateUserFromDto(UpdateUserRequest dto, @MappingTarget User entity);
}