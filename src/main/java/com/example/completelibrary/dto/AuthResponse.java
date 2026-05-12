package com.example.completelibrary.dto;



import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@AllArgsConstructor
@Data

public class AuthResponse {
   private String name;
   private String role;
   private String token;
   private Date expireIn;
   private String message;

}
