package peakspace.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import peakspace.dto.request.CommentRequest;
import peakspace.dto.response.CommentResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.dto.response.CommentInnerResponse;
import peakspace.dto.response.InnerCommentResponse;
import peakspace.entities.Comment;
import peakspace.entities.Notification;
import peakspace.entities.Publication;
import peakspace.entities.User;
import peakspace.enums.Role;
import peakspace.exception.NotFoundException;
import peakspace.repository.CommentRepository;
import peakspace.repository.PublicationRepository;
import peakspace.repository.UserRepository;
import peakspace.service.CommentService;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final PublicationRepository publicationRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SimpleResponse save(Long postId, CommentRequest comment) {
        User currentUser = getCurrentUser();
        Publication publication = publicationRepository.findById(postId).orElseThrow(() -> new NotFoundException(" Нет такой публикации " + postId));
        Comment newComment = new Comment();
        newComment.setMessage(comment.getMessage());
        newComment.setPublication(publication);
        newComment.setUser(currentUser);
        Comment save = commentRepository.save(newComment);
        publication.getComments().add(save);

        Notification notification = new Notification();
        notification.setSeen(false);
        notification.setNotificationMessage(" Ответил  на вашу публикации  ! ");
        notification.setCreatedAt(ZonedDateTime.now());
        save.setNotification(notification);
        notification.setComment(save);
        notification.setUserNotification(publication.getOwner());
        notification.setSenderUserId(currentUser.getId());
        publication.getOwner().getNotifications().add(notification);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Успешно доставлено комментарии !")
                .build();
    }

    @Override
    public List<CommentResponse> getAllComment(Long postId) {
        List<Comment> comments = commentRepository.getAllCommentById(postId);
        List<CommentResponse> commentResponses = new ArrayList<>();
        for (Comment comment : comments) {
            commentResponses.add(CommentResponse.builder()
                            .id(comment.getId())
                            .userId(comment.getUser().getId())
                            .avatar(comment.getUser().getProfile().getAvatar())
                            .userName(comment.getUser().getThisUserName())
                            .comment(comment.getMessage())
                            .countLike(comment.getLikes().size())
                            .createdAt(comment.getCreatedAt())
                    .build());
        }
        return commentResponses;
    }

    @Override
    @Transactional
    public SimpleResponse editComment(Long commentId, CommentRequest comment) {
        Comment editComment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException("Нет такой комментарии" + commentId));
        editComment.setMessage(comment.getMessage());
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Комментарий успешно обновлен !")
                .build();
    }

    public void deleteComment(Long commentId) {
        commentRepository.deleteNotification(commentId);
        commentRepository.deleteByIds(commentId);
        SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Комментарий успешно удален !")
                .build();
    }

    @Override
    public CommentInnerResponse findComment(Long commentId) {
        CommentInnerResponse commentInnerResponse = commentRepository.getCommentResponseInner(commentId);
        if (commentInnerResponse != null) {
            commentInnerResponse.setInnerCommentResponseList(commentRepository.getInnerComments(commentId));
        } else {
            throw new NotFoundException(" Нет такой комментарии !");
        }
        return commentInnerResponse;
    }

    @Override
    @Transactional
    public SimpleResponse saveInnerComment(Long commentId, CommentRequest commentRequest) {
        User currentUser = getCurrentUser();
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NotFoundException(" Нет такой комментарии ! "));

        Comment innerComment = new Comment();
        innerComment.setMessage(commentRequest.getMessage());
        innerComment.setPublication(comment.getPublication());
        innerComment.setUser(currentUser);
        commentRepository.save(innerComment);
        comment.getInnerComments().add(innerComment);

        Notification notification = new Notification();
        notification.setSeen(false);
        notification.setNotificationMessage(" Ответила на ваш комментарий ваша сообщение ! ");
        notification.setCreatedAt(ZonedDateTime.now());
        innerComment.setNotification(notification);
        notification.setComment(innerComment);
        notification.setUserNotification(comment.getUser());
        notification.setSenderUserId(currentUser.getId());
        comment.getUser().getNotifications().add(notification);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Успешно доставлено комментарии !")
                .build();
    }

    @Override @Transactional
    public SimpleResponse editInnerComment(Long innerCommentId, CommentRequest commentRequest) {
        User currentUser = getCurrentUser();
        Comment innerComment = commentRepository.findById(innerCommentId).orElseThrow(() -> new NotFoundException(" Нет такой innerComment !"));
        innerComment.setMessage(commentRequest.getMessage());
        innerComment.setUser(currentUser);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Успешно изменено комментарии !")
                .build();
    }

    @Override @Transactional
    public SimpleResponse removeInnerComment(Long innerCommentId) {
        commentRepository.deleteNotificationComment(innerCommentId);
        commentRepository.deleteLikes(innerCommentId);
        commentRepository.deleteInnerComment(innerCommentId);
        deleteComment(innerCommentId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message(" Успешно удалено комментарии !")
                .build();
    }

    @Override
    public InnerCommentResponse findInnerComment(Long innerCommentId) {
        Comment innerComment = commentRepository.findById(innerCommentId)
                .orElseThrow(() -> new NotFoundException("Нет такого inner комментария!"));
        return InnerCommentResponse.builder()
                .innerCommentId(innerComment.getId())
                .userId(innerComment.getUser().getId())
                .avatar(innerComment.getUser().getProfile().getAvatar())
                .userName(innerComment.getUser().getUsername())
                .comment(innerComment.getMessage())
                .countLike(innerComment.getLikes().size())
                .createdAt(innerComment.getCreatedAt())
                .build();
    }

    @Override
    public List<InnerCommentResponse> getAllInnerComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(" Нет такой комментарии ! "));

        List<InnerCommentResponse> innerCommentResponses = new ArrayList<>();
        for (Comment innerComment : comment.getInnerComments()) {
            InnerCommentResponse innerCommentResponse = InnerCommentResponse.builder()
                    .innerCommentId(innerComment.getId())
                    .userId(innerComment.getUser().getId())
                    .avatar(innerComment.getUser().getProfile().getAvatar())
                    .userName(innerComment.getUser().getUsername())
                    .comment(innerComment.getMessage())
                    .countLike(innerComment.getLikes().size())
                    .createdAt(innerComment.getCreatedAt())
                    .build();
            innerCommentResponses.add(innerCommentResponse);
        }
        return innerCommentResponses;

    }

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User current = userRepository.getByEmail(email);
        if (current.getRole().equals(Role.USER))
            return current;
        else throw new AccessDeniedException("Forbidden 403");
    }
}
