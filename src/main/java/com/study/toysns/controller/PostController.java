package com.study.toysns.controller;

import com.study.toysns.controller.request.PostCreateRequest;
import com.study.toysns.controller.request.PostModifyRequest;
import com.study.toysns.controller.response.PostResponse;
import com.study.toysns.controller.response.Response;
import com.study.toysns.model.Post;
import com.study.toysns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
@RestController
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication) {
        postService.create(request.getTitle(), request.getBody(), authentication.getName());

        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable Long postId, @RequestBody PostModifyRequest request, Authentication authentication) {
        Post post = postService.modify(request.getTitle(), request.getBody(), authentication.getName(), postId);

        return Response.success(PostResponse.from(post));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable Long postId, Authentication authentication) {
        postService.delete(authentication.getName(), postId);

        return Response.success();
    }

}
