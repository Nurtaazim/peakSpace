package peakspace.service;

import peakspace.dto.request.CommentRequest;
import peakspace.dto.response.CommentInnerResponse;
import peakspace.dto.response.CommentResponseByPost;
import peakspace.dto.response.SimpleResponse;

import java.util.List;

public interface CommentService {
    SimpleResponse save(Long postId, CommentRequest comment);

    List<CommentResponseByPost> getAllComment(Long postId);

    SimpleResponse editComment(Long commentId, CommentRequest comment);

    SimpleResponse deleteComment(Long commentId);

    CommentInnerResponse findComment(Long commentId);
}
