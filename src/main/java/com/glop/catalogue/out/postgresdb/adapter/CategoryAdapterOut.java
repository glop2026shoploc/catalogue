package com.glop.catalogue.out.postgresdb.adapter;

import com.glop.catalogue.domain.model.shop.CategoryModel;
import com.glop.catalogue.domain.port.out.CategoryPortOut;
import com.glop.catalogue.out.postgresdb.entity.CategoryEntity;
import com.glop.catalogue.out.postgresdb.mapper.ShopOutMapper;
import com.glop.catalogue.out.postgresdb.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CategoryAdapterOut implements CategoryPortOut {
    private final CategoryRepository categoryRepository;
    private final ShopOutMapper shopOutMapper;

    @Override
    public List<CategoryModel> findAll() {
        return shopOutMapper.toCategoryModels(categoryRepository.findAll(Sort.by("name")));
    }

    @Override
    public List<UUID> findExistingIds(Collection<UUID> categoryIds) {
        return categoryRepository.findAllById(categoryIds).stream().map(CategoryEntity::getCategoryId).toList();
    }
}
