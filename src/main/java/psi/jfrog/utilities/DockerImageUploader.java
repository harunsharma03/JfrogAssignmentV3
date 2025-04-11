package psi.jfrog.utilities;

import com.aventstack.extentreports.Status;
import psi.jfrog.utilities.ExtentManager;
import java.io.*;
import java.util.*;

public class DockerImageUploader {
    public static boolean pushDockerImageWithScript(String dockerPath, String repo, String username, String password,
                                                    String baseImage, String customImage, String tag) {
        try {
            List<String> command = new ArrayList<>();
            command.add("./push-docker-image.sh");
            command.add(dockerPath);
            command.add(repo);
            command.add(username);
            command.add(password);
            command.add(baseImage);
            command.add(customImage);
            command.add(tag);

            ProcessBuilder builder = new ProcessBuilder(command);
            builder.directory(new File("src/test/resources/scripts"));
            builder.redirectErrorStream(true);

            Process process = builder.start();

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                    ExtentManager.getTest().log(Status.INFO, line);
                }
            }

            int exitCode = process.waitFor();
            if (exitCode == 0) {
                ExtentManager.getTest().log(Status.PASS, "Docker image pushed successfully");
                return true;
            } else {
                ExtentManager.getTest().log(Status.FAIL, "Docker image push failed with exit code: " + exitCode);
                return false;
            }
        } catch (Exception e) {
            ExtentManager.getTest().log(Status.FAIL, "Exception while pushing Docker image: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
