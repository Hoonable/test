package org.example.newsfeed.entity;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PostTest {
    @Mock
    User user;

    Post post;

    @BeforeEach
    void setUp() {
    }

    @Test
    void test1(){
        //given
        String content = "content";

        //when
        post = Post.builder().user(user).content(content).build();

        //then
        assertEquals(content, post.getContent());
    }

    @Test
    void test2(){
        //given
        String content = "content";
        String content2 = "content2";
        post = Post.builder().user(user).content(content).build();
        //when
        post.setContent(content2);
        //then
        assertEquals(content2, post.getContent());

    }


}