package com.joolove.core.service;

import com.joolove.core.domain.product.Category;
import com.joolove.core.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public Category findOrCreateCategoryByName(String categoryName) {
        return categoryRepository.findOneByCategoryName(categoryName)
                .orElseGet(() -> {
            Category category = Category.builder()
                    .categoryName(categoryName)
                    .build();
            return categoryRepository.save(category);
        });
    }

    @Transactional
    public Category addCategory(Category category) {
        return categoryRepository.save(category);
    }

    public boolean existsCategoryByName(String categoryName) {
        return categoryRepository.findOneByCategoryName(categoryName).isPresent();
    }

    public Category findCategoryByName(String categoryName) {
        return categoryRepository.findOneByCategoryName(categoryName)
                .orElseThrow(() -> new IllegalArgumentException("해당 카테고리가 없습니다."));
    }
}
