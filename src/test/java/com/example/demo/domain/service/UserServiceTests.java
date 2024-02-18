package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.UserUploadDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@TestConfig
public class UserServiceTests {
    @Autowired
    private UserService userService;
    @Test
    void testLogin(){
        assertNotNull(userService.login("+34645321068"));
        assertThrows(NotFoundException.class, () -> {
            userService.login("+34666000002");
        });
        assertThrows(ForbiddenException.class, () -> {
            userService.login("666");
        });
    }
    @Test
    void testCreateAndRead(){
        UserUploadDto userUploadDto = UserUploadDto.builder()
                .email("test@gmail.com")
                .name("test10")
                .password("6")
                .telephone("+34666000020")
                .build();
        assertThat(userService.create(userUploadDto)).isNotNull();
        User user = userService.read(userUploadDto.getTelephone());
        assertEquals(user.getName(),userUploadDto.getName());
        assertEquals(user.getTelephone(),userUploadDto.getTelephone());
        assertNull(user.getPassword());
        assertEquals(Role.CLIENT,user.getRole());

        assertThrows(NotFoundException.class, () -> {
            userService.read("null");
        });

        assertThrows(ConflictException.class, () -> {
            userService.create(userUploadDto);
        });
    }
}
