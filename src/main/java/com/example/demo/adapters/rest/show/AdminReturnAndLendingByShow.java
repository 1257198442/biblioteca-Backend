package com.example.demo.adapters.rest.show;

import com.example.demo.domain.models.LendingData;
import com.example.demo.domain.models.ReturnData;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AdminReturnAndLendingByShow {
   private List<LendingData> lendingDataList;
   private List<ReturnData> returnDataList;
   private List<ReturnData> returnBox;
}
