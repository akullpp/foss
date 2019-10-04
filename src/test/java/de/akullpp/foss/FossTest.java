package de.akullpp.foss;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FossTest {

    @Test
    @DisplayName("Should parse URLs from String")
    void Should_Parse_Urls_From_String() {
        String githubUrlRegex = "(https://github.com/.+?)\\)";
        String githubOwnerRepositoriesRegex = "https://github.com/(.+?)/([^/]+)";
        Foss foss = new Foss(githubUrlRegex, githubOwnerRepositoriesRegex);
        String URL1 = "https://github.com/owner/repository/branch/file";
        String URL2 = "https://github.com/owner2/repository2/branch2/file2";
        String readme = String.format("#Headline\n##Subheadline\n* [Test](%s) - Description.\n\n* [Test2](%s) - Description2.\n", URL1, URL2);

        List<String> result = foss.parseUrls(readme);

        assertThat(result).containsExactly(URL1, URL2);
    }

    @Test
    @DisplayName("Should accumulate repositories for owner from URLs")
    void Should_Accumulate_Repositories_For_Owner_From_Urls() {
        String githubUrlRegex = "(https://github.com/.+?)\\)";
        String githubOwnerRepositoriesRegex = "https://github.com/(.+?)/([^/]+)";
        Foss foss = new Foss(githubUrlRegex, githubOwnerRepositoriesRegex);
        List<String> urls = Arrays.asList(
            "https://github.com/owner/repository/branch/file",
            "https://github.com/owner/repository2/branch2/file2",
            "https://github.com/owner2/repository/branch/file"
        );

        HashMap<String, List<String>> result = foss.getRepositories(urls, Collections.emptyList());

        assertThat(result).containsExactly(
            new SimpleEntry<>("owner", Arrays.asList("repository", "repository2")),
            new SimpleEntry<>("owner2", Collections.singletonList("repository"))
        );
    }

    @Test
    @DisplayName("Should ignore blacklisted owners")
    void Should_Ignore_Blacklisted_Owners() {
        String githubUrlRegex = "(https://github.com/.+?)\\)";
        String githubOwnerRepositoriesRegex = "https://github.com/(.+?)/([^/]+)";
        Foss foss = new Foss(githubUrlRegex, githubOwnerRepositoriesRegex);
        List<String> urls = Arrays.asList(
            "https://github.com/owner/repository/branch/file",
            "https://github.com/owner/repository2/branch2/file2",
            "https://github.com/owner2/repository/branch/file"
        );
        List<String> blacklist = Collections.singletonList("owner");

        HashMap<String, List<String>> result = foss.getRepositories(urls, blacklist);

        assertThat(result).containsExactly(new SimpleEntry<>("owner2", Collections.singletonList("repository")));
    }

    @Test
    @DisplayName("Should rebuild GitHub URLs from owner and repository")
    void Should_Rebuild_Github_Urls_From_Owner_And_Repository() {
        String owner = "owner";
        String repository = "repository";

        String result = Foss.rebuildUrl(owner, repository);

        String expected = String.format("https://github.com/%s/%s", owner, repository);
        assertThat(result).isEqualTo(expected);
    }
}
