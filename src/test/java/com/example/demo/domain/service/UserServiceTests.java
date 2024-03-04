package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.adapters.rest.dto.SettingUpdateDto;
import com.example.demo.adapters.rest.dto.UserUpdateDto;
import com.example.demo.adapters.rest.dto.UserUploadDto;
import com.example.demo.domain.exceptions.ConflictException;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.NotFoundException;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

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
            userService.login("66600002");
        });
        assertThrows(ForbiddenException.class, () -> {
            userService.login("+34666");
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
        assertEquals(true,user.getSetting().getHideMyProfile());
        assertEquals("This user has not modified his profile",user.getDescription());
        assertEquals(LocalDate.of(1990,1,1),user.getBirthdays());
        assertThrows(NotFoundException.class, () -> {
            userService.read("null");
        });
        assertThrows(ConflictException.class, () -> {
            userService.create(userUploadDto);
        });
    }
    @Test
    void testUpdateAdminROOT(){
        assertThrows(ForbiddenException.class,()->userService.updateRoleROOT("+34666000020","ROOT"));
        assertThat(userService.updateRoleROOT("+34666000020","ADMINISTRATOR")).isNotNull();
    }

    @Test
    void testUpdateAdminADMINISTRATOR(){
        assertThrows(ForbiddenException.class,()->userService.updateRoleADMINISTRATOR("+34666000020","ROOT"));
        assertThat(userService.updateRoleADMINISTRATOR("+34666000020","ADMINISTRATOR")).isNotNull();
        assertThrows(ForbiddenException.class,()->userService.updateRoleADMINISTRATOR("+34666000020","BAN"));
        assertThrows(ForbiddenException.class,()->userService.updateRoleADMINISTRATOR("+34666000020","CLIENT"));
    }

    @Test
    void testUpdate(){
        UserUpdateDto useruserUpdateDto = new UserUpdateDto();
        useruserUpdateDto.setName("test-test");
        useruserUpdateDto.setEmail("test@test.com");
        useruserUpdateDto.setBirthdays(LocalDate.of(2000,12,12));
        useruserUpdateDto.setDescription("test");
        userService.update("+34123",useruserUpdateDto);
        User user = userService.read("+34123");
        assertEquals(user.getName(),"test-test");
        assertEquals(user.getEmail(),"test@test.com");
        assertEquals(user.getBirthdays(),LocalDate.of(2000,12,12));
        assertEquals(user.getDescription(),"test");
    }

    @Test
    void testUpdateSetting(){
        SettingUpdateDto settingUpdateDto = new SettingUpdateDto();
        settingUpdateDto.setHideMyProfile(false);
        userService.updateSetting("+34123",settingUpdateDto);
        assertEquals(userService.read("+34123").getSetting().getHideMyProfile(),false);
        settingUpdateDto.setHideMyProfile(true);
        userService.updateSetting("+34123",settingUpdateDto);
        assertEquals(userService.read("+34123").getSetting().getHideMyProfile(),true);
    }
}
