package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.PublicPhotoAndVideoResponse;
import peakspace.dto.response.CommentResponse;
import peakspace.dto.response.LinkResponse;
import peakspace.dto.response.PublicPostResponse;
import peakspace.dto.response.PublicProfileResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.entities.Comment;
import peakspace.entities.PablicProfile;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.entities.Link_Publication;
import peakspace.enums.Choise;
import peakspace.enums.Role;
import peakspace.exception.BadRequestException;
import peakspace.exception.NotFoundException;
import peakspace.repository.CommentRepository;
import peakspace.repository.PublicProfileRepository;
import peakspace.repository.PublicationRepository;
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
    private final PublicationRepository publicationRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public SimpleResponse save(PublicRequest publicRequest) {
        User currentUser = getCurrentUser();
        if (currentUser.getPablicProfiles() != null) {
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
    public SimpleResponse edit(PublicRequest publicRequest) {
        PablicProfile editPublic = publicProfileRepository.findById(getCurrentUser().getPablicProfiles().getId()).orElseThrow(() -> new NotFoundException("У вас нет паблик !" + getCurrentUser().getUsername()));
        editPublic.setCover(publicRequest.getCover());
        editPublic.setAvatar(publicRequest.getAvatar());
        editPublic.setPablicName(publicRequest.getPablicName());
        editPublic.setDescriptionPublic(publicRequest.getDescriptionPublic());
        editPublic.setTematica(publicRequest.getTematica());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Удачно изменилась !")
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse delete(Long publicId) {
        publicProfileRepository.findById(publicId).orElseThrow(() -> new NotFoundException(" Нет такой паблик !" + publicId));
        publicProfileRepository.deleteUsers(publicId);
        publicProfileRepository.deletePablicById(publicId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Удачно удалено !")
                .build();
    }

    @Override
    public PublicProfileResponse findPublicProfile(Long publicId, Long userId) {
        PablicProfile publicProfile = publicProfileRepository.findById(publicId).orElseThrow(() -> new NotFoundException(" Нет такой паблик !"));
        userRepository.findByIds(userId);
        return PublicProfileResponse.builder()
                .publicId(publicProfile.getId())
                .cover(publicProfile.getCover())
                .avatar(publicProfile.getAvatar())
                .pablicName(publicProfile.getPablicName())
                .tematica(publicProfile.getTematica())
                .countFollower(publicProfile.getUsers().size())
                .build();
    }

    @Override
    public List<PublicPhotoAndVideoResponse> getPublicPost(Choise choise, Long publicId, Long userId) {
        PablicProfile publicProfile = publicProfileRepository.findById(publicId).orElseThrow(() -> new NotFoundException(" Нет такой паблик !"));
        userRepository.findByIds(userId);

        Map<Long, String> publics = new HashMap<>();

        if (publicProfile != null) {
            switch (choise) {
                case Photos:
                case Videos:
                    publics = publicProfile.getPublications().stream()
                            .filter(publication -> {
                                List<String> links = publication.getLinkPublications().stream()
                                        .map(Link_Publication::getLink)
                                        .toList();
                                return (choise == Choise.Photos) && links.stream()
                                        .anyMatch(link -> link.endsWith(".jpg") || link.endsWith(".img") || link.endsWith(".raw"))
                                        || (choise == Choise.Videos) && links.stream()
                                        .anyMatch(link -> link.endsWith(".mp4") || link.endsWith(".webm") || link.endsWith(".ogg"));
                            })
                            .collect(Collectors.toMap(Publication::getId, publication -> publication.getLinkPublications().getFirst().getLink()));
                    break;
                default:
                    break;
            }
        }
        return List.of(PublicPhotoAndVideoResponse.builder()
                .publicationsPublic(publics)
                .build());
    }



    @Override
    public PublicPostResponse findPostPublic(Long postId) {
        Publication publication = publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException(" Нет такой публикации !"));
        List<CommentResponse> commentForResponse = commentRepository.getCommentForResponse(publication.getId());
        commentForResponse.reversed();

        List<LinkResponse> links = publication.getLinkPublications().stream()
                .map(link -> new LinkResponse(link.getId(), link.getLink()))
                .collect(Collectors.toList());
        if (publication.getPablicProfile() == null) {
            throw new BadRequestException(" Нет такой пост в паблике паблик !");
        }
        return new PublicPostResponse(
                publication.getId(),
                publication.getOwner().getId(),
                publication.getOwner().getProfile().getAvatar(),
                publication.getOwner().getThisUserName(),
                publication.getPablicProfile().getTematica(),
                publication.getLikes().size(),
                links,
                commentForResponse
        );
    }

    @Override
    @Transactional
    public SimpleResponse removeUser(Long friendId) {
        User currentUser = getCurrentUser();
        User friendUser = userRepository.findByIds(friendId);

        PablicProfile publicProfile = currentUser.getPablicProfiles();
        if (publicProfile == null) {
            throw new NotFoundException(" Паблик текущего пользователя не найден!");
        }

        List<User> users = publicProfile.getUsers();
        if (users.contains(friendUser)) {
            users.remove(friendUser);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Пользователь успешно удален из паблика !")
                    .build();
        } else {
            throw new NotFoundException("Пользователь не является членом публичного профиля!");
        }
    }

    @Override
    @Transactional
    public SimpleResponse sendPublic(Long publicId) {
        PablicProfile publicProfile = publicProfileRepository.findById(publicId)
                .orElseThrow(() -> new NotFoundException("Паблик не найден!"));

        User currentUser = getCurrentUser();
        String message = null;
        List<User> users = publicProfile.getUsers();
        if (users.contains(currentUser)) {
            users.remove(currentUser);
            message = "Пользователь успешно отписано!";
        } else {
            users.add(currentUser);
            message = "Пользователь успешно присоединились !";
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse removePost(Long postId) {
        User currentUser = getCurrentUser();
        List<Publication> publications = currentUser.getPablicProfiles().getPublications();

        boolean removed = publications.removeIf(publication -> publication.getId().equals(postId));
        publicationRepository.deleteComNotifications(postId);
        publicationRepository.deleteCom(postId);
        publicationRepository.deleteByIds(postId);
        if (removed) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Пост успешно удален!")
                    .build();
        } else {
            throw new NotFoundException("Пост с id " + postId + " не найден!");
        }
    }

    @Override
    public SimpleResponse removeComment(Long commentId) {
        User currentUser = getCurrentUser();
        List<Publication> publications = currentUser.getPablicProfiles().getPublications();

        for (Publication publication : publications) {
            List<Comment> comments = publication.getComments();
            if (comments.removeIf(c -> c.getId().equals(commentId))) {
                commentRepository.deleteById(commentId);
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Комментарий успешно удален!")
                        .build();
            }
        }
        throw new NotFoundException("Комментарий с id " + commentId + " не найден!");
    }

    @Override
    public PublicProfileResponse forwardingMyPublic(String publicName) {
        PablicProfile publicProfile = getCurrentUser().getPablicProfiles();
        if (!publicProfile.getPablicName().equals(publicName)){
            throw new BadRequestException(" Нет такой у вас паблик !");
        }
        return PublicProfileResponse.builder()
                .publicId(publicProfile.getId())
                .cover(publicProfile.getCover())
                .avatar(publicProfile.getAvatar())
                .pablicName(publicProfile.getPablicName())
                .tematica(publicProfile.getTematica())
                .countFollower(publicProfile.getUsers().size())
                .build();
    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }
}
