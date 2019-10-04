# FOSS Scanner [![Build Status](https://travis-ci.com/akullpp/foss.svg?branch=master)](https://travis-ci.com/akullpp/foss)

The FOSS Scanner is used to extract GitHub repositories from a README via the v3 API and check their licenses.

## Configuration

Configuration is done in the `.env` file.

Please use your own `FOSS_GITHUB_OAUTH2_TOKEN` as described [here](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line).

There are no privileges required for the token.

Currently by default the README from [akullpp/awesome-java](https://github.com/akullpp/awesome-java) is parsed, you can change the parsed README by setting `FOSS_ROOT_OWNER` and `FOSS_ROOT_REPOSITORY` environment variables, either in `.env` or the system.

Other environment variables are:

* `FOSS_GITHUB_URL_PATTERN`: The pattern of Github URLs
* `FOSS_GITHUB_OWNER_REPOSITORIES_PATTERN`: The pattern of GitHub URLs for repositories
* `FOSS_OWNER_BLACKLIST`: Comma-denoted blacklist of repository owners.

## Usage

```sh
./mvnw clean package
java -jar target/foss-*-with-dependencies.jar
```
