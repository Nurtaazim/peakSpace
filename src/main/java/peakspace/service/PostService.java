package peakspace.service;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.SimpleResponse;

import java.util.List;

public interface PostService {

    SimpleResponse savePost(PostRequest postRequest);

    SimpleResponse update(Long postId, PostUpdateRequest postUpdateRequest);

    SimpleResponse delete(Long postId);

    SimpleResponse deleteLinkFromPost(Long linkId, Long postId);


     SimpleResponse notationFriend(Long postId,List<Long> foundUserId);

     SimpleResponse removeNotation(List<Long> friendsId);
}
