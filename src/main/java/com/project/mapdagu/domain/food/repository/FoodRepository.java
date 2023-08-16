package com.project.mapdagu.domain.food.repository;

import com.project.mapdagu.domain.food.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodRepository extends JpaRepository<Food, Long> {

    Optional<Food> findByName(String name);
}
