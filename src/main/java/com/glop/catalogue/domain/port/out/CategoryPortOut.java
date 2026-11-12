package com.glop.catalogue.domain.port.out;

import com.glop.catalogue.domain.model.shop.CategoryModel;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface CategoryPortOut {
    /** Categories triées par nom */
    List<CategoryModel> findAll();

    /** cherche {@code categoryIds} ceux qui existent en base. */
    List<UUID> findExistingIds(Collection<UUID> categoryIds);
}
