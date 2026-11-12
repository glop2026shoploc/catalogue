package com.glop.catalogue.domain.port.in;

import com.glop.catalogue.domain.model.shop.CategoryModel;

import java.util.List;

public interface CategoryPortIn {
    List<CategoryModel> listCategories();
}
