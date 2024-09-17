package com.facebook.database.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.facebook.api.comment.Comment;
import com.facebook.api.post.Like;
import com.facebook.api.post.Post;
import com.facebook.api.user.Profile;
import com.facebook.api.user.User;
import com.facebook.database.DatabaseConnection;

public class CommentService {
	public static boolean create(int post_id,int user_id,String comment,List<Integer> taggedId) {
		int commentId = -1;
    	Connection connection = null;
        try {
            connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO post_comment (post_id, user_id, comment) VALUES (?, ?, ?) RETURNING id");
            ps.setInt(1, post_id);
            ps.setInt(2, user_id);
            ps.setString(3, comment);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                commentId = rs.getInt(1);
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
        
        try {
            connection = DatabaseConnection.getDbConnection();
            PreparedStatement ps = connection.prepareStatement("INSERT INTO comment_tagged_user (comment_id, tagged_user_id) VALUES (?, ?)");
            for (int taggedUserId : taggedId) {
            	ps.setInt(1, commentId);
                ps.setInt(2, taggedUserId);
                ps.executeUpdate();
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
        return true;
    }
	
	
	public static List<Comment> list(int post_id) {
		String query = "SELECT pc.id AS comment_id, pc.comment AS comment_text, u.id AS commenting_user_id, u.firstName AS commenting_user_first_name,u.lastName AS commenting_user_last_name,p.image AS commenting_user_profile_image,tu.id AS tagged_user_id,tu.firstName AS tagged_user_first_name,tu.lastName AS tagged_user_last_name,tp.image AS tagged_user_profile_image FROM post_comment pc JOIN users u ON pc.user_id = u.id LEFT JOIN profile p ON u.id = p.user_id LEFT JOIN comment_tagged_user ctu ON pc.id = ctu.comment_id LEFT JOIN users tu ON ctu.tagged_user_id = tu.id LEFT JOIN profile tp ON tu.id = tp.user_id WHERE pc.post_id = ? ORDER BY pc.created_at;";
		Connection connection = null;
		List<Comment> commentList = new ArrayList<>();
	    Map<Integer, Comment> commentMap = new HashMap<>();
		try {
			connection = DatabaseConnection.getDbConnection();
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, post_id);
			ResultSet resultSet = preparedStatement.executeQuery();
			while (resultSet.next()) {
				int commentId = resultSet.getInt("comment_id");
	            Comment comment = commentMap.get(commentId);
	            if (comment == null) {
	                User commentingUser = new User(
	                        resultSet.getInt("commenting_user_id"),
	                        resultSet.getString("commenting_user_first_name"),
	                        resultSet.getString("commenting_user_last_name"),
	                        null,
	                        null,
	                        null,
	                        null,
	                        null,
	                        null,
	                        null
	                );
	                comment = new Comment(commentId, commentingUser, resultSet.getString("comment_text"), new ArrayList<>());
	                commentMap.put(commentId, comment);
	                commentList.add(comment);
	            }

	            int taggedUserId = resultSet.getInt("tagged_user_id");
	            if (taggedUserId > 0) {
	                User taggedUser = new User(
	                        taggedUserId,
	                        resultSet.getString("tagged_user_first_name"),
	                        resultSet.getString("tagged_user_last_name"),
	                        null,
	                        null,
	                        null,
	                        null,
	                        null,
	                        null,
	                        null
	                );
	                comment.getTag().add(taggedUser);
	            }
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
		return commentList;
	}
}
