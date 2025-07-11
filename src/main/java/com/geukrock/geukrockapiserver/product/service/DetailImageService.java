package com.geukrock.geukrockapiserver.product.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.geukrock.geukrockapiserver.product.dto.DetailImageReqDto;
import com.geukrock.geukrockapiserver.product.entity.DetailImage;
import com.geukrock.geukrockapiserver.product.entity.Product;
import com.geukrock.geukrockapiserver.product.repository.DetailImageRepository;
import com.geukrock.geukrockapiserver.product.repository.ProductRepository;
import com.geukrock.geukrockapiserver.util.ImageStore;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class DetailImageService {
    private final ImageStore imageStore;
    private final DetailImageRepository detailImageRepository;
    private final ProductRepository productRepository;

    public void addDetailImages(Long productId, List<MultipartFile> detailImages){
        Product product = productRepository.findById(productId)
        .orElseThrow(()-> new IllegalArgumentException("해당 제품이 존재하지 않습니다. " + productId));
        
        int maxSequence = detailImageRepository.findMaxSequence(productId).orElse(0);
        
        List<DetailImage> entities = new ArrayList<>();
        for (MultipartFile multipartFile : detailImages) {
            String url = imageStore.store(multipartFile);
            DetailImage detailImage = DetailImage.builder()
            .product(product)
            .detailImageUrl(url)
            .sequence(++maxSequence)
            .build();
            entities.add(detailImage);
        }

        detailImageRepository.saveAll(entities);    
    }

    public List<DetailImageReqDto> getDetailImages(Long productId){
        List<DetailImage> detailImages = detailImageRepository.findByProductId(productId);
        
        return detailImages.stream().map(DetailImageReqDto::new).toList();
    }

    public void deleteDetailImage(Long id) {
        DetailImage detailImage = detailImageRepository.findById(id)
        .orElseThrow(()-> new IllegalArgumentException("상세 이미지가 없습니다: " + id));
        detailImageRepository.delete(detailImage);

        imageStore.delete(detailImage.getDetailImageUrl());
    }
}
