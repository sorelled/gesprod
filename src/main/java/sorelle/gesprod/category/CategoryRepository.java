package sorelle.gesprod.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<Category, UUID> {
// Sera utilisé pour éviter d'enregistrer 2 catégories ayant le même nom
boolean existsByNomCategoryIgnoreCase(String nomCategory);
}

