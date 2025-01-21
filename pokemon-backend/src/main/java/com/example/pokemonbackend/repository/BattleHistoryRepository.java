package com.example.pokemonbackend.repository;

import com.example.pokemonbackend.model.BattleHistory;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BattleHistoryRepository extends JpaRepository<BattleHistory, Long> {

    @Query("SELECT b FROM BattleHistory b ORDER BY b.battleTime DESC")
    List<BattleHistory> findTopHistories(Pageable pageable);

}
