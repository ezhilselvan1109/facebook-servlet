package com.facebook.database.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facebook.api.user.Profile;
import com.facebook.api.user.User;
import com.facebook.database.DatabaseConnection;
import com.facebook.util.Validation;

public class FriendService {
	public static boolean request(int from,int to) {
    	Connection connection = null;
        try {
            connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps = connection.prepareStatement("insert into friends (user1,user2) values (?,?);");
            ps.setInt(1, from);
            ps.setInt(2, to);
            if(ps.executeUpdate()==0)
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
	
	public static boolean accept(int from,int to) {
    	Connection connection = null;
        try {
            connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps = connection.prepareStatement("update friends set is_friend=true where( user1=? and user2=?) or( user1=? and user2=?);");
            ps.setInt(1, from);
            ps.setInt(2, to);
            ps.setInt(3, to);
            ps.setInt(4, from);
            if(ps.executeUpdate()==0)
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
	
	public static boolean reject(int from,int to) {
    	Connection connection = null;
        try {
            connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps = connection.prepareStatement("delete from friends where (user1=? and user2=?) or (user1=? and user2=?);");
            ps.setInt(1, from);
            ps.setInt(2, to);
            ps.setInt(3, to);
            ps.setInt(4, from);
            if(ps.executeUpdate()==0)
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

	public static List<User> list(int user_id) {
		String query = "SELECT u.id, u.firstName,u.lastName, p.image FROM users u JOIN friends f ON (f.user1 = u.id OR f.user2 = u.id) AND f.is_friend = true LEFT JOIN profile p ON u.id = p.user_id WHERE (f.user1 = ? OR f.user2 = ?) AND u.id != ?;";
		Connection connection = null;
		List<User> userList = new ArrayList<>();
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, user_id);
			preparedStatement.setInt(2, user_id);
			preparedStatement.setInt(3, user_id);
			ResultSet users = preparedStatement.executeQuery();
			while (users.next()) {
				byte[] image = users.getBytes("image");
				User user = new User(users.getInt("id"), users.getString("firstName"), users.getString("lastName"),null, null, null, null,null,new Profile(image),null);
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
}
