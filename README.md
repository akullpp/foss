# FOSS Scanner [![Build Status](https://travis-ci.com/akullpp/foss.svg?branch=master)](https://travis-ci.com/akullpp/foss)

The FOSS Scanner is used to extract GitHub repositories from a README via the v3 API and check their licenses.

## Configuration

Is done in the `.env` file.

Please use your own `FOSS_GITHUB_OAUTH2_TOKEN` as described [here](https://help.github.com/en/articles/creating-a-personal-access-token-for-the-command-line). There are no privileges required for the token.

## Usage

```sh
./mvnw clean package
java -jar target/foss-*-with-dependencies.jar
```
