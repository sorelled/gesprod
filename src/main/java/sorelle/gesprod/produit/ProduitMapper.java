package sorelle.gesprod.produit;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import sorelle.gesprod.category.Category;
import sorelle.gesprod.category.CategoryRepository;

@Component
@RequiredArgsConstructor
public class ProduitMapper {

    private final CategoryRepository categoryRepository;

    public ProduitDTO toDTO(Produit produit) {
        // Méthode 1: En utilisant le builder
        return ProduitDTO.builder()
                .id(produit.getId())
                .nomProduit(produit.getNomProduit())
                .prixProduit(produit.getPrixProduit())
                .dateExpiration(produit.getDateExpiration())
                .categoryId(produit.getCategory() != null ?
                        produit.getCategory().getId() : null)
                .build();
    }

    public Produit toEntity(ProduitDTO produitDTO) {
        // Méthode 2: En créant une nouvelle instance de l'entité
        Produit produit = new Produit();
        produit.setId(produitDTO.getId());
        produit.setNomProduit(produitDTO.getNomProduit());
        produit.setPrixProduit(produitDTO.getPrixProduit());
        produit.setDateExpiration(produitDTO.getDateExpiration());

        if (produitDTO.getCategoryId() != null) {
            Category category = categoryRepository.findById(produitDTO.getCategoryId())
                    .orElse(null);
            produit.setCategory(category);
        }

        return produit;
    }
}

