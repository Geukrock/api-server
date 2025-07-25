package com.geukrock.geukrockapiserver.product.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.geukrock.geukrockapiserver.product.entity.DetailImage;

import jakarta.transaction.Transactional;

public interface DetailImageRepository extends JpaRepository<DetailImage, Long> {
    @Query("select d from DetailImage d where d.product.id = :productId order by d.sequence ASC")
    List<DetailImage> findByProductId(@Param("productId") Long productId);

    @Transactional
    @Modifying
    @Query("delete from DetailImage d where d.product.id = :productId")
    void deleteByProductId(@Param("productId") Long productId);

    @Query("select max(d.sequence) from DetailImage d where d.product.id =:productId")
    Optional<Integer> findMaxSequence(@Param("productId") Long productId);

    @Query("select d from DetailImage d where d.product.id = :productId and d.sequence = :sequence")
    boolean existsDetailImagesequence(Long productId, Integer sequence);


    @Modifying
    @Query("UPDATE DetailImage d SET d.sequence = d.sequence + 1 WHERE d.product.id = :productId AND d.sequence >= :start AND d.sequence < :end")
    void shiftSequenceUp(@Param("productId") Long productId,
            @Param("start") Integer start,
            @Param("end") Integer end);

    @Modifying
    @Query("UPDATE DetailImage d SET d.sequence = d.sequence - 1 WHERE d.product.id = :productId AND d.sequence > :start AND d.sequence <= :end")
    void shiftSequenceDown(@Param("productId") Long productId,
            @Param("start") Integer start,
            @Param("end") Integer end);
}