package com.geukrock.geukrockapiserver.product.repository;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.geukrock.geukrockapiserver.product.entity.Product;
import com.geukrock.geukrockapiserver.product.entity.QDetailImage;
import com.geukrock.geukrockapiserver.product.entity.QProduct;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Repository
public class ProductQueryRepository {
    private final JPAQueryFactory queryFactory;

    public Optional<Product> getProductWithDetailImages(Long productId){
        QProduct product = QProduct.product;
        QDetailImage detailImage = QDetailImage.detailImage;

        Product result = queryFactory
        .selectFrom(product)
        .leftJoin(product.detailImageUrls, detailImage).fetchJoin()
        .where(product.id.eq(productId))
        .fetchOne();

        return Optional.ofNullable(result);
    }
}   
