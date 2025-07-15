package com.geukrock.geukrockapiserver.product.service;

import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.geukrock.geukrockapiserver.product.dto.DetailImageReqDto;
import com.geukrock.geukrockapiserver.product.entity.DetailImage;
import com.geukrock.geukrockapiserver.product.entity.Product;
import com.geukrock.geukrockapiserver.product.repository.DetailImageRepository;
import com.geukrock.geukrockapiserver.product.repository.ProductRepository;
import com.geukrock.geukrockapiserver.util.ImageStore;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
@Transactional
public class DetailImageService {
    private final ImageStore imageStore;
    private final DetailImageRepository detailImageRepository;
    private final ProductRepository productRepository;

    public void addDetailImages(Long productId, List<MultipartFile> detailImages) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("해당 제품이 존재하지 않습니다. " + productId));

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

    @Transactional(readOnly = false)
    public List<DetailImageReqDto> getDetailImages(Long productId) {
        List<DetailImage> detailImages = detailImageRepository.findByProductId(productId);

        return detailImages.stream().map(DetailImageReqDto::new).toList();
    }

    public void deleteDetailImage(Long id) {
        DetailImage detailImage = detailImageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상세 이미지가 없습니다: " + id));
        detailImageRepository.delete(detailImage);

        imageStore.delete(detailImage.getDetailImageUrl());
    }

    public void updateDetailImageSequence(Long detailImageId, Integer newSequence) {
        DetailImage targetImage = detailImageRepository.findById(detailImageId)
                .orElseThrow(() -> new IllegalArgumentException("상세 이미지가 없습니다: " + detailImageId));

        Integer oldSequence = targetImage.getSequence();
        Long productId = targetImage.getProduct().getId();
        // 순서가 같다면 변경하지 않음
        if (oldSequence.equals(newSequence))
            return;

        // 임시로 겹치지 않는 값 지정 (9999 등)
        targetImage.updateSequence(9999);
        detailImageRepository.save(targetImage);

        if (newSequence < oldSequence) {
            detailImageRepository.shiftSequenceUp(productId, newSequence, oldSequence);
        } else {
            detailImageRepository.shiftSequenceDown(productId, oldSequence, newSequence);
        }

        targetImage.updateSequence(newSequence);
        detailImageRepository.save(targetImage);
    }
}
