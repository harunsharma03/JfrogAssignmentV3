package psi.jfrog.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Loads config values from config.properties under /src/test/resources.
 */
public class Config {

    private static final Properties properties = new Properties();

    static {
        try {
            FileInputStream fis = new FileInputStream("src/test/resources/config.properties");
            properties.load(fis);
        } catch (IOException e) {
            System.out.println("Failed to load config.properties: " + e.getMessage());
        }
    }

    public static String getBaseUrl() {
        return properties.getProperty("base.url");
    }
    
    public static String getSecurityPolicy() {
        return properties.getProperty("security.policy");
    }
    
    public static String gettag() {
        return properties.getProperty("tag");
    }
    
    
    public static String getBaseImage() {
        return properties.getProperty("baseImage");
    }
    
    public static String getCustomImage() {
        return properties.getProperty("customImage");
    }
    
    public static String getDockerPath() {
        return properties.getProperty("docker.path");
    }

    public static String getUsername() {
        return properties.getProperty("username");
    }

    public static String getPassword() {
        return properties.getProperty("password");
    }
    
    public static String getPlatform() {
        return properties.getProperty("docker.platform");
    }

    public static String getRepoName() {
        return properties.getProperty("repo.name");
    }

    public static String getImageName() {
        return properties.getProperty("docker.image.name");
    }

    public static String getImageTag() {
        return properties.getProperty("docker.image.tag");
    }

    public static String getPolicyName() {
        return properties.getProperty("policy.name");
    }

    public static String getWatchName() {
        return properties.getProperty("watch.name");
    }
    
    public static String getWatchNameToApply() {
        return properties.getProperty("watchnametoapply");
    }
    public static String getWatchStartDate() {
        return properties.getProperty("watchstartdate");
    }
    public static String getWatchEndDate() {
        return properties.getProperty("watchenddate");
    }
}
