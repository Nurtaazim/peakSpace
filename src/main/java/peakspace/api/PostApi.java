package peakspace.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PostRequest;
import peakspace.dto.request.PostUpdateRequest;
import peakspace.dto.response.PostResponse;
import peakspace.dto.response.SimpleResponse;
import peakspace.service.PostService;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PostApi {
    private final PostService  postService;
    @PostMapping("/post")
    public SimpleResponse savePost(@RequestBody PostRequest postRequest){
      return   postService.savePost(postRequest);
    }

    @GetMapping("/getPost/{postId}/{userId}")
    public PostResponse getPost(@PathVariable Long postId,@PathVariable Long userId){
        return postService.getById(postId,userId);
    }

    @PostMapping("/updatePost/{postId}")
    public SimpleResponse updatePost(@PathVariable Long postId,@RequestBody PostUpdateRequest postUpdateRequest){
        return  postService.update(postId,postUpdateRequest);
    }
    @PostMapping("/deletePost/{postId}")
    public SimpleResponse delete (@PathVariable Long postId){
        return postService.delete(postId);
    }

    @PostMapping("/deleteLink/{linkId}/{postId}")
    public SimpleResponse deleteLink(@PathVariable Long linkId,@PathVariable Long postId){
        return postService.deleteLinkFromPost(linkId,postId);
    }

}
