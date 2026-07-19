package org.example.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.enumeration.RetryStatus;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RetryResultsDto {
    private Long notificationId;
    private boolean success;
    private String errorMessage;
    private RetryStatus newStatus;
}
