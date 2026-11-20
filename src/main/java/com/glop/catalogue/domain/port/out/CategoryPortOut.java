package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.CategoryModel;

import java.util.List;
import java.util.UUID;

public interface CategoryPortOut {
    /** Categories triées par nom */
    List<CategoryModel> findAll();

    boolean existsById(UUID categoryId);
}
