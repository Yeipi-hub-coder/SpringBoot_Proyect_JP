package com.ProyectoSpringBoot.JP.Auth;

import com.ProyectoSpringBoot.JP.Model.Usuario;
import com.ProyectoSpringBoot.JP.exception.BadRequestException;
import com.ProyectoSpringBoot.JP.Repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthServiceImpl(
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest dto) {

        Usuario usuario = usuarioRepository.findByEmail(dto.email())
                .orElseThrow(() ->
                        new BadRequestException("Credenciales inválidas"));

        if (!passwordEncoder.matches(
                dto.password(),
                usuario.getPassword()
        )) {
            throw new BadRequestException("Credenciales inválidas");
        }

        String token = jwtService.generarToken(usuario);

        return new LoginResponse(
                token,
                usuario.getId(),
                usuario.getNombre(),
                usuario.getEmail()
        );
    }
}