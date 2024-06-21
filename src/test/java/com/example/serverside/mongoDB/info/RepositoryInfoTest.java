package com.example.serverside.mongoDB.info;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RepositoryInfoTest {

    private RepositoryInfo repoInfo;

    @BeforeEach
    public void setUp() {
        repoInfo = new RepositoryInfo();
    }

    @Test
    public void testUsernameGetterSetter() {
        String testUsername = "testUser";
        repoInfo.setUsername(testUsername);
        assertEquals(testUsername, repoInfo.getUsername());
    }

    @Test
    public void testRepoGetterSetter() {
        String testRepo = "testRepo";
        repoInfo.setRepo(testRepo);
        assertEquals(testRepo, repoInfo.getRepo());
    }

    @Test
    public void testCommitIdGetterSetter() {
        String testCommitId = "123abc";
        repoInfo.setCommitId(testCommitId);
        assertEquals(testCommitId, repoInfo.getCommitId());
    }

    @Test
    public void testPathGetterSetter() {
        String testPath = "/path/to/repo";
        repoInfo.setPath(testPath);
        assertEquals(testPath, repoInfo.getPath());
    }
}
