package com.uniquindio.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * Envía un correo con el código de verificación de 6 dígitos.
     */
    public void enviarCodigoVerificacion(String destinatario, String codigo) {
        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(destinatario);
        mensaje.setSubject("SGS Supermercado - Código de verificación");
        mensaje.setText(
                "¡Bienvenido a SGS Supermercado!\n\n" +
                "Tu código de verificación es: " + codigo + "\n\n" +
                "Este código expira en 15 minutos.\n\n" +
                "Si no solicitaste este registro, ignora este correo."
        );

        mailSender.send(mensaje);
    }
}
