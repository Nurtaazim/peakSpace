package peakspace.service;

import peakspace.dto.request.CommentRequest;
import peakspace.dto.response.*;

import java.util.List;

public interface CommentService {
    SimpleResponse save(Long postId, CommentRequest comment);

    List<CommentResponse> getAllComment(Long postId);

    SimpleResponse editComment(Long commentId, CommentRequest comment);

    CommentInnerResponse findComment(Long commentId);

    SimpleResponse saveInnerComment(Long commentId, CommentRequest commentRequest);

    SimpleResponse editInnerComment(Long innerCommentId, CommentRequest commentRequest);

    SimpleResponse removeInnerComment(Long innerCommentId);

    InnerCommentResponse findInnerComment(Long innerCommentId);

    List<InnerCommentResponse> getAllInnerComment(Long commentId);
}
