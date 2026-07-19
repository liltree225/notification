package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetryResponseDto {
    private Integer total;
    private Integer successCount;
    private Integer failedCount;
    private List<RetryResultsDto> results = new ArrayList<>();
}
