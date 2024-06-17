package org.example.newsfeed.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.Filter;
import java.security.Principal;
import org.example.newsfeed.config.WebSecurityConfig;
import org.example.newsfeed.dto.PostRequestDTO;
import org.example.newsfeed.entity.Post;
import org.example.newsfeed.entity.User;
import org.example.newsfeed.entity.UserStatusEnum;
import org.example.newsfeed.security.UserDetailsImpl;
import org.example.newsfeed.service.CommentService;
import org.example.newsfeed.service.PostService;
import org.example.newsfeed.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

@WebMvcTest(
    controllers = {PostController.class, CommentController.class},
    excludeFilters = {
        @ComponentScan.Filter(
            type = FilterType.ASSIGNABLE_TYPE,
            classes = WebSecurityConfig.class
        )
    }
)
class ControllerTest {

    private MockMvc mvc;
    private Principal mockPrincipal;
    private User testUser;
    @Autowired
    private WebApplicationContext context;
    @Qualifier("springSecurityFilterChain")
    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;
    @MockBean
    private PostService postService;
    @MockBean
    private CommentService commentService;


    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity(new MockSpringSecurityFilter()))
            .build();
    }

    private void mockUserSetup() {
        // Mock 테스트 유져 생성
        String userId = "id";

        String password = "password";
        String username = "name";
        String email = "email@email.com";
        String comment = "This is a comment";

        String refreshToken = "";
        String statusChangeTime = "";
        UserStatusEnum status = UserStatusEnum.ACTIVE;

        testUser = new User(userId, password, username, email, comment, refreshToken,
            statusChangeTime, status);

        UserDetailsImpl testUserDetails = new UserDetailsImpl(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(testUserDetails, "",
            testUserDetails.getAuthorities());
    }


    @Test
    @DisplayName("게시물 등록")
    void test1() throws Exception {
        //given
        this.mockUserSetup();
        String content = "내용";
        PostRequestDTO requestDTO = new PostRequestDTO();
        requestDTO.setContent(content);

        String postInfo = objectMapper.writeValueAsString(requestDTO);

        Post mockPost = Post.builder().user(testUser).content(content).build();
        Mockito.when(postService.createPost(Mockito.any(PostRequestDTO.class), Mockito.any(User.class)))
            .thenReturn(mockPost);

        //when-then
        mvc.perform(post("/api/posts")
            .content(postInfo)
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
            .principal(mockPrincipal)
        )

            .andExpect(status().isOk())
            .andDo(print());

    }

}
