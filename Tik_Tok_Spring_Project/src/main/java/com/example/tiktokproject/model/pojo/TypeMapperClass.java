package com.example.tiktokproject.model.pojo;

import com.example.tiktokproject.model.dto.userDTO.UserEditRequestDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.Access;

@Component
public class TypeMapperClass {
    private static final ModelMapper modelMapper = new ModelMapper();
    private static TypeMap<UserEditRequestDTO, User> typeMapper;

    public static TypeMap<UserEditRequestDTO, User> getInstance() {
        synchronized (TypeMapperClass.class){
            if (typeMapper == null) {
                typeMapper = modelMapper.createTypeMap(UserEditRequestDTO.class, User.class, modelMapper.getConfiguration().setSkipNullEnabled(true));
            }
        }

        return typeMapper;
    }
}
