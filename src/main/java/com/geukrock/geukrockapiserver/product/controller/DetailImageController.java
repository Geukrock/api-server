package com.geukrock.geukrockapiserver.product.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.geukrock.geukrockapiserver.product.dto.DetailImageReqDto;
import com.geukrock.geukrockapiserver.product.service.DetailImageService;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class DetailImageController {
    private final DetailImageService detailImageService;

    // 상세 이미지 조회
    @GetMapping("/{id}/detail-images")
    public ResponseEntity<List<DetailImageReqDto>> getDetailImages(@PathVariable("id") Long id) {
        List<DetailImageReqDto> detailImages = detailImageService.getDetailImages(id);
        return ResponseEntity.ok(detailImages);
    }

    // 상세 이미지 추가
    @PostMapping(value = "/{id}/detail-images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addDetailImages(
            @PathVariable("id") Long id,
            @RequestPart(value = "images", required = true) List<MultipartFile> multipartFiles) {
        detailImageService.addDetailImages(id, multipartFiles);
        return ResponseEntity.ok().build();
    }

    // 상세 이미지 삭제
    @DeleteMapping("/detail-images/{id}")
    public ResponseEntity<Void> deleteDetailImages(@PathVariable("id") Long id) {
        detailImageService.deleteDetailImage(id);
        return ResponseEntity.ok().build();
    }

    // DetailImage Sequence 수정
    @PutMapping("/detail-images/{detailImageId}/sequence")
    public ResponseEntity<Void> updateDetailImageSequence(
            @PathVariable("detailImageId") Long detailImageId,
            @RequestBody Map<String, Integer> sequence){

        detailImageService.updateDetailImageSequence(detailImageId, sequence.get("sequence"));
        return ResponseEntity.ok().build();
    }
}
