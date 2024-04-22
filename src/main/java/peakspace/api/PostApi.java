package peakspace.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import peakspace.dto.request.PostRequest;
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

    @GetMapping("/getPost/{postId}")
    public PostResponse getPost(@PathVariable Long postId){
        return postService.getById(postId);
    }

    @PostMapping("/updatePost/{postId}")
    public SimpleResponse updatePost(@PathVariable Long postId,@RequestBody PostRequest postRequest){
        return  postService.update(postId,postRequest);
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
