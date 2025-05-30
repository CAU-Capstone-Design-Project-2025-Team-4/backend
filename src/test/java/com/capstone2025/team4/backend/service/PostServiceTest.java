package com.capstone2025.team4.backend.service;

import com.capstone2025.team4.backend.domain.Post;
import com.capstone2025.team4.backend.domain.User;
import com.capstone2025.team4.backend.domain.design.Design;
import com.capstone2025.team4.backend.service.dto.PostFullDTO;
import com.capstone2025.team4.backend.service.dto.PostSimpleDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PostServiceTest {

    @Autowired
    EntityManager em;
    @Autowired
    PostService postService;

    @Test
    void createNewPost() {
        //given
        User user = User.builder()
                .build();
        Design design = Design.builder().user(user).build();
        final String TITLE = "TITLE";
        final String CONTENT = "CONTENT";

        em.persist(user);
        em.persist(design);
        em.flush();
        em.clear();

        //when
        Post newPost = postService.createNewPost(user.getId(), design.getId(), TITLE, CONTENT);
        em.flush();
        em.clear();

        //then
        Post foundPost = em.find(Post.class, newPost.getId());
        Design foundDesign= em.find(Design.class, design.getId());
        assertThat(foundPost).isNotNull();
        assertThat(foundPost.getTitle()).isEqualTo(TITLE);
        assertThat(foundPost.getContent()).isEqualTo(CONTENT);
        assertThat(foundPost.getUser().getId()).isEqualTo(user.getId());
        assertThat(foundPost.getDesign().getId()).isEqualTo(design.getId());
        
        assertThat(foundPost.getDesign().getInPost()).isTrue();
        assertThat(foundDesign.getInPost()).isTrue();
    }
    
    @Test
    void userPostsPage(){
        //given
        int PAGE_SIZE = 5;

        User user = User.builder()
                .email("email")
                .build();

        em.persist(user);

        make100Posts(user);

        em.flush();
        em.clear();

        //when
        Page<PostSimpleDTO> page = postService.searchPage(user.getId(), 0, PAGE_SIZE);

        //then
        assertThat(page.getSize()).isEqualTo(PAGE_SIZE);
        assertThat(page.getContent()).extracting(PostSimpleDTO::getTitle)
                .containsExactly("temp0", "temp1", "temp2", "temp3", "temp4");
    }

    private void make100Posts(User user) {
        for (int i = 0; i < 100; i++) {
            Post post = Post.builder()
                    .user(user)
                    .title("temp" + i)
                    .build();
            em.persist(post);
        }
    }

    @Test
    void allPostPage(){
        //given
        int PAGE_SIZE = 5;

        User user = User.builder()
                .email("email")
                .build();

        em.persist(user);

        make100Posts(user);

        em.flush();
        em.clear();

        //when
        Page<PostSimpleDTO> page = postService.searchPage(user.getId(), 0, 5);

        //then
        assertThat(page.getSize()).isEqualTo(PAGE_SIZE);
        assertThat(page.getContent()).extracting(PostSimpleDTO::getTitle)
                .containsExactly("temp0", "temp1", "temp2", "temp3", "temp4");
    }

    @Test
    void findPost(){
        //given
        User user = User.builder()
                .email("email")
                .name("name")
                .build();
        Post post = Post.builder()
                .user(user)
                .title("temp")
                .build();

        em.persist(post);
        em.persist(user);
        em.flush();
        em.clear();

        //when
        PostFullDTO result= postService.findPost(post.getId());

        //then
        assertThat(result.getId()).isEqualTo(post.getId());
        assertThat(result.getTitle()).isEqualTo(post.getTitle());
        assertThat(result.getUsername()).isEqualTo(user.getName());
        assertThat(result.getUserId()).isEqualTo(user.getId());
    }

    @Test
    void postOrder(){
        //given
        int PAGE_SIZE = 3;

        User user = User.builder()
                .email("email")
                .build();

        em.persist(user);

        for (int i = 0; i < PAGE_SIZE; i++) {
            Post post= Post.builder()
                    .user(user)
                    .title("post" + i)
                    .build();

            em.persist(post);
            em.flush();
            em.clear();
        }

        em.flush();
        em.clear();

        //when
        Page<PostSimpleDTO> page = postService.searchPage(user.getId(), 0, PAGE_SIZE);

        //then
        assertThat(page.getContent())
                .extracting(PostSimpleDTO::getTitle)
                .containsExactly("post2", "post1", "post0");

    }
}