package com.example.librarymanagement.service.impl;

import com.example.librarymanagement.constant.EventConstants;
import com.example.librarymanagement.constant.SuccessMessage;
import com.example.librarymanagement.domain.dto.common.CommonResponseDto;
import com.example.librarymanagement.domain.dto.request.other.SlideRequestDto;
import com.example.librarymanagement.domain.dto.response.other.SlideResponseDto;
import com.example.librarymanagement.service.LogService;
import com.example.librarymanagement.service.SlideService;
import com.example.librarymanagement.util.UploadFileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class SlideServiceImpl implements SlideService {

    private static final String SLIDE_DATA_FILE_PATH = "data/slide_config.txt";
    private static final String TAG = "Thiết lập hệ thống";

    private final LogService logService;

    private final MessageSource messageSource;

    private final UploadFileUtil uploadFileUtil;

    public List<SlideResponseDto> readFromFile(String filePath) {
        List<SlideResponseDto> slides = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filePath));

            for (String line : lines) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    String id = parts[0];
                    String title = parts[1];
                    String description = parts[2];
                    String imageUrl = parts[3];
                    Boolean activeFlag = Boolean.valueOf(parts[4]);

                    slides.add(new SlideResponseDto(id, title, description, imageUrl, activeFlag));
                }
            }
        } catch (IOException e) {
            log.error("Error reading file: {}", filePath, e);
        }
        return slides;
    }

    public void writeToFile(String filePath, List<SlideResponseDto> slides) {
        List<String> lines = new ArrayList<>();
        for (SlideResponseDto slide : slides) {
            String slideData = slide.getId() + ","
                    + slide.getTitle() + ","
                    + slide.getDescription() + ","
                    + slide.getImageUrl() + ","
                    + slide.getActiveFlag();
            lines.add(slideData);
        }

        try {
            Files.write(Paths.get(filePath), lines);
        } catch (IOException e) {
            log.error("Error writing to file: {}", filePath, e);
        }
    }

    private SlideResponseDto findById(String id, List<SlideResponseDto> slides) {
        return slides.stream().filter(dto -> dto.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public CommonResponseDto addSlide(SlideRequestDto slideRequestDto, MultipartFile image, String userId) {
        uploadFileUtil.checkImageIsValid(image);

        List<SlideResponseDto> slides = readFromFile(SLIDE_DATA_FILE_PATH);

        SlideResponseDto newSlide = new SlideResponseDto();
        newSlide.setId(String.valueOf(slides.size()));
        newSlide.setTitle(slideRequestDto.getTitle());
        newSlide.setDescription(slideRequestDto.getDescription());
        newSlide.setActiveFlag(slideRequestDto.isActiveFlag());

        String imageUrl = uploadFileUtil.uploadFile(image);
        newSlide.setImageUrl(imageUrl);
        slides.add(newSlide);

        writeToFile(SLIDE_DATA_FILE_PATH,slides);

        logService.createLog(TAG,  EventConstants.ADD, "Thêm mới slide", userId);

        String message = messageSource.getMessage(SuccessMessage.CREATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, newSlide);
    }

    @Override
    public CommonResponseDto updateSlide(String slideId, SlideRequestDto slideRequest, MultipartFile image, String userId) {
        uploadFileUtil.checkImageIsValid(image);

        List<SlideResponseDto> slides = readFromFile(SLIDE_DATA_FILE_PATH);
        SlideResponseDto slideToUpdate = findById(slideId, slides);
        if (slideToUpdate == null) {
            return new CommonResponseDto("Slide with ID " + slideId + " not found.");
        }

        slideToUpdate.setTitle(slideRequest.getTitle());
        slideToUpdate.setDescription(slideRequest.getDescription());
        slideToUpdate.setActiveFlag(slideRequest.isActiveFlag());

        if (image != null && !image.isEmpty()) {
            String newImageUrl = uploadFileUtil.uploadFile(image);
            uploadFileUtil.destroyFileWithUrl(slideToUpdate.getImageUrl());
            slideToUpdate.setImageUrl(newImageUrl);
        }

        writeToFile(SLIDE_DATA_FILE_PATH, slides);

        logService.createLog(TAG, EventConstants.EDIT, "Sửa slide Id: " + slideId, userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, slideToUpdate);
    }

    @Override
    public CommonResponseDto deleteSlide(String slideId, String userId) {
        List<SlideResponseDto> slides = readFromFile(SLIDE_DATA_FILE_PATH);
        SlideResponseDto slideToDelete = findById(slideId, slides);
        if (slideToDelete == null) {
            return new CommonResponseDto("Slide with ID " + slideId + " not found.");
        }

        uploadFileUtil.destroyFileWithUrl(slideToDelete.getImageUrl());

        slides.remove(slideToDelete);

        writeToFile(SLIDE_DATA_FILE_PATH, slides);

        logService.createLog(TAG, EventConstants.DELETE, "Sửa slide Id: " + slideId, userId);

        String message = messageSource.getMessage(SuccessMessage.DELETE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message);
    }

    @Override
    public List<SlideResponseDto> getAllSlides(Boolean activeFlag) {
        return readFromFile(SLIDE_DATA_FILE_PATH).stream()
                .filter(dto -> activeFlag == null || dto.getActiveFlag().equals(activeFlag))
                .toList();
    }

    @Override
    public SlideResponseDto getSlideById(String id) {
        List<SlideResponseDto> slides = readFromFile(SLIDE_DATA_FILE_PATH);
        return findById(id, slides);
    }

    @Override
    public CommonResponseDto toggleActiveStatus(String id, String userId) {
        List<SlideResponseDto> slides = readFromFile(SLIDE_DATA_FILE_PATH);
        SlideResponseDto slide = findById(id, slides);

        slide.setActiveFlag(!slide.getActiveFlag());

        writeToFile(SLIDE_DATA_FILE_PATH, slides);

        logService.createLog(TAG, EventConstants.EDIT, "Thay đổi trạng thái slide Id: " + slide.getId() + ", trạng thái: " + slide.getActiveFlag(), userId);

        String message = messageSource.getMessage(SuccessMessage.UPDATE, null, LocaleContextHolder.getLocale());
        return new CommonResponseDto(message, slide.getActiveFlag());
    }
}
