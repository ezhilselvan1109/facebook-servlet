package com.facebook.database.service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facebook.api.friend.Friend;
import com.facebook.api.user.Profile;
import com.facebook.api.user.User;
import com.facebook.database.DatabaseConnection;
import com.facebook.util.Password;
import com.facebook.util.Validation;

public class UserService {
	public static String getPassword(String username) {
		String query = "SELECT password FROM users WHERE email = ? or phone = ?";
		Connection connection = null;
		String password = "";
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setInt(2, Validation.phoneNumber(username) ? Integer.parseInt(username) : -1);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				password = resultSet.getString("password");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return password;
	}
	
	public static int getId(String username) {
		String query = "SELECT id FROM users WHERE email = ? or phone = ?";
		Connection connection = null;
		int id=-1;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setInt(2, Validation.phoneNumber(username) ? Integer.parseInt(username) : -1);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				id = resultSet.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return id;
	}

	public static boolean accountCreate(String firstName, String lastName, Date dateOfBirth, String password,String email, int phone) {
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement ps = connection.prepareStatement(
					"INSERT INTO users (firstName, lastName, dateOfBirth, password, email, phone) VALUES (?,?,?,?,?,?);");
			ps.setString(1, firstName);
			ps.setString(2, lastName);
			ps.setDate(3, dateOfBirth);
			ps.setString(4, Password.hashPassword(password));
			ps.setString(5, email);
			ps.setInt(6, phone);
			ps.executeUpdate();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	public static boolean isUserExists(String email, int phone) {
		String query = "SELECT * FROM users WHERE email = ? or phone = ?";
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			preparedStatement.setInt(2, phone);
			ResultSet resultSet = preparedStatement.executeQuery();
			if (resultSet.next()) {
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	public static int getUserId(String username) {
		String query = "SELECT id FROM users WHERE email = ? or phone = ?";
		Connection connection = null;
		int id = -1;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setInt(2, Validation.phoneNumber(username) ? Integer.parseInt(username) : -1);
			ResultSet users = preparedStatement.executeQuery();
			while (users.next()) {
				id = users.getInt(1);
				System.out.println(users.getInt(1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return id;
	}

	public static List<User> getUsersByKey(String key,int id) {
		String query = "SELECT id, firstName, lastName FROM users WHERE (firstName LIKE ? OR lastName LIKE ? ) AND id != ?;";
		Connection connection = null;
		List<User> userList = new ArrayList<>();
		String wildcard = "%";
		if (key.equals("") || key == null)
			wildcard = "";
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			String searchPattern = key + wildcard;
			preparedStatement.setString(1, searchPattern);
			preparedStatement.setString(2, searchPattern);
			preparedStatement.setInt(3, id);
			ResultSet users = preparedStatement.executeQuery();
			while (users.next()) {
				User user = new User(users.getInt("id"), users.getString("firstName"), users.getString("lastName"),null, null, null, null,null,null,null);
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return userList;
	}

	public static List<User> getUsers() {
		String query = "SELECT * FROM users";
		Connection connection = null;
		List<User> userList = new ArrayList<>();
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			ResultSet users = preparedStatement.executeQuery();
			while (users.next()) {
				User user = new User(users.getInt("id"), users.getString("firstName"), users.getString("lastName"),users.getDate("dateOfBirth"),users.getString("email"), users.getInt("phone"),null, null,null,null);
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return userList;
	}

	public static boolean delete(int id) {
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement ps = connection.prepareStatement("delete from user where id=?");
			ps.setInt(1, id);
			ps.executeUpdate();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}

	public static boolean changePassword(int id, String password) {
		String query = "UPDATE users SET password = ? WHERE id = ?";
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement ps = connection.prepareStatement(query);
			ps.setString(1, Password.hashPassword(password));
			ps.setInt(2, id);
			if (ps.executeUpdate() == 1)
				return true;
			System.out.println();
			connection.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}

	public static List<User> getUser(String username) {
		String query = "SELECT u.id,u.firstName,u.lastName,u.dateOfBirth,u.email,u.phone,p.image FROM users u LEFT JOIN profile p ON u.id = p.user_id WHERE u.email = ? or u.phone=?;";
		Connection connection = null;
		List<User> userList = new ArrayList<>();
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setInt(2, Validation.phoneNumber(username) ? Integer.parseInt(username) : -1);
			ResultSet users = preparedStatement.executeQuery();
			while (users.next()) {
				byte[] image = users.getBytes("image");
				User user = new User(users.getInt("id"), users.getString("firstName"), users.getString("lastName"),users.getDate("dateOfBirth"), users.getString("email"), users.getInt("phone"), null,null,new Profile(image),null);
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return userList;
	}

	public static List<User> profile(int id) {
		String query = "SELECT u.id,u.firstName,u.lastName,u.dateOfBirth,u.email,u.phone,p.image FROM users u LEFT JOIN profile p ON u.id = p.user_id WHERE u.id = ?;";
		Connection connection = null;
		List<User> userList = new ArrayList<>();
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet users = preparedStatement.executeQuery();
			while (users.next()) {
				byte[] image = users.getBytes("image");
				User user = new User(users.getInt("id"), users.getString("firstName"), users.getString("lastName"),users.getDate("dateOfBirth"), users.getString("email"), users.getInt("phone"), null,null,new Profile(image),null);
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return userList;
	}

	public static List<User> profile(int id, int from) {
		String query = "SELECT * FROM ( WITH user_details AS ( SELECT u.id AS user_id, u.firstName, u.lastName, u.dateOfBirth, u.email, u.phone,  p.image FROM users u LEFT JOIN profile p ON u.id = p.user_id WHERE u.id = ? ), friendship AS ( SELECT f.is_friend, u1.id AS user1_id, u2.id AS user2_id FROM friends f INNER JOIN users u1 ON f.user1 = u1.id INNER JOIN users u2 ON f.user2 = u2.id WHERE (f.user1 = ? AND f.user2 = ?) OR (f.user1 = ? AND f.user2 = ?) ) SELECT ud.*,f.user1_id as friend_from,f.user2_id as friend_to, f.is_friend FROM user_details ud LEFT JOIN friendship f ON f.user2_id = ud.user_id OR f.user1_id = ud.user_id ) AS result;";
		Connection connection = null;
		List<User> userList = new ArrayList<>();
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, id);
			preparedStatement.setInt(3, from);
			preparedStatement.setInt(4, from);
			preparedStatement.setInt(5, id);
			ResultSet users = preparedStatement.executeQuery();
			while (users.next()) {
				Boolean isFriend = null;
			    boolean isFriendValue = users.getBoolean("is_friend");
			    if (!users.wasNull()) {
			        isFriend = isFriendValue;
			    }
			    Boolean status=true;
			    if(from==users.getInt("friend_from") && isFriend==false)
			    	status=false;
			    if(isFriend==null)
			    	status=null;
				byte[] image = users.getBytes("image");
				User user = new User(users.getInt("user_id"), users.getString("firstName"), users.getString("lastName"),users.getDate("dateOfBirth"), users.getString("email"), users.getInt("phone"), null,new Friend(isFriend,status),new Profile(image),null);
				userList.add(user);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return userList;
	}

	public static boolean profile(int user_id, byte[] image) {
		String query="INSERT INTO profile (image,user_id) VALUES (?,?);";
		if(profileIsExists(user_id)) {
			query="UPDATE profile SET image= ? WHERE user_id = ?;";
		}
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement ps = connection
					.prepareStatement(query);
			if (image != null) {
				ps.setBytes(1, image);
			} else {
				ps.setNull(1, java.sql.Types.BINARY);
			}
			ps.setInt(2, user_id);
			
			if (ps.executeUpdate() == 0)
				return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return true;
	}
	
	public static boolean profileIsExists(int user_id) {
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement ps = connection.prepareStatement("SELECT true as is_exists FROM profile WHERE id = ? LIMIT 1;");
			ps.setInt(1, user_id);
			ResultSet users = ps.executeQuery();
			while (users.next()) {
				return true;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return false;
	}
}
