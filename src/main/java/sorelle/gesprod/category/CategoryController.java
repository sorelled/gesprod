package sorelle.gesprod.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Tag(name = "Catégories", description = "API pour la gestion des catégories")
public class CategoryController {

    private final CategoryService categoryService;

    /*
    ResponseEntity.ok() indique explicitement :
    - Que la requête a réussi (code 200)
    - Que le corps contient la ressource demandée
    */

    // Liste de toutes les catégorie
    @GetMapping
    @Operation(
            summary = "Liste de toutes les catégories",
            description = "Cette méthode vous permet de lister toutes " +
                    "les catégories de produit qui existent",
            operationId = "getAllCategories",
            tags = {"Catégories"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Succès")
            }
    )
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    // Recuperer une catégorie à partir de son ID
    @GetMapping("/{id}")
    @Operation(
            summary = "Obtenir une catégorie via son ID",
            description = "Cette méthode vous permet de rechercher " +
                    "une catégorie à partir de son identifiant",
            operationId = "getCategoryById",
            tags = {"Catégories"},
            responses = {
                    @ApiResponse(responseCode = "200", description = "Succès")
            }
    )
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable UUID id) {
        return ResponseEntity.ok(categoryService.getCategoryByID(id));
    }

    // Enregistrer une nouvelle catégorie
    @PostMapping
    @Operation(
            summary = "Enregistrer une nouvelle catégorie",
            description = "Cette fonction vous permet d'enregistrer " +
                    "une nouvelle catégorie en base de données ",
            operationId = "createCategory",
            tags = {"Catégories"},
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Catégorie créée avec succès"),
                    @ApiResponse(responseCode = "400",
                            description = "Catégorie déjà existante")
            }
    )
    public ResponseEntity<Category> createCategory(
            @Valid @RequestBody CategoryDTO categoryDTO)
    {
        Category created = categoryService.createCategoryWithBuilder(categoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    // Mettre à jour une catégorie existante
    @PutMapping("/{id}")
    @Operation(
            summary = "Mettre à jour une catégorie existante",
            description = "Cette fonction vous permet de modifier " +
                    "les informations d'une catégorie déjà existante",
            operationId = "updateCategory",
            tags = {"Catégories"},
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Catégorie mise à jour avec succès"),
                    @ApiResponse(responseCode = "400",
                            description = "Catégorie déjà existante")
            }
    )
    public ResponseEntity<CategoryDTO> updateCategory(
            @PathVariable UUID id,
            @Valid @RequestBody CategoryDTO categoryDTO)
    {
        return ResponseEntity.ok(categoryService.updateCategory(id, categoryDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Supprimer une catégorie",
            description = "Cette fonction vous permet de supprimer " +
                    "de la base de données les informations d'une catégorie déjà existante",
            operationId = "deleteCategory",
            tags = {"Catégories"},
            responses = {
                    @ApiResponse(responseCode = "201",
                            description = "Catégorie supprimée avec succès"),
                    @ApiResponse(responseCode = "400",
                            description = "Catégorie non existante")
            }
    )
    public ResponseEntity<Void> deleteCategory(@PathVariable UUID id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
