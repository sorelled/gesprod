package sorelle.gesprod.produit;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ProduitDTO {
   private UUID id;

   @Size(min = 3, max = 48, message = "{Size.produit.nomProduit}")
   private String nomProduit;

   @Min(value = 25, message = "{Min.produit.prixProduit}")
   @Max(value = 500000, message = "{Max.produit.prixProduit}")
   private int prixProduit;

   @FutureOrPresent(message = "{FutureOrPresent.produit.dateProduit}")
   private LocalDate dateExpiration;

   private UUID categoryId; // on évite de mettre toute la Category ici
}
