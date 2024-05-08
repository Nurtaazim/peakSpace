package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import peakspace.entities.Link_Publication;
import peakspace.entities.Link_PublicationRepository;
import com.amazonaws.services.cloudfront.model.EntityNotFoundException;
import com.google.cloud.vision.v1.*;
import com.google.common.collect.ImmutableList;
import peakspace.service.LinkPublicationService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LinkPublicationServiceImpl implements LinkPublicationService {

    private final Link_PublicationRepository link_PublicationRepository;

    @Override
    public List<String> analyzePhoto(Long photoId) {
        List<String> descriptions = new ArrayList<>();

        Link_Publication linkPublication = link_PublicationRepository.findById(photoId).orElse(null);
        if (linkPublication == null) {
            throw new EntityNotFoundException("LinkPublication с ID " + photoId + " не найден");
        }
        try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
            Image image = Image.newBuilder().setSource(ImageSource.newBuilder().setImageUri(linkPublication.getLink())).build();

            // Добавляем текстовый комментарий на русском языке перед анализом изображения
            String russianText = "На этом изображении отвечайте на русском";
            ImageContext imageContext = ImageContext.newBuilder()
                    .addLanguageHints("ru")
                    .addLanguageHints(russianText)
                    .build();
            AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                    .addFeatures(Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION))
                    .addFeatures(Feature.newBuilder().setType(Feature.Type.WEB_DETECTION))
                    .addFeatures(Feature.newBuilder().setType(Feature.Type.OBJECT_LOCALIZATION))
                    .setImage(image)
                    .setImageContext(imageContext)
                    .build();

            BatchAnnotateImagesResponse response = vision.batchAnnotateImages(ImmutableList.of(request));

            for (AnnotateImageResponse res : response.getResponsesList()) {
                for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
                    descriptions.add(annotation.getDescription());
                }
                for (EntityAnnotation annotation : res.getTextAnnotationsList()) {
                    descriptions.add(annotation.getDescription());
                }
                // И так далее...
            }

            return descriptions;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return descriptions;
    }
}
