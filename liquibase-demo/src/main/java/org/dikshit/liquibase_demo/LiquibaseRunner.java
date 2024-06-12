package org.dikshit.liquibase_demo;

import liquibase.Contexts;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.resource.ClassLoaderResourceAccessor;
import liquibase.resource.ResourceAccessor;

import java.sql.Connection;
import java.sql.DriverManager;

public class LiquibaseRunner {

    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/demoDb1";
        String user = "postgres";
        String password = "password";
        String changelogFile = "changelog.xml";

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(conn));
            ResourceAccessor resourceAccessor = new ClassLoaderResourceAccessor();
            Liquibase liquibase = new Liquibase(changelogFile, resourceAccessor, database);

            liquibase.update(new Contexts());
            System.out.println("Database updated successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
