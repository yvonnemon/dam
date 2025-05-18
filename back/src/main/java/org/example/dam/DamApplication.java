package org.example.dam;

import org.example.dam.util.HelpBotSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class DamApplication {
    public static void main(String[] args) {
        SpringApplication.run(DamApplication.class, args);
        new HelpBotSocketServer().start();
    }
    //TODO borrar esto
}
