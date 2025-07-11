package com.geukrock.geukrockapiserver.product.dto;

import java.util.ArrayList;
import java.util.List;

import com.geukrock.geukrockapiserver.product.entity.DetailImage;
import com.geukrock.geukrockapiserver.product.entity.Product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductDetailResDto extends ProductResDto {
    private List<String> detailImageUrls = new ArrayList<>();

    public ProductDetailResDto(Product product){
        super(product);
        detailImageUrls = product.getDetailImageUrls().stream().map(DetailImage::getDetailImageUrl).toList();
    }
}
