package com.example.demo.adapters.rest;


import com.example.demo.adapters.rest.dto.LendingDataUploadDto;
import com.example.demo.adapters.rest.show.AdminReturnAndLendingByShow;
import com.example.demo.adapters.rest.show.ClientReturnAndLendingByShow;
import com.example.demo.adapters.rest.show.LendingStatisticsShow;
import com.example.demo.domain.exceptions.ForbiddenException;
import com.example.demo.domain.exceptions.UnauthorizedException;
import com.example.demo.domain.models.LendingData;
import com.example.demo.domain.models.Role;
import com.example.demo.domain.service.LendingDataService;
import com.example.demo.domain.service.ReturnDataService;
import com.example.demo.domain.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(LendingDataResource.LENDING_DATA)
public class LendingDataResource {
    public static final String LENDING_DATA = "/lendingData";
    public static final String REFERENCE = "/{reference}";
    public static final String SEARCH = "/search";
    public static final String NO_RETURN = "/no_return";
    public static final String CLIENT_RETURN_AND_LENDING = "/client_return_and_lending";
    public static final String ADMIN_RETURN_AND_LENDING = "/admin_return_and_lending";
    public static final String LENDING_STATISTICS="/lending_statistics";
    public static final String MONTHLY_COUNTS="/monthly_counts";
    public static final String WEEk_COUNTS="/weekly_counts";
    public static final String YEAR_COUNTS="/yearly_counts";
    public static final String SEND_EMAIL_TO_USER_BY_OVERDUE_MAX_30DAY="/send_email_to_user_by_overdue_max_30day";
    public static final String READ_LENDING_DATA_BY_OVERDUE_MAX_30DAY="/read_lending_data_by_overdue_max_30day";
    public static final String SEND_EMAIL_TO_USER_BY_APPROACHING_DATE="/send_email_to_user_by_approaching_date";
    public static final String SEND_EMAIL_TO_USER_BY_ORDER_OVERDUE="/send_email_to_user_by_order_overdue";
    public final LendingDataService lendingDataService;
    public final ReturnDataService returnDataService;
    public final UserService userService;
    public final List<Role> adminRole= Arrays.asList(Role.ADMINISTRATOR,Role.ROOT);

    @Autowired
    public LendingDataResource(LendingDataService lendingDataService,
                               UserService userService,
                               ReturnDataService returnDataService){
        this.lendingDataService = lendingDataService;
        this.userService = userService;
        this.returnDataService = returnDataService;
    }

    //POST
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping
    public LendingData create(@RequestBody LendingDataUploadDto lendingData){
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        if (hasPermission(adminRole,lendingData.getTelephone())) {
            if(!encoder.matches(lendingData.getPassword(),this.userService.getUserPassword(lendingData.getTelephone()))){
                throw new UnauthorizedException("The password is wrong.");
            }
            return this.lendingDataService.create(lendingData);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    //GET
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(REFERENCE)
    public LendingData read(@PathVariable String reference){
        LendingData lending = this.lendingDataService.read(reference);
        if(hasPermission(adminRole,lending.getUser().getTelephone())){
            return lending;
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(SEARCH)
    public List<LendingData> readByTelephone(@RequestParam String telephone){
        if(hasPermission(adminRole,telephone)){
            return this.lendingDataService.readAllByUserTelephone(telephone);
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT') or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(CLIENT_RETURN_AND_LENDING +SEARCH)
    public ClientReturnAndLendingByShow readClientReturnAndLendingByShow(@RequestParam String telephone){
        if(hasPermission(adminRole,telephone)){
            return ClientReturnAndLendingByShow.builder()
                    .lendingDataList(this.lendingDataService.readAllByUserTelephone(telephone))
                    .returnDataList(this.returnDataService.readAllByUserTelephone(telephone)).build();
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(ADMIN_RETURN_AND_LENDING)
    public AdminReturnAndLendingByShow readAdminReturnAndLendingByShow(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return AdminReturnAndLendingByShow.builder()
                    .lendingDataList(this.lendingDataService.readAll())
                    .returnDataList(this.returnDataService.readAll())
                    .returnBox(this.returnDataService.readAllByWaitingForVerification()).build();
        }else {
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(NO_RETURN+SEARCH)
    public List<LendingData> readNoReturnByTelephone(@RequestParam String telephone){
        if(hasPermission(adminRole,telephone)){
            return this.lendingDataService.readNoReturnByTelephone(telephone);
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(LENDING_STATISTICS)
    public LendingStatisticsShow readLendingStatistics(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.getLendingStatistics();
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(MONTHLY_COUNTS)
    public List<Integer> readLendingMonthlyCountsByThisYear(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.getLendingMonthlyCountsByThisYear();
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(WEEk_COUNTS)
    public List<Integer> readLendingDailyCountsByThisWeek(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.getLendingDailyCountsByThisWeek();
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT')  or hasRole('CLIENT')" )
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(YEAR_COUNTS)
    public List<Integer> readLendingYearlyCounts(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.getLendingYearlyCounts();
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT') or hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(SEND_EMAIL_TO_USER_BY_OVERDUE_MAX_30DAY)
    public List<LendingData> banUserByOverdueMax30Days(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.sendEmailToUserByExtensionBeyond30Days();
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT') or hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    @GetMapping(READ_LENDING_DATA_BY_OVERDUE_MAX_30DAY)
    public List<LendingData> readUserByOverdueMax30Days(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.readLendingDataByExtensionBeyond30Days().collect(Collectors.toList());
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT') or hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(SEND_EMAIL_TO_USER_BY_APPROACHING_DATE)
    public List<LendingData> sendEmailToUserByApproachingExpiryDate(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.sendEmailToUserByApproachingExpiryDate();
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }
    }

    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('ROOT') or hasRole('CLIENT')")
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping(SEND_EMAIL_TO_USER_BY_ORDER_OVERDUE)
    public List<LendingData> sendEmailToUserByOrderOverdue(){
        if(Role.isCompetent(adminRole,this.extractRoleClaims())){
            return this.lendingDataService.sendEmailToUserByOrderOverdue();
        }else{
            throw new ForbiddenException("You don't have permission to make this request.");
        }

    }

    private Role extractRoleClaims() {
        List< String > roleClaims = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        return Role.of(roleClaims.get(0));
    }

    private String extractUserName() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    public boolean hasPermission(List<Role> requiredRoles, String targetTelephone) {
        boolean hasRolePermission = Role.isCompetent(requiredRoles, this.extractRoleClaims());
        boolean isTargetUser = extractUserName().equals(targetTelephone);
        return hasRolePermission || isTargetUser;
    }

}