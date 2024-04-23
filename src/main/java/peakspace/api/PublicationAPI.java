package peakspace.api;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.response.PublicationResponse;
import peakspace.dto.response.PublicationWithYouResponse;
import peakspace.service.PublicationService;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PublicationAPI {

    private final PublicationService publicationService;

    @GetMapping("/photoWithMe/{foundUserId}")
    @ApiOperation(value = " Фото с вами !")
    public List<PublicationWithYouResponse> withPhoto(@PathVariable Long foundUserId) {
        return publicationService.withPhoto(foundUserId);
    }

    @GetMapping("/public/{friendId}")
    @ApiOperation(value = " Все публикации друга ! ")
    public List<PublicationResponse> findAllPublic(@PathVariable Long friendId) {
        return publicationService.findAllPublic(friendId);
    }


}
