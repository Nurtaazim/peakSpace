package peakspace.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import peakspace.dto.request.PostRequest;
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
}
