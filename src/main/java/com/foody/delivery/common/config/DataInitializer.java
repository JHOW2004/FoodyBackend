package com.foody.delivery.common.config;

import com.foody.delivery.product.model.Product;
import com.foody.delivery.product.model.ProductCategory;
import com.foody.delivery.product.repository.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final ProductRepository productRepository;

    public DataInitializer(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void run(String... args) {
        if (productRepository.count() == 0) {
            log.info("Nenhum produto cadastrado no banco de dados. Inicializando cardápio inicial de 13 produtos...");

            List<Product> seedProducts = List.of(
                // 1. HAMBÚRGUERES (3)
                new Product("Foody Classic Burger", "Pão brioche, 180g de blend bovino artesanal, queijo cheddar fatiado, alface, tomate e molho da casa", new BigDecimal("29.90"), ProductCategory.HAMBURGUER, null),
                new Product("Foody Smash Double Cheese", "Pão prensado na manteiga, 2x smash burger de 90g, 4 fatias de queijo prato, picles e maionese especial", new BigDecimal("34.90"), ProductCategory.HAMBURGUER, null),
                new Product("Foody Bacon Supreme", "Pão australiano, 200g de blend bovino, bacon crocante em tiras, queijo gorgonzola cremosa e cebola caramelizada", new BigDecimal("39.90"), ProductCategory.HAMBURGUER, null),

                // 2. CACHORROS QUENTES (2)
                new Product("Cachorro Quente Duplo Especial", "Pão macio de hot dog, 2 salsichas premiadas, purê de batata caseiro, milho, vinagrete, molho de tomate e batata palha", new BigDecimal("22.90"), ProductCategory.CACHORRO_QUENTE, null),
                new Product("Cachorro Quente Prensado Supremo", "Pão prensado na chapa, salsicha bovina, queijo muçarela derretido, bacon picado, catupiry e batata palha crocante", new BigDecimal("25.90"), ProductCategory.CACHORRO_QUENTE, null),

                // 3. SANDUÍCHES (3)
                new Product("Sanduíche Natural de Frango", "Pão integral artesanal, frango desfiado temperado, maionese leve de ervas, cenoura ralada, alface americana e tomate", new BigDecimal("18.90"), ProductCategory.SANDUICHE, null),
                new Product("Club Sandwich Gourmet", "Pão de forma artesanal tostado, peito de peru, queijo prato, bacon, ovo frito, alface, tomate e maionese dijon", new BigDecimal("27.90"), ProductCategory.SANDUICHE, null),
                new Product("Sanduíche de Costela Desfiada", "Pão baguete de fermentação natural, costela bovina assada por 12h e desfiada, queijo provolone e molho barbecue", new BigDecimal("32.90"), ProductCategory.SANDUICHE, null),

                // 4. BEBIDAS (2)
                new Product("Refrigerante Guaraná 350ml", "Lata 350ml gelada", new BigDecimal("6.50"), ProductCategory.BEBIDA, null),
                new Product("Suco Natural de Laranja 500ml", "Suco 100% natural de laranja espremida na hora, garrafa de 500ml", new BigDecimal("9.90"), ProductCategory.BEBIDA, null),

                // 5. SOBREMESAS (3)
                new Product("Pudim de Leite Condensado", "Pudim tradicional super cremoso com calda de caramelo caseira (fatia 150g)", new BigDecimal("12.90"), ProductCategory.SOBREMESA, null),
                new Product("Brownie com Sorvete de Creme", "Brownie de chocolate meio amargo aquecido servido com uma bola de sorvete de creme e calda de chocolate", new BigDecimal("18.90"), ProductCategory.SOBREMESA, null),
                new Product("Torta Holandesa Especial", "Fatia de torta holandesa com creme aveludado, cobertura de ganache de chocolate e biscoito calipso", new BigDecimal("16.90"), ProductCategory.SOBREMESA, null)
            );

            productRepository.saveAll(seedProducts);
            log.info("Cardápio inicial de 13 produtos cadastrado com sucesso!");
        } else {
            log.info("Banco de dados já contém {} produtos cadastrados. Nenhuma ação de seed executada.", productRepository.count());
        }
    }
}
