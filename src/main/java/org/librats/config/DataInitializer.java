package org.librats.config; // Verifique se o pacote bate com a sua pasta

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component; // Adicione este import

@Component // ADICIONE ESTA ANOTAÇÃO - Ela é o "interruptor" que liga a classe
public class DataInitializer implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        System.out.println("\n\n=======================================");
        System.out.println("   LIB RATS - SISTEMA INICIALIZADO     ");
        System.out.println("   Status: Java 17 + Spring Boot 3.4   ");
        System.out.println("   Desenvolvedor: Giovanni Carvalho    ");
        System.out.println("=======================================\n\n");
    }
}