package com.example.demo.adapters.rest.show;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LendingStatisticsShow {
    private int today;
    private int thisWeek;
    private int thisMonth;
    private int thisYear;
    private int all;
}
