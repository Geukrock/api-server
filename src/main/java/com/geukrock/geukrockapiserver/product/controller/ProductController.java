package com.geukrock.geukrockapiserver.product.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geukrock.geukrockapiserver.product.dto.ProductDetailResDto;
import com.geukrock.geukrockapiserver.product.dto.ProductReqDto;
import com.geukrock.geukrockapiserver.product.dto.ProductResDto;
import com.geukrock.geukrockapiserver.product.entity.Product;
import com.geukrock.geukrockapiserver.product.service.ProductService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    // 상품 조회
    @GetMapping()
    public ResponseEntity<List<ProductResDto>> getProducts() {
        List<ProductResDto> products = productService.getProducts();
        return ResponseEntity.ok(products);
    }   

    // 상품 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResDto> getProduct(@PathVariable("id") Long id) {
        ProductResDto product = productService.getProduct(id);
        return ResponseEntity.ok(product);
    }
    
    // 상품 등록
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Product> addProduct(
            @ModelAttribute("product") ProductReqDto productAddDto,
            @RequestPart(value = "image", required = false) MultipartFile image) {

        Product product = productService.addProduct(productAddDto, image);
        return ResponseEntity.ok(product);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

    // 상품 상세 단건 조회
    @GetMapping("/{id}/with-detail-images")
    public ResponseEntity<ProductDetailResDto>getProductDetail(@PathVariable("id") Long id) {
        ProductDetailResDto productDetail = productService.getProductDetail(id);
        return ResponseEntity.ok(productDetail);
    }
}
