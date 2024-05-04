package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.PublicPhotoAndVideoResponse;
import peakspace.dto.response.PublicProfileResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.entities.Link_Publication;
import peakspace.entities.PablicProfile;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.enums.Choise;
import peakspace.enums.Role;
import peakspace.exception.NotFoundException;
import peakspace.repository.PublicProfileRepository;
import peakspace.repository.UserRepository;
import peakspace.service.PublicProfileService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicProfileServiceImpl implements PublicProfileService {

    private final PublicProfileRepository publicProfileRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SimpleResponse save(PublicRequest publicRequest) {
        User currentUser = getCurrentUser();
        if (currentUser.getPablicProfiles() != null){
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message(" Профиль уже существует для данного пользователя !")
                    .build();
        }
        PablicProfile newPublic = new PablicProfile();
        newPublic.setCover(publicRequest.getCover());
        newPublic.setAvatar(publicRequest.getAvatar());
        newPublic.setPablicName(publicRequest.getPablicName());
        newPublic.setDescriptionPublic(publicRequest.getDescriptionPublic());
        newPublic.setTematica(publicRequest.getTematica());
        currentUser.setPablicProfiles(newPublic);
        newPublic.setUser(currentUser);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Удачно сохранилась  !")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse edit(Long publicId, PublicRequest publicRequest) {
        PablicProfile editPublic = publicProfileRepository.findById(publicId).orElseThrow(() -> new NotFoundException(" Нет такой паблик !" + publicId));
        editPublic.setCover(publicRequest.getCover());
        editPublic.setAvatar(publicRequest.getAvatar());
        editPublic.setPablicName(publicRequest.getPablicName());
        editPublic.setDescriptionPublic(publicRequest.getDescriptionPublic());
        editPublic.setTematica(publicRequest.getTematica());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Удачно изменилась  !")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse delete(Long publicId) {
        publicProfileRepository.findById(publicId).orElseThrow(() -> new NotFoundException(" Нет такой паблик !" + publicId));
        publicProfileRepository.deleteUsers(publicId);
        publicProfileRepository.deleteById(publicId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Удачно удалено !")
                .build();
    }

    @Override
    public PublicProfileResponse findPublicProfile() {
        User currentUser = getCurrentUser();
        if (currentUser.getPablicProfiles() == null) {
            throw new NotFoundException(" Not found public !");
        }
        return PublicProfileResponse.builder()
                .publicId(currentUser.getPablicProfiles().getId())
                .cover(currentUser.getPablicProfiles().getCover())
                .avatar(currentUser.getPablicProfiles().getAvatar())
                .pablicName(currentUser.getPablicProfiles().getPablicName())
                .descriptionPublic(currentUser.getPablicProfiles().getDescriptionPublic())
                .tematica(currentUser.getPablicProfiles().getTematica())
                .countFollower(currentUser.getPablicProfiles().getUsers().size())
                .build();
    }

    @Override
    public List<PublicPhotoAndVideoResponse> getPublicPost(Choise choise) {
        User currentUser = getCurrentUser();
        Map<Long, String> publics;

        if (currentUser.getPablicProfiles() != null) {
            switch (choise) {
                case Photos:
                case Videos:

                    publics = currentUser.getPablicProfiles().getPublications().stream()
                            .filter(publication -> {
                                List<String> links = publication.getLinkPublications().stream()
                                        .map(Link_Publication::getLink)
                                        .collect(Collectors.toList()).reversed();
                                return (choise == Choise.Photos) && links.stream()
                                        .anyMatch(link -> link.endsWith(".jpg") || link.endsWith(".img") || link.endsWith(".raw"))
                                        || (choise == Choise.Videos) && links.stream()
                                        .anyMatch(link -> link.endsWith(".mp4") || link.endsWith(".webm") || link.endsWith(".ogg"));
                            })
                            .collect(Collectors.toMap(Publication::getId, publication -> publication.getLinkPublications().getFirst().getLink()));
                    break;
                default:
                    publics = new HashMap<>();
            }
        } else {
            publics = new HashMap<>();
        }
        return List.of(PublicPhotoAndVideoResponse.builder()
                .publicationsPublic(publics)
                .build());
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }
}
