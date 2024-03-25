package com.example.demo.domain.models;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReturnSendEmail {
    private Boolean returnReminderEmails;
    private Boolean returnOverdueReminderEmails;
    private Boolean returnOverdueBeyond30DayReminderEmails;

    public ReturnSendEmail init(){
        return ReturnSendEmail.builder()
                .returnReminderEmails(false)
                .returnOverdueReminderEmails(false)
                .returnOverdueBeyond30DayReminderEmails(false).build();
    }
}