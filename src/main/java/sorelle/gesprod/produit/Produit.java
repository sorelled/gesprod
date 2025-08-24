package sorelle.gesprod.produit;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import sorelle.gesprod.category.Category;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "produits")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "category")

public class Produit {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    @Size(min = 3, max = 48, message = "{Size.produit.nomProduit}")
    private String nomProduit;

    @Column(nullable = false)
    @Min(value = 25, message = "{Min.produit.prixProduit}")
    @Max(value = 500000, message = "{Max.produit.prixProduit}")
    private int prixProduit;

    @Column(nullable = false)
    @FutureOrPresent(message = "{FutureOrPresent.produit.dateProduit}")
    private LocalDate dateExpiration;

    @ManyToOne
    @JoinColumn(name = "category")
    @JsonIgnoreProperties("produits")
    private Category category;
}
