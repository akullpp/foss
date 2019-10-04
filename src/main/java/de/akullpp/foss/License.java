package de.akullpp.foss;

public class License {

    private static final String OUTPUT_FORMAT = "%s\t\t\t\t\t%s";

    private String owner;
    private String repository;
    private String license;

    public License(String owner, String repository, String license) {
        this.owner = owner;
        this.repository = repository;
        this.license = license;
    }

    public String getUrl() {
        return Foss.rebuildUrl(owner, repository);
    }

    public String getOwner() {
        return owner;
    }

    public String getRepository() {
        return repository;
    }

    public String getLicense() {
        return license;
    }

    @Override
    public String toString() {
        return String.format(OUTPUT_FORMAT, license, getUrl());
    }
}
