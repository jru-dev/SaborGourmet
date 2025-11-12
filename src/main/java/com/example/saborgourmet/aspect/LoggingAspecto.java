package com.example.saborgourmet.aspect;

import com.example.saborgourmet.model.Auditoria;
import com.example.saborgourmet.model.Cliente;
import com.example.saborgourmet.model.Mesa;
import com.example.saborgourmet.repository.AuditoriaRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Calendar;

@Component
@Aspect
public class LoggingAspecto {

    private Long tx;

    @Autowired
    private AuditoriaRepository auditoriaRepository;

    /**
     * Obtiene el nombre del usuario autenticado actual
     */
    private String obtenerUsuarioActual() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return "sistema";
    }

    /**
     * Aspecto que envuelve la ejecución de métodos de servicios
     * para registrar entrada, salida y tiempo de ejecución
     */
    @Around("execution(* com.example.saborgourmet.service.*Service.*(..))")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // Identificador de transacción basado en timestamp
        tx = System.currentTimeMillis();

        // Obtener logger y nombre del método
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String metodo = joinPoint.getSignature().getName();
        String usuario = obtenerUsuarioActual();

        // Registrar entrada con argumentos si existen
        if (joinPoint.getArgs().length > 0) {
            logger.info("tx[{}] - Usuario[{}] - {}() INPUT: {}", tx, usuario, metodo, Arrays.toString(joinPoint.getArgs()));
        }

        // Medir tiempo de inicio
        long currTime = System.currentTimeMillis();
        Object result = null;

        try {
            // Ejecutar el método interceptado
            result = joinPoint.proceed();
        } catch (Throwable e) {
            logger.error("tx[{}] - Usuario[{}] - {}() ERROR: {}", tx, usuario, metodo, e.getMessage());
            throw e;
        }

        // Registrar tiempo transcurrido
        logger.info("tx[{}] - Usuario[{}] - {}(): tiempo transcurrido {} ms.", tx, usuario, metodo, (System.currentTimeMillis() - currTime));

        return result;
    }

    /**
     * Aspecto que se ejecuta DESPUÉS de los métodos guardar, actualizar y eliminar
     * para registrar la auditoría en la base de datos
     */
    @After("execution(* com.example.saborgourmet.controller.*Controller.guardar*(..)) || " +
            "execution(* com.example.saborgourmet.controller.*Controller.actualizar*(..)) || " +
            "execution(* com.example.saborgourmet.controller.*Controller.eliminar*(..))")
    public void auditoria(JoinPoint joinPoint) throws Throwable {
        // Obtener logger y nombre del método
        Logger logger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());
        String metodo = joinPoint.getSignature().getName();
        String usuarioActual = obtenerUsuarioActual();

        String tabla = "";
        Long id = null;

        // Identificar la tabla y el ID según el método ejecutado
        if (metodo.startsWith("guardar")) {
            // Para guardar, el primer parámetro es el objeto completo
            Object[] parametros = joinPoint.getArgs();

            if (parametros[0] instanceof Cliente) {
                Cliente cliente = (Cliente) parametros[0];
                id = cliente.getIdCliente();
                tabla = "clientes";
            } else if (parametros[0] instanceof Mesa) {
                Mesa mesa = (Mesa) parametros[0];
                id = mesa.getIdMesa();
                tabla = "mesas";
            }
        } else if (metodo.startsWith("actualizar")) {
            // Para actualizar, el primer parámetro es el ID
            Object[] parametros = joinPoint.getArgs();
            id = (Long) parametros[0];

            // Determinar la tabla según la clase del controlador
            String nombreClase = joinPoint.getTarget().getClass().getSimpleName();
            if (nombreClase.contains("Cliente")) {
                tabla = "clientes";
            } else if (nombreClase.contains("Mesa")) {
                tabla = "mesas";
            }
        } else if (metodo.startsWith("eliminar")) {
            // Para eliminar, el primer parámetro es el ID
            Object[] parametros = joinPoint.getArgs();
            id = (Long) parametros[0];

            // Determinar la tabla según la clase del controlador
            String nombreClase = joinPoint.getTarget().getClass().getSimpleName();
            if (nombreClase.contains("Cliente")) {
                tabla = "clientes";
            } else if (nombreClase.contains("Mesa")) {
                tabla = "mesas";
            }
        }

        // Registrar la auditoría en logs y base de datos
        if (id != null && !tabla.isEmpty()) {
            String traza = "tx[" + tx + "] - Usuario[" + usuarioActual + "] - " + metodo;
            logger.info("{}: registrando auditoria en tabla '{}' para ID {}", traza, tabla, id);

            // Guardar en la base de datos con el usuario autenticado
            auditoriaRepository.save(new Auditoria(
                    tabla,
                    id,
                    Calendar.getInstance().getTime(),
                    usuarioActual, // Usuario actual obtenido de Spring Security
                    metodo
            ));
        }
    }
}