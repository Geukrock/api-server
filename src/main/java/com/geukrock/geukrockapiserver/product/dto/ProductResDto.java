package com.geukrock.geukrockapiserver.product.dto;

import java.math.BigDecimal;

import com.geukrock.geukrockapiserver.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ProductResDto {
    private Long id;
    private String name;
    private BigDecimal price;
    private String thumbnailUrl;
    private Boolean selling;

    public ProductResDto(Product product){
        this.id = product.getId();
        this.name = product.getName();
        this.price = product.getPrice();
        this.thumbnailUrl = product.getThumbnailUrl();
        this.selling = product.getSelling();
    }
}
