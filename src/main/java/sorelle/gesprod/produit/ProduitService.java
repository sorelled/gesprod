package sorelle.gesprod.produit;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import sorelle.gesprod.category.Category;
import sorelle.gesprod.category.CategoryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProduitService {

    private final ProduitRepository produitRepository;
    private final ProduitMapper produitMapper;
    private final CategoryRepository categoryRepository;

    private static Boolean isPerime(LocalDate dateExpiration) {
        return dateExpiration.isBefore(LocalDate.now());
    }

    // Liste de tous les produits
    public List<ProduitDTO> getAllProduits() {
        return produitRepository.findAll()
                .stream()
                .map(produitMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer un produitDTO par son id
    public ProduitDTO getProduitDTOById(UUID id) {
        Produit produit = produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non existant !"));
        return produitMapper.toDTO(produit);
    }

    // Récupérer un produit par son id
    public Produit getProduitById(UUID id) {
        return produitRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Produit non existant !"));
    }

    // Enregistrer un nouveau produit à partir d'un DTO
    public ProduitDTO createProduit(ProduitDTO produitDTO) {
        // Vérifie si le produit existe déjà
        if (produitRepository.existsByNomProduitIgnoreCase(produitDTO.getNomProduit())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Ce produit existe déjà en BD !");
        }

        // On récupère la catégorie du produit
        Category category = categoryRepository.findById(produitDTO.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Catégorie non existante !"));

        // Nouveau produit
        Produit produit = new Produit();
        produit.setNomProduit(produitDTO.getNomProduit());
        produit.setPrixProduit(produitDTO.getPrixProduit());
        produit.setDateExpiration(produitDTO.getDateExpiration());
        produit.setCategory(category);

        Produit savedProduit = produitRepository.save(produit);
        return produitMapper.toDTO(savedProduit);
    }

    // Mettre à jour les informations sur un produit existant
    public ProduitDTO updateProduit(UUID id, ProduitDTO updatedProduitDTO) {
        return produitRepository.findById(id).map(existingProduit -> {
            existingProduit.setNomProduit(updatedProduitDTO.getNomProduit());
            existingProduit.setPrixProduit(updatedProduitDTO.getPrixProduit());
            existingProduit.setDateExpiration(updatedProduitDTO.getDateExpiration());

            // Mise à jour éventuelle de la catégorie
            if (updatedProduitDTO.getCategoryId() != null) {
                existingProduit.setCategory(
                        categoryRepository.findById(updatedProduitDTO.getCategoryId())
                                .orElseThrow(() -> new IllegalArgumentException("Catégorie non existante !"))
                );
            }

            return produitMapper.toDTO(produitRepository.save(existingProduit));
        }).orElseThrow(() -> new IllegalArgumentException("Produit non existant !"));
    }

    // Supprimer un produit par son ID
    public void deleteProduit(UUID id) {
        if (!produitRepository.existsById(id)) {
            throw new IllegalArgumentException("Produit non existant !");
        }
        produitRepository.deleteById(id);
    }

    // Liste des produits périmés
    public List<ProduitDTO> getProduitsPerimes() {
        return produitRepository.findAll().stream()
                .filter(p -> isPerime(p.getDateExpiration()))
                .map(produitMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Liste des produits par catégorie
    public List<ProduitDTO> getProduitsByCategory(UUID categoryId) {
        List<Produit> produits = produitRepository.findByCategoryId(categoryId);
        return produits.stream()
                .map(produitMapper::toDTO)
                .collect(Collectors.toList());
    }
}
