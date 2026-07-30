package com.foody.delivery;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FoodyDeliveryApplication {

    public static void main(String[] args) {
        // Carrega automaticamente variáveis de ambiente do arquivo .env se existir
        try {
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            dotenv.entries().forEach(entry -> {
                if (System.getProperty(entry.getKey()) == null) {
                    System.setProperty(entry.getKey(), entry.getValue());
                }
            });
        } catch (Exception e) {
            System.out.println("INFO: Arquivo .env não encontrado ou usando padrões do application.properties.");
        }

        SpringApplication.run(FoodyDeliveryApplication.class, args);
    }

}
