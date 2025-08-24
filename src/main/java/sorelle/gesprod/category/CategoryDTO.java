package sorelle.gesprod.category;

import jakarta.validation.constraints.Size;
import lombok.*;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryDTO {
    private UUID id;

    @Size(min = 3, max = 48, message = "{Size.category.nomCategory}")
    private String nomCategory;
}
