package com.geukrock.geukrockapiserver.product.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.geukrock.geukrockapiserver.product.dto.ProductDetailResDto;
import com.geukrock.geukrockapiserver.product.dto.ProductReqDto;
import com.geukrock.geukrockapiserver.product.dto.ProductResDto;
import com.geukrock.geukrockapiserver.product.entity.Product;
import com.geukrock.geukrockapiserver.product.repository.DetailImageRepository;
import com.geukrock.geukrockapiserver.product.repository.ProductQueryRepository;
import com.geukrock.geukrockapiserver.product.repository.ProductRepository;
import com.geukrock.geukrockapiserver.util.ImageStore;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    private final ImageStore imageStore;
    private final ProductRepository productRepository;
    private final ProductQueryRepository productQueryRepository;
    private final DetailImageRepository detailImageRepository; 

    public List<ProductResDto> getProducts(){
        List<Product> products = productRepository.findAll();
        return products.stream().map(ProductResDto::new).toList();
    }

    public ProductResDto getProduct(Long productId){
        Product product = productRepository.findById(productId)
        .orElseThrow(()-> new IllegalArgumentException("제품이 존재하지 않습니다. " + productId));
        return new ProductResDto(product);
    }

    public ProductDetailResDto getProductDetail(Long productId){
        Product product = productQueryRepository.getProductWithDetailImages(productId)
        .orElseThrow(()-> new IllegalArgumentException("제품이 존재하지 않습니다. " + productId));
        return new ProductDetailResDto(product);
    }

    public Product addProduct(ProductReqDto productAddDto, MultipartFile image){
        String fileName = imageStore.store(image);

        Product product = Product.builder()
        .name(productAddDto.getName())
        .price(productAddDto.getPrice())
        .thumbnailUrl(fileName)
        .selling(productAddDto.getSelling())
        .build();
        return productRepository.save(product);
    }

    public void deleteProduct(Long id){
        Product product = productRepository.findById(id)
        .orElseThrow(()-> new IllegalArgumentException("제품이 없습니다: " + id));
        String thumbnailUrl = product.getThumbnailUrl();

        detailImageRepository.deleteByProductId(id);
        imageStore.delete(thumbnailUrl);
        productRepository.deleteById(id);
    }
}