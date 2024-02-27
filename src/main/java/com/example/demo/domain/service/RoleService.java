package com.example.demo.domain.service;


import com.example.demo.domain.models.Role;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    public boolean isCompetent(List<Role> roleList,Role roleClaim){
        for (Role role:roleList){
            if(role.equals(roleClaim)){
                return true;
            }
        }
        return false;
    }
}
