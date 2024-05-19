package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.PublicPhotoAndVideoResponse;
import peakspace.dto.response.PublicPostResponse;
import peakspace.dto.response.PublicProfileResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.enums.Choise;
import peakspace.service.PublicProfileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-profiles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicProfileAPI {

    private final PublicProfileService publicService;

    @Secured("USER")
    @Operation(summary = " Создание паблик канал !")
    @PostMapping
    public SimpleResponse addPublic(@RequestBody PublicRequest publicRequest) {
        return publicService.save(publicRequest);
    }

    @Secured("USER")
    @Operation(summary = " Изменение паблика !")
    @PutMapping
    public SimpleResponse editPublic(@RequestBody PublicRequest publicRequest) {
        return publicService.edit(publicRequest);
    }

    @Secured({"USER"})
    @Operation(summary = " Удаление паблик !")
    @DeleteMapping("/{publicId}")
    public SimpleResponse delete(@PathVariable Long publicId) {
        return publicService.delete(publicId);
    }

    @Secured({"USER"})
    @Operation(summary = " Мой паблик !")
    @GetMapping("/my-public/{publicId}/{userId}")
    public PublicProfileResponse findPublic(@PathVariable Long publicId, @PathVariable Long userId) {
        return publicService.findPublicProfile(publicId, userId);
    }

    @Secured("USER")
    @Operation(summary = " Публикации моего паблика по выбором фото или видео !")
    @GetMapping("/public-photo-and-video/{publicId}/{userId}")
    public List<PublicPhotoAndVideoResponse> getMyPublic(@RequestParam Choise choise, @PathVariable Long publicId, @PathVariable Long userId) {
        return publicService.getPublicPost(choise, publicId, userId);
    }

    @Secured("USER")
    @Operation(summary = " Страница одного поста полный вид findByPostId")
    @GetMapping("/find/{postId}")
    public PublicPostResponse getByPost(@PathVariable Long postId) {
        return publicService.findPostPublic(postId);
    }

    @Secured("USER")
    @Operation(summary = " Удаление пользователя в паблике ")
    @PutMapping("/tag/{friendId}")
    public SimpleResponse removeUserFromPublic(@PathVariable Long friendId) {
        return publicService.removeUser(friendId);
    }

    @Secured("USER")
    @Operation(summary = " Для кнопка присоединится на паблик канал ! ")
    @PutMapping("/send-join-public/{publicId}")
    public SimpleResponse sendJoinPublic(@PathVariable Long publicId) {
        return publicService.sendPublic(publicId);
    }

    @Secured("USER")
    @Operation(summary = " Для удаление фото на паблика от имени Admin (владелец паблика) !")
    @PutMapping("/post/{postId}")
    public SimpleResponse removePostAdmin(@PathVariable Long postId) {
        return publicService.removePost(postId);
    }

    @Secured("USER")
    @Operation(summary = " Войти на Мой паблик или Профиль через ссылку !")
    @GetMapping()
    public PublicProfileResponse forwarding(@RequestParam String publicName){
        return publicService.forwardingMyPublic(publicName);
    }

}