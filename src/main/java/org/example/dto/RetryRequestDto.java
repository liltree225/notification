package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetryRequestDto {
    private List<Long> notificationIds = new ArrayList<>();
    private boolean forceRetry = false;
}
