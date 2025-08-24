package com.springboot.store.payment;

import com.stripe.Stripe;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "stripe")
/**
 * @ConfigurationProperties needs getters and setters (or @Data / @Getter+@Setter with Lombok),
 * otherwise Spring can’t bind stripe.secretKey from application.yml.
 */
@Data
public class StripeConfig {
    private String secretKey;

    @PostConstruct
    public void init(){
        Stripe.apiKey = secretKey;
    }
}
