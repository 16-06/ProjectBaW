package com.example.projectbaw.service;

import com.example.projectbaw.model.User;
import com.example.projectbaw.model.Vote;
import com.example.projectbaw.repository.UserRepository;
import com.example.projectbaw.repository.VoteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class VoteServiceTest {

    @InjectMocks
    private VoteService voteService;

    @Mock
    private VoteRepository voteRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveVote_shouldSetUserAndAuthorAndSaveVote() {
        // given
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        user.setId(1L);

        Vote vote = new Vote();

        // mock SecurityContext
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        // mock userRepository
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // mock voteRepository
        when(voteRepository.save(any(Vote.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // when
        Vote savedVote = voteService.save(vote);

        // then
        assertEquals(user, savedVote.getUser());
        assertEquals(username, savedVote.getAuthor());

        verify(voteRepository, times(1)).save(vote);
    }

    @Test
    void saveVote_shouldThrowExceptionWhenUserNotFound() {
        // given
        String username = "notExistUser";
        Vote vote = new Vote();

        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(username);
        SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // then
        assertThrows(RuntimeException.class, () -> voteService.save(vote));

        verify(voteRepository, never()).save(any());
    }
}
