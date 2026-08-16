package com.fhlbdm.eadvantage.models;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
 
@Data
@AllArgsConstructor
public class ErrorResponse {

    private LocalDateTime timestamp;
    private int status;
    private String message;
    private String path;
    private String requestId;
    private String userId;

    
}
