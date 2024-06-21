
# Java Spring Boot Backend for Static Analysis

This Java Spring Boot application serves as the backend for the static analysis tool. It leverages the GitHub API to retrieve code repositories, conducts static analysis on the fetched code, stores the results in MongoDB, and integrates with the Slack API for notifications.




## Features

- GitHub API Integration: Fetches code from specified repositories using the GitHub API.
- Static Analysis: Performs static code analysis on retrieved repositories.
- Multiple Analysis Types: Supports analysis for code smells, complexity, quality, security, and style.
- MongoDB Storage: Stores analysis results in MongoDB for persistence and easy retrieval.
- Slack Notifications: Sends notifications and reports to a specified Slack channel using the Slack API.


## Run Locally

Prerequisites
- Java JDK 17 or later
- Maven
- MongoDB
- Slack account (for Slack API integration)

Installation
```bash
  git clone https://gitlab.eeecs.qub.ac.uk/40293324/analyser_tool_server
```


## Running the Application

Build the project with Maven:
```bash
  mvn clean install
```

Run the Spring Boot application:

```bash
  mvn spring-boot:run
```





## Usage

Once the application is running, it will allow fetching repositories from GitHub based on the input parameters, perform static analysis, store the results in MongoDB, and send notifications to Slack.


## API Reference

- The application provides various endpoints under http://localhost:8080/. The endpoints are categorized as follows:
- GitHub Operations: Retrieve code from GitHub repositories.
- Analysis Operations: Perform and manage code analysis.
- Results Operations: Fetch and manage the results of analyses.

#### git-hub-controller-raw

```http
  GET /raw/githubusercontent/{username}/{repo}/{commitId}/{path}
```

| Parameter | Type     | Description                |
| :-------- | :------- | :------------------------- |
| `username` | `string` | **Required**. Github username |
| `repo` | `string` | **Required**. Repo Name |
| `commitId` | `string` | **Required**. CommitId of code |
| `path` | `string` | **Required**. Code Path in Github |

#### git-hub-controller

```http
  GET /api/github/repos/{username}/{repo}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `username` | `string` | **Required**. Github username |
| `repo` | `string` | **Required**. Repo Name |

```http
  GET /api/github/repos/{username}/{repo}/git/trees/{commitId}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `username` | `string` | **Required**. Github username |
| `repo` | `string` | **Required**. Repo Name |
| `commitId` | `string` | **Required**. CommitId of code |


```http
  GET /api/github/repos/{username}/{repo}/commits
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `username` | `string` | **Required**. Github username |
| `repo` | `string` | **Required**. Repo Name |


#### code-analysis-controller

##### analyse code style

```http
  GET /api/analyse-style
```

##### analyse code security

```http
  GET /api/analyse-security
```

##### analyse code quality

```http
  GET /api/analyse-quality
```

##### analyse code complexity

```http
  GET /api/analyse-complexity
```

##### analyse code smells

```http
  GET /api/analyse-code-smells
```

##### use all code analysis

```http
  GET /api/analyse-all
```

#### code-analysis-controller

```http
  GET /api/results-all/{id}
```

| Parameter | Type     | Description                       |
| :-------- | :------- | :-------------------------------- |
| `id` | `string` | **Required**. MongoDB ID of analysis to retreive |


#### database-results-controller

```http
  GET /api/combined-results/all
```

```http
  GET /api/combined-results/all/path/{path}
```

| Parameter | Type     | Description                                        |
| :-------- | :------- |:---------------------------------------------------|
| `path` | `string` | **Required**. Name of path to retreive details for |

#### code-analysis-controller-scheduled

```http
  POST /api/schedule-analysis
```

#### scheduled-task-entity-controller

```http
  GET /scheduledTasks
```

```http
  POST /scheduledTasks
```

```http
  GET /scheduledTasks/{id}
```

```http
  PUT /scheduledTasks/{id}
```

```http
  DELETE /scheduledTasks/{id}
```

```http
  PATCH /scheduledTasks/{id}
```

#### scheduled-task-entity-controller

```http
  GET /scheduledTasks/search/findByScheduleTimeBefore
```
