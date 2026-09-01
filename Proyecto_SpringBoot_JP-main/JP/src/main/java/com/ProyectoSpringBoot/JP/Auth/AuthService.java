package com.ProyectoSpringBoot.JP.Auth;

import com.ProyectoSpringBoot.JP.Auth.LoginRequest;
import com.ProyectoSpringBoot.JP.Auth.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest dto);
}
