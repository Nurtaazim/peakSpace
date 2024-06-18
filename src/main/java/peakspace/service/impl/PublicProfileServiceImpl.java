package peakspace.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PublicRequest;
import peakspace.dto.response.*;
import peakspace.entities.*;
import peakspace.entities.Comment;
import peakspace.entities.PablicProfile;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.entities.Link_Publication;
import peakspace.enums.Role;
import peakspace.exception.BadRequestException;
import peakspace.exception.ForbiddenException;
import peakspace.exception.MessagingException;
import peakspace.exception.NotFoundException;
import peakspace.repository.*;
import peakspace.service.PublicProfileService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PublicProfileServiceImpl implements PublicProfileService {

    private final PublicProfileRepository publicProfileRepository;
    private final UserRepository userRepository;
    private final PublicationRepository publicationRepository;
    private final CommentRepository commentRepository;
    private final LinkPublicationRepo linkPublicationRepository;
    private final JdbcTemplate jdbcTemplate;
    private final NotificationRepository notificationRepository;

    @Override
    @Transactional
    public PublicProfileResponse save(PublicRequest publicRequest) {
        User currentUser = getCurrentUser();
        if (currentUser.getCommunity() != null) throw new MessagingException("У вас уже существует сообщество");
        PablicProfile newPublic = new PablicProfile();
        newPublic.setCover(publicRequest.getCover());
        newPublic.setAvatar(publicRequest.getAvatar());
        newPublic.setPablicName(publicRequest.getPablicName());
        newPublic.setDescriptionPublic(publicRequest.getDescriptionPublic());
        newPublic.setTematica(publicRequest.getTematica());
        newPublic.setPublications(new ArrayList<>());
        PablicProfile save = publicProfileRepository.save(newPublic);
        currentUser.setCommunity(newPublic);
        save.setOwner(currentUser);
        return PublicProfileResponse.builder()
                .publicId(save.getId())
                .avatar(newPublic.getAvatar())
                .cover(newPublic.getCover())
                .userName(newPublic.getOwner().getThisUserName())
                .countFollower(0)
                .descriptionPublic(newPublic.getDescriptionPublic())
                .pablicName(newPublic.getPablicName())
                .tematica(newPublic.getTematica())
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse edit(PublicRequest publicRequest) {
        PablicProfile editPublic = publicProfileRepository.findById(getCurrentUser().getCommunity().getId()).orElseThrow(() -> new NotFoundException("У вас нет паблик !" + getCurrentUser().getUsername()));
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

    @Transactional
    @Override
    public SimpleResponse delete() {
        PablicProfile community = getCurrentUser().getCommunity();
        if (community == null) throw new BadRequestException("У вас не существует сообщество");
        for (Publication publication : community.getPublications()) {
            String deleteCommentsNotificationsQuery = "DELETE FROM comments_notifications WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteCommentsNotificationsQuery, publication.getId());

            // Then, delete from notifications
            String deleteNotificationsQuery = "DELETE FROM notifications WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteNotificationsQuery, publication.getId());

            // Удалить связанные записи из comments_likes
            String deleteCommentsLikesSQL = "DELETE FROM comments_likes WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteCommentsLikesSQL, publication.getId());
            String deleteInnerCommentsSql = "DELETE FROM inner_comment WHERE comment_id IN (SELECT id FROM comments WHERE publication_id = ?)";
            jdbcTemplate.update(deleteInnerCommentsSql, publication.getId());

            // Удалить комментарии
            String deleteCommentsSQL = "DELETE FROM comments WHERE publication_id = ?";
            jdbcTemplate.update(deleteCommentsSQL, publication.getId());
            notificationRepository.deleteByPublicationId(publication.getId());
            publicationRepository.delete(publication);
        }
        publicProfileRepository.delete(community);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Удачно удалено !")
                .build();
    }


    @Override
    public PublicPostResponse findPostPublic(Long postId) {
        Publication publication = publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException(" Нет такой публикации !"));
        List<Comment> commentsByPublicationId = commentRepository.getCommentsByPublicationId(postId);
        List<CommentResponse> commentForResponse = new ArrayList<>();
        for (Comment comment : commentsByPublicationId) {
            CommentResponse commentResponse = new CommentResponse(comment.getId(),
                    comment.getUser().getId(),
                    comment.getUser().getProfile().getAvatar(),
                    comment.getUser().getThisUserName(),
                    comment.getMessage(),
                    comment.getLikes().size(),
                    comment.getCreatedAt());
            commentForResponse.add(commentResponse);
        }

        List<LinkResponse> links = publication.getLinkPublications().stream()
                .map(link -> new LinkResponse(link.getId(), link.getLink()))
                .collect(Collectors.toList());
        return new PublicPostResponse(
                publication.getId(),
                publication.getOwner().getId(),
                publication.getOwner().getProfile().getAvatar(),
                publication.getOwner().getThisUserName(),
                publication.getLocation(),
                publication.getDescription(),
                publication.getLikes().size(),
                links,
                commentForResponse,
                publication.isBlockComment()
        );
    }

    @Override
    @Transactional
    public SimpleResponse removeUser(Long friendId) {
        User currentUser = getCurrentUser();
        User friendUser = userRepository.findByIds(friendId);

        PablicProfile publicProfile = currentUser.getCommunity();
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
                .orElseThrow(() -> new NotFoundException("Сообщество с таким id " + publicId + " не найдено"));

        User currentUser = getCurrentUser();
        if (publicProfile.getBlockUsers().contains(currentUser))
            throw new ForbiddenException("Вы были заблокированы владельцом этого сообщества");
        String message = null;
        List<User> users = publicProfile.getUsers();
        if (users.contains(currentUser)) {
            users.remove(currentUser);
            currentUser.getPublicProfilesSize().remove(publicProfile.getId());
            message = "Пользователь успешно отписано!";
        } else {
            users.add(currentUser);
            currentUser.getPublicProfilesSize().add(publicProfile.getId());
            message = "Пользователь успешно присоединились !";
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(message)
                .build();
    }

    @Override
    public SimpleResponse removeComment(Long commentId) {
        User currentUser = getCurrentUser();
        List<Publication> publications = currentUser.getCommunity().getPublications();

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
        getCurrentUser();
        PablicProfile publicProfile = publicProfileRepository.findByPublicName(publicName)
                .orElseThrow(() -> new NotFoundException("Паблик не найден"));

        if (!publicProfile.getPablicName().equals(publicName)) {
            throw new BadRequestException("У пользователя нет такого паблика!");
        }

        return PublicProfileResponse.builder()
                .publicId(publicProfile.getId())
                .cover(publicProfile.getCover())
                .avatar(publicProfile.getAvatar())
                .pablicName(publicProfile.getPablicName())
                .userName(publicProfile.getOwner().getThisUserName())
                .tematica(publicProfile.getTematica())
                .countFollower(publicProfile.getUsers().size())
                .build();
    }

    @Override
    public ProfileFriendsResponse forwardingMyProfile(String userName) {
        getCurrentUser();
        User ownerProfile = userRepository.findByName(userName).orElseThrow(() -> new NotFoundException(" Нет такой пользователь "));
        int sizeFriends = 0;
        for (Chapter chapter : ownerProfile.getChapters()) {
            sizeFriends += chapter.getFriends().size();
        }

        return ProfileFriendsResponse.builder()
                .id(ownerProfile.getId())
                .avatar(ownerProfile.getProfile().getAvatar())
                .cover(ownerProfile.getProfile().getCover())
                .userName(ownerProfile.getThisUserName())
                .aboutYourSelf(ownerProfile.getProfile().getAboutYourSelf())
                .profession(ownerProfile.getProfile().getProfession())
                .friendsSize(sizeFriends)
                .publicationsSize(ownerProfile.getPublicProfilesSize().size())
                .build();
    }

    @Override
    public ProfileFriendsResponse findUserByPostId(Long postId) {
        getCurrentUser();
        Publication publication = publicationRepository.findPostById(postId);
        int sizeFriends = 0;
        for (Chapter chapter : publication.getOwner().getChapters()) {
            sizeFriends += chapter.getFriends().size();
        }
        return ProfileFriendsResponse.builder()
                .id(publication.getOwner().getId())
                .avatar(publication.getOwner().getProfile().getAvatar())
                .cover(publication.getOwner().getProfile().getCover())
                .userName(publication.getOwner().getThisUserName())
                .aboutYourSelf(publication.getOwner().getProfile().getAboutYourSelf())
                .profession(publication.getOwner().getProfile().getProfession())
                .friendsSize(sizeFriends)
                .publicationsSize(publication.getOwner().getPublicProfilesSize().size())
                .build();
    }

    public List<PublicProfileResponse> getRandomCommunities() {
        List<PablicProfile> all = publicProfileRepository.findAll();
        if (all.isEmpty()) {
            return new ArrayList<>();
        }

        List<PublicProfileResponse> randomCommunities = new ArrayList<>();
        Random random = new Random();
        List<Integer> numbers = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            int randomIndex = random.nextInt(0, all.size() - 1);
            if (!numbers.contains(randomIndex)) {
                PablicProfile publicProfile = all.get(randomIndex);
                numbers.add(randomIndex);
                if (!publicProfile.getUsers().contains(getCurrentUser()) &&
                        !publicProfile.getOwner().getId().equals(getCurrentUser().getId())) {

                    randomCommunities.add(new PublicProfileResponse(
                            publicProfile.getId(),
                            publicProfile.getCover(),
                            publicProfile.getAvatar(),
                            publicProfile.getPablicName(),
                            publicProfile.getOwner().getThisUserName(),
                            publicProfile.getDescriptionPublic(),
                            publicProfile.getTematica(),
                            publicProfile.getUsers().size()
                    ));
                }
            }
        }

        return randomCommunities;
    }

    @Override
    public List<PublicProfileResponse> getMyCommunities() {
        List<PablicProfile> all = publicProfileRepository.findAll();
        if (all.isEmpty()) {
            return new ArrayList<>();
        }
        List<PublicProfileResponse> myCommunities = new ArrayList<>();
        for (PablicProfile pablicProfile : all) {
            if (pablicProfile.getUsers().contains(getCurrentUser())) {
                myCommunities.add(new PublicProfileResponse(
                        pablicProfile.getId(),
                        pablicProfile.getCover(),
                        pablicProfile.getAvatar(),
                        pablicProfile.getPablicName(),
                        pablicProfile.getOwner().getThisUserName(),
                        pablicProfile.getDescriptionPublic(),
                        pablicProfile.getTematica(),
                        pablicProfile.getUsers().size()
                ));
            }
        }
        return myCommunities;
    }

    @Override
    public PublicProfileResponse getMyCommunity() {
        PablicProfile community = getCurrentUser().getCommunity();
        if (community == null) return null;
        else return PublicProfileResponse.builder()
                .publicId(community.getId())
                .avatar(community.getAvatar())
                .cover(community.getCover())
                .userName(community.getOwner().getThisUserName())
                .countFollower(community.getUsers().size())
                .descriptionPublic(community.getDescriptionPublic())
                .pablicName(community.getPablicName())
                .tematica(community.getTematica())
                .build();
    }

    @Override
    public PublicProfileResponse getCommunityById(Long communityId) {
        PablicProfile community = publicProfileRepository.findById(communityId).orElseThrow(() -> new NotFoundException("Сообщество с такой id не найдено"));
        if (community.getBlockUsers().contains(getCurrentUser()))
            throw new ForbiddenException("Вы были заблокированы для этого сообщество");
        return PublicProfileResponse.builder()
                .publicId(communityId)
                .cover(community.getCover())
                .avatar(community.getAvatar())
                .tematica(community.getTematica())
                .pablicName(community.getPablicName())
                .descriptionPublic(community.getDescriptionPublic())
                .countFollower(community.getUsers().size())
                .userName(community.getOwner().getThisUserName())
                .build();
    }

    @Override
    @jakarta.transaction.Transactional
    public SimpleResponse addPublicationToCommunityById(Long communityId, PostRequest postRequest) {
        PablicProfile publicProfile = publicProfileRepository.findById(communityId).orElseThrow(() -> new NotFoundException("Сообщество с такой айди не существует!"));
        if (publicProfile.getUsers().contains(getCurrentUser()) || publicProfile.getOwner().equals(getCurrentUser())) {
            Publication publication = new Publication();
            publication.setOwner(getCurrentUser());
            publication.setLikes(new ArrayList<>());
            publication.setBlockComment(postRequest.isBlockComment());
            publication.setComments(new ArrayList<>());
            publication.setDescription(postRequest.getDescription());
            publication.setLocation(postRequest.getLocation());
            List<String> strings = postRequest.getLinks();
            Publication save = publicationRepository.save(publication);
            List<Link_Publication> videosOrPhotosUrl = new ArrayList<>();
            for (String string : strings) {
                Link_Publication linkPublication = new Link_Publication();
                linkPublication.setLink(string);
                linkPublicationRepository.save(linkPublication);
                videosOrPhotosUrl.add(linkPublication);
            }
            save.setLinkPublications(videosOrPhotosUrl);
            publicProfile.getPublications().add(publication);
            save.setPablicProfile(publicProfile);
            return SimpleResponse.builder()
                    .message("Успешно добавлено!")
                    .httpStatus(HttpStatus.OK).build();
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .message("Вы не можете добавить публикацию в сообщество  которой не состоите").build();
    }

    @Override
    public List<ShortPublicationResponse> getAllPublicationByCommunityId(Long communityId) {
        PablicProfile community = publicProfileRepository.findById(communityId).orElseThrow(() -> new NotFoundException("Сообщество с такой айди не существует!"));
        List<ShortPublicationResponse> publications = new ArrayList<>();
        for (Publication publication : community.getPublications()) {
            ShortPublicationResponse shortPublicationResponse = new ShortPublicationResponse(publication.getId(), publication.getOwner().getId(), publication.getLinkPublications().getFirst().getLink());
            publications.add(shortPublicationResponse);
        }
        return publications;
    }

    @Override
    @Transactional
    public SimpleResponse blockUserInCommunity(Long communityId, Long userId) {
        PablicProfile pablicProfile = publicProfileRepository.findById(communityId).orElseThrow(() -> new NotFoundException("Сообщество с таким айди не найдено"));
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь с таким айди не найдено"));
        if (!pablicProfile.getOwner().equals(getCurrentUser()))
            throw new ForbiddenException("У вас нету прав заблокировать других пользователей этого сообщество");
        if (!pablicProfile.getBlockUsers().contains(user)) {
            pablicProfile.getBlockUsers().add(user);
            pablicProfile.getUsers().remove(user);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Пользователь успешно заблокировано")
                    .isBlock(true)
                    .build();
        }
        pablicProfile.getBlockUsers().remove(user);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Пользователь успешно разблокировано")
                .isBlock(false)
                .build();

    }

    @Override
    public List<SearchResponse> getUsersByCommunityId(Long communityId) {
        PablicProfile community = publicProfileRepository.findById(communityId).orElseThrow(() -> new NotFoundException("Сообщество с таким айди не существует!"));
        if (!community.getUsers().contains(getCurrentUser()) || !getCurrentUser().equals(community.getOwner())) throw new ForbiddenException("Так как вы не являетесь участником, вы не можете посмотреть список участников ");
        List<SearchResponse> users = new ArrayList<>();
        if (community.getUsers() == null ) return new ArrayList<>();
        for (User user : community.getUsers()) {
            SearchResponse searchResponse = new SearchResponse(user.getId(), user.getThisUserName(), user.getProfile().getAvatar(), user.getProfile().getAboutYourSelf());
            users.add(searchResponse);
        }
        return users;
    }


    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }
}
