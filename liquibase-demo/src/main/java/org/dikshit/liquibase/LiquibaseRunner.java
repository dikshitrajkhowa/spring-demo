package org.dikshit.liquibase;

import liquibase.Liquibase;
import liquibase.Contexts;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.FileSystemResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;

public class LiquibaseRunner {

    public static void main(String[] args) {
        if (args.length < 6) {
            System.out.println("Usage: java -jar LiquibaseRunner.jar <dbUrl> <dbUser> <dbPassword>  <searchDir> <changelogFile> <action> [rollbackTag]");
            System.exit(1);
        }

        String url = args[0];
        String user = args[1];
        String password = args[2];
        String searchDir = args[3];
        String changelogFilePath = args[4];
        String action = args[5];
        String rollbackTag = args.length > 6 ? args[6] : null;

        // Ensure the changelog file exists
        File changelogFile = new File(searchDir+changelogFilePath);
        if (!changelogFile.exists()) {
            System.err.println("The changelog file was not found at: " + changelogFilePath);
            System.exit(1);
        }

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            ResourceAccessor resourceAccessor = new FileSystemResourceAccessor();

            Liquibase liquibase = new Liquibase(changelogFilePath, new FileSystemResourceAccessor(new File(searchDir)), database);


            if ("update".equalsIgnoreCase(action)) {
                liquibase.update(new Contexts());
                System.out.println("Database updated successfully!");
            } else if ("rollback".equalsIgnoreCase(action)) {
                if (rollbackTag != null) {
                    liquibase.rollback(rollbackTag, new Contexts());
                    System.out.println("Database rolled back to tag: " + rollbackTag);
                } else {
                    System.out.println("Rollback tag not provided. Exiting.");
                }
            } else {
                System.out.println("Invalid action specified. Please enter either 'update' or 'rollback'.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
