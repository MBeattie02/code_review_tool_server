package com.example.serverside.mongoDB.info;

/**
 * Class representing the repository information.
 * Contains details such as the username, repository name, commit ID, and path.
 */
public class RepositoryInfo {
    private String username;
    private String repo;
    private String commitId;
    private String path;

    /**
     * Sets the username associated with the repository.
     *
     * @param username The username to set.
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the repository name.
     *
     * @param repo The name of the repository.
     */
    public void setRepo(String repo) {
        this.repo = repo;
    }

    /**
     * Sets the commit ID associated with the repository.
     *
     * @param commitId The commit ID to set.
     */
    public void setCommitId(String commitId) {
        this.commitId = commitId;
    }

    /**
     * Sets the path within the repository.
     *
     * @param path The path to set.
     */
    public void setPath(String path) {
        this.path = path;
    }


    /**
     * Gets the username associated with the repository.
     *
     * @return The username.
     */
    public String getUsername() {
        return username;
    }

    /**
     * Gets the repository name.
     *
     * @return The name of the repository.
     */
    public String getRepo() {
        return repo;
    }

    /**
     * Gets the commit ID associated with the repository.
     *
     * @return The commit ID.
     */
    public String getCommitId() {
        return commitId;
    }

    /**
     * Gets the path within the repository.
     *
     * @return The path.
     */
    public String getPath() {
        return path;
    }

    /**
     * Returns a string representation of the object.
     *
     * @return A string representation of the object.
     */
    @Override
    public String toString() {
        return "username='" + username + '\'' + "\n" +
                "repo='" + repo + '\'' + "\n" +
                "commitId='" + commitId + '\'' + "\n" +
                "path='" + path + '\''
                ;
    }

}
