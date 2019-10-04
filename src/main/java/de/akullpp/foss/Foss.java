package de.akullpp.foss;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Foss {

    private final Logger LOG = LoggerFactory.getLogger(getClass().getSimpleName());
    private final Pattern GITHUB_URL_PATTERN;
    private final Pattern GITHUB_OWNER_REPOSITORIES_PATTERN;

    public static final String GITHUB_REPOSITORY_SEGMENT = "https://github.com/%s/%s";

    public Foss(String githubUrlRegex, String githubOwnerRepositoriesRegex) {
        GITHUB_URL_PATTERN = Pattern.compile(githubUrlRegex);
        GITHUB_OWNER_REPOSITORIES_PATTERN = Pattern.compile(githubOwnerRepositoriesRegex);
    }

    public List<String> parseUrls(String readme) {
        List<String> urls = new ArrayList<>();
        readme.lines().forEach(line -> {
            Matcher matcher = GITHUB_URL_PATTERN.matcher(line);
            if (matcher.find()) {
                urls.add(matcher.group(1));
            }
        });
        LOG.info("Found {} GitHub URLs", urls.size());
        return urls;
    }

    public HashMap<String, List<String>> getRepositories(List<String> urls, List<String> blacklist) {
        HashMap<String, List<String>> ownerRepositories = new HashMap<>();
        int repositoryCount = 0;

        for (String url : urls) {
            Matcher matcher = GITHUB_OWNER_REPOSITORIES_PATTERN.matcher(url);
            if (matcher.find()) {
                String owner = matcher.group(1);
                String repository = matcher.group(2);

                if (!blacklist.contains(owner)) {
                    ownerRepositories.computeIfAbsent(owner, k -> new ArrayList<>()).add(repository);
                    repositoryCount++;
                }
            }
        }
        LOG.info("Extracted {} owners with {} repositories", ownerRepositories.size(), repositoryCount);
        return ownerRepositories;
    }

    public static String rebuildUrl(String ownerName, String repositoryName) {
        return String.format(GITHUB_REPOSITORY_SEGMENT, ownerName, repositoryName);
    }

    public void writeLicenses(List<License> licenses) {
        // TODO: Write to file.
        licenses.forEach(license -> LOG.info(license.toString()));
    }

    public void checkLicenses(String readme, List<License> licenses) {
        // TODO: Compare licenses in readme with extracted licenses.
        LOG.info("TODO: Compare licenses in readme with extracted licenses");
    }
}
