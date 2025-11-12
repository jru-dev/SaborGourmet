package com.example.saborgourmet.config;

import com.example.saborgourmet.model.Usuario;
import com.example.saborgourmet.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner initDatabase(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            // Solo crear usuarios si no existen
            if (usuarioRepository.count() == 0) {
                System.out.println("Inicializando usuarios de prueba...");

                // ADMIN
                Usuario admin = Usuario.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin123"))
                        .nombre("Administrador")
                        .apellido("Sistema")
                        .rol("ADMIN")
                        .estado("activo")
                        .enabled(true)
                        .build();

                // MOZO
                Usuario mozo = Usuario.builder()
                        .username("mozo")
                        .password(passwordEncoder.encode("mozo123"))
                        .nombre("Juan")
                        .apellido("Pérez")
                        .rol("MOZO")
                        .estado("activo")
                        .enabled(true)
                        .build();

                // COCINERO
                Usuario cocinero = Usuario.builder()
                        .username("cocinero")
                        .password(passwordEncoder.encode("cocinero123"))
                        .nombre("María")
                        .apellido("González")
                        .rol("COCINERO")
                        .estado("activo")
                        .enabled(true)
                        .build();

                // CAJERO
                Usuario cajero = Usuario.builder()
                        .username("cajero")
                        .password(passwordEncoder.encode("cajero123"))
                        .nombre("Carlos")
                        .apellido("Rodríguez")
                        .rol("CAJERO")
                        .estado("activo")
                        .enabled(true)
                        .build();

                usuarioRepository.save(admin);
                usuarioRepository.save(mozo);
                usuarioRepository.save(cocinero);
                usuarioRepository.save(cajero);

                System.out.println("✅ Usuarios de prueba creados exitosamente");
                System.out.println("   - admin/admin123 (ADMIN)");
                System.out.println("   - mozo/mozo123 (MOZO)");
                System.out.println("   - cocinero/cocinero123 (COCINERO)");
                System.out.println("   - cajero/cajero123 (CAJERO)");
            }
        };
    }
}