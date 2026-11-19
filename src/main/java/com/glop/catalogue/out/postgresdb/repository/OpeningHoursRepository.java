package com.glop.catalogue.out.postgresdb.repository;

import com.glop.catalogue.out.postgresdb.entity.OpeningHoursEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface OpeningHoursRepository extends JpaRepository<OpeningHoursEntity, UUID> {
    List<OpeningHoursEntity> findByShopShopIdOrderByDayOfWeekAscOpenTimeAsc(UUID shopId);
}
