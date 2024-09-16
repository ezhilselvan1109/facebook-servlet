package com.facebook.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
	public static Connection getDbConnection(){
		try {
			Class.forName("org.postgresql.Driver");
			Connection connection  = DriverManager.getConnection("jdbc:postgresql://localhost:5432/facebook","postgres", "root");
			return connection;
		} catch (Exception e) {
			System.out.println(e);
		}
		return null;
	}
}