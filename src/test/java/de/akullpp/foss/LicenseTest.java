package de.akullpp.foss;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class LicenseTest {

    @Test
    @DisplayName("Should return the computed GitHub URL")
    void Should_Return_The_Computed_Github_Url() {
        String ownerName = "owner";
        String repositoryName = "repository";
        String licenseName = "license";
        License license = new License(ownerName, repositoryName, licenseName);

        String result = license.getUrl();

        String expected = String.format("https://github.com/%s/%s", ownerName, repositoryName);
        assertThat(result).isEqualTo(expected);
    }

    @Test
    @DisplayName("Should return custom string with license and URL")
    void Should_Return_Custom_String_With_License_And_Url() {
        String ownerName = "owner";
        String repositoryName = "repository";
        String licenseName = "license";
        License license = new License(ownerName, repositoryName, licenseName);

        String result = license.toString();

        String expected = String.format("%s\t\t\t\t\thttps://github.com/%s/%s", licenseName, ownerName, repositoryName);
        assertThat(result).isEqualTo(expected);
    }
}