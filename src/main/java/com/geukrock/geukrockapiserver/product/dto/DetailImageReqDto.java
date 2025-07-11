package com.geukrock.geukrockapiserver.product.dto;

import com.geukrock.geukrockapiserver.product.entity.DetailImage;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class DetailImageReqDto {
    private Long id;
    private Long productId;
    private String detailImageUrl;
    private int sequence;

    public DetailImageReqDto(DetailImage detailImage){
        this.id = detailImage.getId();
        this.productId = detailImage.getProduct().getId();
        this.detailImageUrl = detailImage.getDetailImageUrl();
        this.sequence = detailImage.getSequence();
    }
}
