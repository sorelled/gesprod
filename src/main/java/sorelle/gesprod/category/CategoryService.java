package sorelle.gesprod.category;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    // Liste des catégories
    public List<CategoryDTO> getAllCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toDTO)
                .collect(Collectors.toList());
    }

    // Récupérer une catégorie par son ID
    public CategoryDTO getCategoryByID(UUID id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Categorie non existante !"));
        return categoryMapper.toDTO(category);
    }

    // Créer une nouvelle catégorie
    public Category createCategory(Category category) {
        // Vérifie si la catégorie existe déjà
        if (categoryRepository.existsByNomCategoryIgnoreCase(category.getNomCategory())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cette catégorie existe déjà !");
        }

        // Crée une nouvelle instance indépendante
        Category newCategory = new Category();
        newCategory.setNomCategory(category.getNomCategory());

        return categoryRepository.save(newCategory);
    }

    // Alternative avec Builder
    public Category createCategoryWithBuilder(CategoryDTO categoryDTO) {
        return categoryRepository.save(
                Category.builder()
                        .nomCategory(categoryDTO.getNomCategory())
                        .build()
        );
    }

    // Mettre à jour une catégorie existante
    public CategoryDTO updateCategory(UUID id, CategoryDTO updatedCategoryDTO) {
        return categoryRepository.findById(id).map(existingCategory -> {
            existingCategory.setNomCategory(updatedCategoryDTO.getNomCategory());
            return categoryMapper.toDTO(categoryRepository.save(existingCategory));
        }).orElseThrow(() -> new IllegalArgumentException("Categorie non existante !"));
    }

    // Supprimer une catégorie existante en BD
    public void deleteCategory(UUID id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Categorie non existante !");
        }
        categoryRepository.deleteById(id);
    }
}

