package by.shakhau.ps.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class PaymentSystemProductServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(PaymentSystemProductServiceApplication.class, args);
    }

}
