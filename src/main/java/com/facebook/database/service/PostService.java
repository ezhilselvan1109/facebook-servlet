package com.facebook.database.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.facebook.api.post.Like;
import com.facebook.api.post.Post;
import com.facebook.api.user.Profile;
import com.facebook.api.user.User;
import com.facebook.database.DatabaseConnection;

public class PostService {

	public static boolean createPost(int user_id, String description, byte[] image) {
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement ps = connection
					.prepareStatement("INSERT INTO post (user_id,description,image) VALUES (?,?,?);");
			ps.setInt(1, user_id);
			ps.setString(2, description);
			if (image != null) {
				ps.setBytes(3, image);
			} else {
				ps.setNull(3, java.sql.Types.BINARY);
			}
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

	public static boolean like(int user_id, int post_id) {
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement ps = connection.prepareStatement("insert into postlike (post_id,liked_by) values (?,?);");
			ps.setInt(1, post_id);
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

	public static boolean unlike(int user_id, int post_id) {
		Connection connection = null;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement ps = connection.prepareStatement("delete from postlike where liked_by=? and post_id=?;");
			ps.setInt(1, user_id);
			ps.setInt(2, post_id);
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

	public static List<Post> getPosts(int id) {
		String query = "SELECT p.id AS post_id,p.description,p.image,u.firstName,u.lastName,u.id AS user_id,u.email,u.phone,pr.image AS profile_image,COUNT(pl.id) AS total_likes,CASE WHEN EXISTS ( SELECT 1 FROM postlike pl2 WHERE pl2.post_id = p.id AND pl2.liked_by = ?) THEN TRUE ELSE FALSE END AS is_liked FROM post p LEFT JOIN postlike pl ON p.id = pl.post_id JOIN users u ON p.user_id = u.id LEFT JOIN profile pr ON u.id = pr.user_id WHERE p.user_id != ? GROUP BY p.id,u.id, pr.image ORDER BY p.created_at DESC;";
		Connection connection = null;
		List<Post> postList = new ArrayList<>();
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			preparedStatement.setInt(2, id);
			ResultSet posts = preparedStatement.executeQuery();
			while (posts.next()) {
				byte[] post_image = posts.getBytes("image");
				byte[] profile_image = posts.getBytes("profile_image");
				postList.add(new Post(posts.getInt("post_id"),posts.getString("description"),post_image,new User(posts.getInt("user_id"),posts.getString("firstName"), posts.getString("lastName"),null, null, null, null,null,new Profile(profile_image),null),new Like(posts.getBoolean("is_liked"),null,posts.getInt("total_likes"))));
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
		return postList;
	}

	public static List<User> getUserPosts(int id,int from) {
		String query = "SELECT p.id AS post_id,p.description,p.image,u.firstName,u.lastName,u.id as user_id,u.email,u.phone,pr.image AS profile_image,COUNT(pl.id) AS total_likes,CASE WHEN EXISTS ( SELECT 1 FROM postlike pl2 WHERE pl2.post_id = p.id AND pl2.liked_by = ? ) THEN TRUE ELSE FALSE END AS is_liked FROM post p LEFT JOIN postlike pl ON p.id = pl.post_id JOIN users u ON p.user_id = u.id LEFT JOIN profile pr ON u.id = pr.user_id WHERE p.user_id = ? GROUP BY p.id, u.id, pr.image ORDER BY p.created_at DESC;";
		Connection connection = null;
		List<Post> postList = new ArrayList<>();
		List<User> userPost = new ArrayList<>();
		User user=new User();
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, from);
			preparedStatement.setInt(2, id);
			ResultSet posts = preparedStatement.executeQuery();
			boolean isExists=false;
			while (posts.next()) {
				isExists=true;
				user.setId(posts.getInt("user_id"));
				user.setFirst_name(posts.getString("firstName"));
				user.setLast_name(posts.getString("lastName"));
				byte[] profile_image = posts.getBytes("profile_image");
				user.setProfile(new Profile(profile_image));
				byte[] post_image = posts.getBytes("image");
				postList.add(new Post(posts.getInt("post_id"),posts.getString("description"),post_image,null,new Like(posts.getBoolean("is_liked"),null,posts.getInt("total_likes"))));
			}
			if(isExists)
				userPost.add(new User(user.getId(),user.getFirst_name(), user.getLast_name(),null, null, null, null,null,user.getProfile(),postList));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				throw new RuntimeException(e);
			}
		}
		return userPost;
	}
	
	public static int getUserId(int id) {
		String query = "select user_id from post where id=?";
		Connection connection = null;
		int user_id=-1;
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, id);
			ResultSet posts = preparedStatement.executeQuery();
			while (posts.next()) {
				user_id=posts.getInt("user_id");
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
		return user_id;
	}

}
