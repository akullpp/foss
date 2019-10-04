package de.akullpp.foss.client;

import de.akullpp.foss.Foss;
import de.akullpp.foss.License;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Optional;
import org.eclipse.egit.github.core.Repository;
import org.eclipse.egit.github.core.RepositoryContents;
import org.eclipse.egit.github.core.client.GitHubClient;
import org.eclipse.egit.github.core.client.GitHubRequest;
import org.eclipse.egit.github.core.client.GitHubResponse;
import org.eclipse.egit.github.core.client.RequestException;
import org.eclipse.egit.github.core.service.ContentsService;
import org.eclipse.egit.github.core.service.RepositoryService;
import org.eclipse.egit.github.core.util.EncodingUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LicenseClient {
    private final Logger LOG = LoggerFactory.getLogger(getClass().getSimpleName());

    private static final String LICENSE_URL_SEGMENT = "/repos/%s/%s/license";

    private final GitHubClient client;
    private final RepositoryService repositoryService;
    private final ContentsService contentsService;

    public LicenseClient(String oAuth2Token) {
        client = new GitHubClient();
        client.setOAuth2Token(oAuth2Token);
        repositoryService = new RepositoryService();
        contentsService = new ContentsService();
    }

    public Optional<String> getReadme(String ownerName, String repositoryName) {
        try {
            Repository repository = repositoryService.getRepository(ownerName, repositoryName);
            RepositoryContents contents = contentsService.getReadme(repository);
            return Optional.of(new String(EncodingUtils.fromBase64(contents.getContent())));
        } catch (IOException ioe) {
            LOG.error("Could not get repository {} for owner {}", repositoryName, ownerName, ioe);
        } catch (Exception e) {
            LOG.error("Could not get readme information for repository {} and owner {}", repositoryName, ownerName, e);
        }
        return Optional.empty();
    }

    public Optional<LicenseResponse> getLicense(String ownerName, String repositoryName) {
        try {
            GitHubRequest repositoryRequest = new GitHubRequest();
            repositoryRequest.setUri(String.format(LICENSE_URL_SEGMENT, ownerName, repositoryName));
            repositoryRequest.setType(LicenseResponse.class);

            GitHubResponse response = client.get(repositoryRequest);
            return Optional.of((LicenseResponse) response.getBody());
        } catch (IOException ioe) {
            LOG.error("Could not get license information for {} {}", Foss.rebuildUrl(ownerName, repositoryName), ioe.getMessage());
        }
        return Optional.empty();
    }

    public List<License> getLicenses(HashMap<String, List<String>> ownerRepositories) {
        List<License> licenses = new ArrayList<>();

        for (Entry<String, List<String>> entry : ownerRepositories.entrySet()) {
            String owner = entry.getKey();

            for (String repository : entry.getValue()) {
                getLicense(owner, repository).ifPresent(licenseResponse ->
                    licenses.add(new License(owner, repository, licenseResponse.getLicense().getName()))
                );
            }
        }
        return licenses;
    }
}
