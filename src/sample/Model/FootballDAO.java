package sample.Model;

import sample.Utils.Constant;

import java.sql.*;

public class FootballDAO {
    final static String TABLE = "Football";

    public String add(Football football){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(Constant.URL + Constant.DB_NAME, Constant.DB_USERNAME, "");

            preparedStatement = connection.prepareStatement("INSERT INTO " + TABLE +
                    " (TeamName, Manager, Player, platform, Division, user_id) VALUES (?,?,?,?,?,?)");

            preparedStatement.setString(1, football.getTeamName());
            preparedStatement.setString(2, football.getManager());
            preparedStatement.setDouble(3, football.getPlayers());
            preparedStatement.setString(4, football.getPlatform());
            preparedStatement.setString(5, football.getDivision());
            preparedStatement.setInt(6, football.getUserId());
            preparedStatement.executeUpdate();

            return "Successfully created new entry";
        } catch (SQLException e) {
            e.printStackTrace();
            return "Failure creating new entry";
        }
    }

    public ResultSet searchByTitle(String TeamName, User user){
        String query = "";
        if (user.isAdmin()) {
            if (TeamName.equals("")) {
                query = "SELECT * FROM " + TABLE;
            } else {
                query = "SELECT * FROM " + TABLE + " WHERE TeamName LIKE '" + TeamName + "'";
            }
        } else {
            if (TeamName.equals("")) {
                query = "SELECT * FROM " + TABLE + " WHERE user_id = '" + user.getId() + "'";
            } else {
                query = "SELECT * FROM " + TABLE + " WHERE user_id = '" + user.getId() + "' AND TeamName LIKE '" + TeamName + "'";
            }
        }

        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(Constant.URL + Constant.DB_NAME, Constant.DB_USERNAME, "");
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    public void editById(Football football){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(Constant.URL + Constant.DB_NAME, Constant.DB_USERNAME, "");
            preparedStatement = connection.prepareStatement("UPDATE " + TABLE +
                    " SET TeamName=?, Manager=?, Player=?, platform=?, Division=?, user_id=? WHERE id = ?");
            preparedStatement.setString(1, football.getTeamName());
            preparedStatement.setString(2, football.getManager());
            preparedStatement.setDouble(3, football.getPlayers());
            preparedStatement.setString(4, football.getPlatform());
            preparedStatement.setString(5, football.getDivision());
            preparedStatement.setInt( 7, football.getId());
            preparedStatement.setInt(6, football.getUserId());
            preparedStatement.executeUpdate();

            System.out.println("Successfully updated football team");
        } catch (SQLException e) {
            System.out.println("Failure updating football team");
            e.printStackTrace();
        }
    }
    public void deleteById(int id){
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = DriverManager.getConnection(Constant.URL + Constant.DB_NAME, Constant.DB_USERNAME, "");
            preparedStatement = connection.prepareStatement("DELETE FROM " + TABLE + " WHERE id=?");
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();

            System.out.println("Successfully deleted football team");
        } catch (SQLException e) {
            System.out.println("Failure deleting football team");
            e.printStackTrace();
        }
    }
}
