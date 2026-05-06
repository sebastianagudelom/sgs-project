package com.uniquindio.backend.config;

import com.mercadopago.MercadoPagoConfig;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class MercadoPagoSetup {

    private final MercadoPagoProperties mercadoPagoProperties;

    @PostConstruct
    public void init() {
        MercadoPagoConfig.setAccessToken(mercadoPagoProperties.getAccessToken());
    }
}
