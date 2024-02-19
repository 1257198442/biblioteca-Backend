package com.example.demo.domain.service;

import com.example.demo.TestConfig;
import com.example.demo.domain.models.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@TestConfig
public class RoleServiceTests {
    @Autowired
    private RoleService roleService;
    @Test
    public void testIsCompetentReturnsTrueWhenRoleIsPresent() {
        Role role1 = Role.ROOT;
        Role role2 = Role.ADMINISTRATOR;
        Role role3 = Role.CLIENT;
        List<Role> roleList = Arrays.asList(role1, role2);
        assertTrue(roleService.isCompetent(roleList, role1));
        assertFalse(roleService.isCompetent(roleList, role3));
    }

}
