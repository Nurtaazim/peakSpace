package peakspace.api;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.*;
import peakspace.enums.Choise;
import peakspace.repository.jdbsTamplate.GetAllPublics;
import peakspace.service.PublicProfileService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/public-profiles")
@CrossOrigin(origins = "*", maxAge = 3600)
public class PublicProfileAPI {

    private final PublicProfileService publicService;
    private final GetAllPublics getAllPublics;

    @Secured("USER")
    @Operation(summary = " Создание паблик канал !")
    @PostMapping
    public PublicProfileResponse addPublic(@RequestBody PublicRequest publicRequest) {
        return publicService.save(publicRequest);
    }

    @Secured("USER")
    @Operation(summary = " Изменение паблика !")
    @PatchMapping
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
    @Operation(summary = " Войти на Мой паблик от страницы профилья!")
    @GetMapping("/public/{publicName}")
    public PublicProfileResponse forwardingPublic(@PathVariable String publicName) {
        return publicService.forwardingMyPublic(publicName);
    }

    @Secured("USER")
    @Operation(summary = " Войти на профиль от страницы паблика !")
    @GetMapping("/profile/{userName}")
    public ProfileFriendsResponse forwardingProfile(@PathVariable String userName) {
        return publicService.forwardingMyProfile(userName);
    }

    @Secured("USER")
    @Operation(summary = " Профиль участника !")
    @GetMapping("/findUser/{postId}")
    public ProfileFriendsResponse findUser(@PathVariable Long postId) {
        return publicService.findUserByPostId(postId);
    }

    @Secured("USER")
    @Operation(summary = "Get all PublicProfiles!")
    @GetMapping("/profiles/{userId}")
    public List<GetAllPublicProfileResponse> getAllPublicProfiles(@PathVariable Long userId) {
        return getAllPublics.getAllPublics(userId);
    }

    @GetMapping()
    @Secured("USER")
    @Operation(summary = "Сообщества для рекомендации")
    public List<PublicProfileResponse> getRandomCommunities() {
        return publicService.getRandomCommunities();
    }

    @GetMapping("/myCommunities")
    @Secured("USER")
    @Operation(summary = "Сообщества в которые вступил данный пользователь")
    public List<PublicProfileResponse> getMyCommunities() {
        return publicService.getMyCommunities();
    }

    @GetMapping("/myCommunity")
    @Secured("USER")
    @Operation(summary = "Войти в сообщество данного пользователя")
    public PublicProfileResponse getMyCommunity() {
        return publicService.getMyCommunity();
    }

    @GetMapping("/{communityId}")
    @Secured("USER")
    @Operation(summary = "Войти в сообщество", description = "id сообщество")
    PublicProfileResponse getCommunityById(@PathVariable Long communityId) {
        return publicService.getCommunityById(communityId);
    }

    @PostMapping("/{communityId}")
    @Secured("USER")
    @Operation(summary = "Добавить публикацию в сообщество", description = "айди сообщества в которую хотите добавить")
    SimpleResponse addPublicationToCommunity(@PathVariable Long communityId,
                                             @RequestBody PostRequest postRequest) {
        return publicService.addPublicationToCommunityById(communityId, postRequest);
    }

    @GetMapping("/publications/{communityId}")
    @Secured("USER")
    @Operation(summary = "Получить всех публикаций сообщества", description = "id сообщество")
    List<ShortPublicationResponse> getAllPublicationByCommunityId(@PathVariable Long communityId) {
        return publicService.getAllPublicationByCommunityId(communityId);
    }


}