package pe.edu.vallegrande.api9demo.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

/**
 * 🔒 CONFIGURACIÓN DE SEGURIDAD - API9:2023 COMPLIANT
 * 
 * Implementa protecciones específicas contra API9:2023 Improper Inventory Management:
 * - Restringe acceso a documentación en producción
 * - Bloquea endpoints de inventario en producción
 * - Controla acceso a endpoints sensibles por ambiente
 */
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Value("${app.environment:development}")
    private String environment;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(exchanges -> {
                
                // 🏥 ENDPOINTS PÚBLICOS DE SALUD (siempre disponibles)
                exchanges.pathMatchers("/actuator/health", "/actuator/info").permitAll();
                exchanges.pathMatchers("/api/v2/health", "/api/v2/health/version").permitAll();
                
                // 🚨 CONTROL POR AMBIENTE - CLAVE PARA API9:2023
                if ("production".equals(environment)) {
                    // 🔒 EN PRODUCCIÓN: Bloquear endpoints sensibles
                    exchanges.pathMatchers(
                        "/swagger-ui/**", 
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/webjars/**",
                        "/api/inventory/**",
                        "/api/v2/health/security"  // Info de seguridad solo en dev
                    ).denyAll();
                    
                    log.info("🔒 PRODUCCIÓN: Endpoints sensibles bloqueados");
                    
                } else {
                    // 🔧 EN DESARROLLO: Permitir documentación e inventario
                    exchanges.pathMatchers(
                        "/swagger-ui/**", 
                        "/swagger-ui.html",
                        "/v3/api-docs/**",
                        "/webjars/**"
                    ).permitAll();
                    
                    // 🔍 Inventario de APIs solo para administradores en desarrollo
                    exchanges.pathMatchers("/api/inventory/**").hasRole("ADMIN");
                    exchanges.pathMatchers("/api/v2/health/security").hasRole("ADMIN");
                    
                    log.info("🔧 DESARROLLO: Documentación e inventario habilitados");
                }
                
                // 🔐 ENDPOINTS DE NEGOCIO: Requieren autenticación
                exchanges.pathMatchers("/api/v2/users/**").authenticated();
                exchanges.pathMatchers("/api/v2/qualitytests/**").authenticated();
                
                // 🛡️ ACTUATOR SENSIBLE: Solo administradores
                exchanges.pathMatchers("/actuator/**").hasRole("ADMIN");
                
                // 🚫 TODO LO DEMÁS: Requiere autenticación
                exchanges.anyExchange().authenticated();
            })
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwkSetUri("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}"))
            )
            .build();
    }
    
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(SecurityConfig.class);
}