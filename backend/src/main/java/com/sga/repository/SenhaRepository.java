package com.sga.repository;

import com.sga.model.Senha;
import com.sga.model.StatusSenha;
import com.sga.model.TipoAtendimento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SenhaRepository extends JpaRepository<Senha, Long> {

    @Query("SELECT MAX(s.numero) FROM Senha s WHERE s.tipo = :tipo AND s.dataCriacao >= :inicioDia")
    Optional<Integer> findMaxNumeroByTipoEData(@Param("tipo") TipoAtendimento tipo, @Param("inicioDia") LocalDateTime inicioDia);

    List<Senha> findTop10ByStatusOrderByDataChamadaDesc(StatusSenha status);

    List<Senha> findByStatusOrderByDataCriacaoAsc(StatusSenha status);

    @Query("SELECT s FROM Senha s WHERE s.status = :status ORDER BY CASE WHEN s.tipo = :prefTipo THEN 1 ELSE 2 END, s.dataCriacao ASC")
    List<Senha> findProximaSenhaFilaPrioritaria(@Param("status") StatusSenha status, @Param("prefTipo") TipoAtendimento prefTipo);

    long countByStatus(StatusSenha status);

    long countByTipoAndStatus(TipoAtendimento tipo, StatusSenha status);
}
