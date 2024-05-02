package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.PublicPhotoAndVideoResponse;
import peakspace.dto.response.PublicPostResponse;
import peakspace.dto.response.PublicProfileResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.enums.Choise;
import peakspace.service.PublicProfileService;
import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicAPI {

    private final PublicProfileService publicService;

    @Secured("USER")
    @Operation(summary = " Создание паблик канал !")
    @PostMapping("/addPublic")
    public SimpleResponse addPublic(@RequestBody PublicRequest publicRequest){
        return publicService.save(publicRequest);
    }

    @Secured("USER")
    @Operation(summary = " Изменение паблика !")
    @PutMapping("/editPublic/{publicId}")
    public SimpleResponse editPublic(@PathVariable Long publicId,@RequestBody PublicRequest publicRequest){
        return publicService.edit(publicId,publicRequest);
    }

    @Secured({"USER"})
    @Operation(summary = " Удаление паблик !")
    @DeleteMapping("/delete/{publicId}")
    public SimpleResponse delete(@PathVariable Long publicId){
        return publicService.delete(publicId);
    }

    @Secured({"USER"})
    @Operation(summary = " Мой паблик !")
    @GetMapping("/myPublic/{publicId}/{userId}")
    public PublicProfileResponse findPublic(@PathVariable Long publicId, @PathVariable Long userId){
        return publicService.findPublicProfile(publicId,userId);
    }

    @Secured("USER")
    @Operation(summary = " Публикации моего паблика по выбором фото или видео !")
    @GetMapping("/publicPhotoAndVideo/{publicId}/{userId}")
    public List<PublicPhotoAndVideoResponse> getMyPublic(@RequestParam Choise choise,@PathVariable Long publicId,@PathVariable Long userId){
        return publicService.getPublicPost(choise,publicId,userId);
    }

    @Secured("USER")
    @Operation(summary = " Страница одного поста полный вид findByPostId")
    @GetMapping("/findPostPublic/{postId}")
    public PublicPostResponse getByPost(@PathVariable Long postId){
        return publicService.findPostPublic(postId);
    }

    @Secured("USER")
    @Operation (summary = " Удаление пользователья в паблике ")
    @PutMapping("/removeUser/{friendId}")
    public SimpleResponse removeUserFromPublic(@PathVariable Long friendId){
        return publicService.removeUser(friendId);
    }


}
