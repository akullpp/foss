package de.akullpp.foss;


import de.akullpp.foss.client.LicenseClient;
import io.github.cdimascio.dotenv.Dotenv;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) {
        // TODO: Commandline parser

        Dotenv dotenv = Dotenv.load();
        Foss foss = new Foss(dotenv.get("FOSS_GITHUB_URL_PATTERN"), dotenv.get("FOSS_GITHUB_OWNER_REPOSITORIES_PATTERN"));
        LicenseClient client = new LicenseClient(dotenv.get("FOSS_GITHUB_OAUTH2_TOKEN"));

        Optional<String> readme = client.getReadme(dotenv.get("FOSS_ROOT_OWNER"), dotenv.get("FOSS_ROOT_REPOSITORY"));

        if (readme.isEmpty()) {
            throw new FossException();
        }
        List<String> urls = foss.parseUrls(readme.get());
        HashMap<String, List<String>> ownerRepositories = foss.getRepositories(
            urls,
            Arrays.asList(dotenv.get("FOSS_OWNER_BLACKLIST").split(","))
        );
        List<License> licenses = client.getLicenses(ownerRepositories);
        foss.writeLicenses(licenses);
    }
}
