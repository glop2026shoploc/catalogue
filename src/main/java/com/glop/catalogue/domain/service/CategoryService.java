package com.glop.catalogue.domain.service;

import com.glop.catalogue.domain.model.shop.CategoryModel;
import com.glop.catalogue.domain.port.in.CategoryPortIn;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService implements CategoryPortIn {
    private final CategoryPortOut categoryPortOut;

    @Override
    public List<CategoryModel> listCategories() {
        return categoryPortOut.findAll();
    }
}
