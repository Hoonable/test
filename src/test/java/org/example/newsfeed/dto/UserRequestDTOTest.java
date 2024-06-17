package org.example.newsfeed.dto;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Valid;
import org.junit.jupiter.api.Test;

class UserRequestDTOTest {

    @Test
    void test1(){
        //given
        String userId = "id";
        String password = "1234";
        String name = "user";
        String comment = "comment";
        String email = "email@emailcom";

        //when
        UserRequestDTO userRequestDTO = new UserRequestDTO(userId, password, name, comment, email);

        //then
        assertEquals(userId, userRequestDTO.getUserId());
    }

}