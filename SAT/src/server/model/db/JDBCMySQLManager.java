/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server.model.db;

import com.mysql.jdbc.exceptions.MySQLIntegrityConstraintViolationException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ega Prianto
 */
public class JDBCMySQLManager {

    public static String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    public static String DB_URL;
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static String USER = "root";
    public static String PASS = "";

    public Connection conn = null;
    public Statement stmt = null;

    public JDBCMySQLManager() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        DB_URL = "jdbc:mysql://localhost/sat";
        conn = DriverManager.getConnection(DB_URL, USER, PASS);
        stmt = conn.createStatement();
    }

    public JDBCMySQLManager(String DB_URL) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.jdbc.Driver");
        this.DB_URL = DB_URL;
        conn = DriverManager.getConnection(this.DB_URL, USER, PASS);
        stmt = conn.createStatement();
    }

    public void close() throws SQLException {
        this.conn.close();
        this.stmt.close();
    }

    public String getIpAddressPortServer(String idServer) throws SQLException {
        String sql = "SELECT `ip_address`,`port` FROM `server` WHERE `id` = '" + idServer + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        String result = rs.getString("ip_address") + ";" + rs.getInt("port");
        rs.close();
        return result;
    }

    public String getIdServer(String ipAddress, int port) throws SQLException {

        String sql = "SELECT `id` FROM `server` WHERE `ip_address` = '" + ipAddress + "' AND `port` = " + port;
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        String result = rs.getString("id");
        rs.close();
        return result;
    }

    public List<String> getAllIpAddressServer() throws SQLException {
        String sql = "SELECT `ip_address`,`port` FROM `server`";
        ResultSet rs = stmt.executeQuery(sql);
        List<String> result = new ArrayList<>();
        while (rs.next()) {
            String getIpAddressPort = rs.getString("ip_address") + ":" + rs.getInt("port");
            result.add(getIpAddressPort);
        }
        return result;
    }

    public List<String> getAllMemberIdGroup(String idGroup) throws SQLException {
        String sql = "SELECT `id_member` FROM `user_group` WHERE `id_group` = '" + idGroup + "'";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        List<String> result = new ArrayList<>();
        while (rs.next()) {
            String idMember = rs.getString("id_member");
            result.add(idMember);
        }
        return result;
    }

    public List<String> getAllIpAddressServerExcept(String idServer) throws SQLException {
        String sql = "SELECT `ip_address`,`port` FROM `server` WHERE `id` != '" + idServer + "'";
        ResultSet rs = stmt.executeQuery(sql);
        List<String> result = new ArrayList<>();
        while (rs.next()) {
            String getIpAddressPort = rs.getString("ip_address") + ":" + rs.getInt("port");
            result.add(getIpAddressPort);
        }
        return result;
    }

    public void insertChat(String id_sender, String id_receiver, String chat, String date) throws SQLException, ParseException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO `chat` (`id_sender`, `id_receiver`, `chat_message`, `datetime`) VALUES (?, ?,?, ?)");
        preparedStatement.setString(1, id_sender);
        preparedStatement.setString(2, id_receiver);
        preparedStatement.setString(3, chat);
        preparedStatement.setString(4, date);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void insertChatGroup(String id_sender, String id_group, String chat, String date) throws SQLException, ParseException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO `group_chat` (`id_sender`, `id_group`, `chat_message`, `datetime`) VALUES (?, ?,?, ?)");
        preparedStatement.setString(1, id_sender);
        preparedStatement.setString(2, id_group);
        preparedStatement.setString(3, chat);
        preparedStatement.setString(4, date);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void insertGroup(String id, String name) throws SQLException, ParseException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO `group_sat` (`id`, `Name`) VALUES (?, ?)");
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, name);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void insertServer(String idServer, String ip_address, int port) throws SQLException {
        try {
            PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO `server` (`id`, `ip_address`, `port`) VALUES (?, ?, ?)");
            preparedStatement.setString(1, idServer);
            preparedStatement.setString(2, ip_address);
            preparedStatement.setInt(3, port);
            preparedStatement.execute();
            preparedStatement.close();
        } catch (MySQLIntegrityConstraintViolationException ex) {
            //idServerAlreadyExist
            //do nothing
        }
    }

    public void insertUser(String id, String id_server, String ip_address_port, String passwd, String prof_name, String public_key, String current_status) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO `user_sat` (`id`, `id_server`, `ip_address_port`, `passwd`, `prof_name`, `public_key`, `current_status`) VALUES (?, ?, ?, ?, ?, ?, ?)");
        preparedStatement.setString(1, id);
        preparedStatement.setString(2, id_server);
        preparedStatement.setString(3, ip_address_port);
        preparedStatement.setString(4, passwd);
        preparedStatement.setString(5, prof_name);
        preparedStatement.setString(6, public_key);
        preparedStatement.setString(7, current_status);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public void insertUserToGroup(String idGroup, String idUser, String publicKey) throws SQLException {
        PreparedStatement preparedStatement = conn.prepareStatement("INSERT INTO `user_group` (`id_group`, `id_member`, `public_key`) VALUES (?, ?, ?)");
        System.out.println("INSERT INTO `user_group` (`id_group`, `id_member`, `public_key`) VALUES ("+idGroup+", "+idUser+", "+publicKey+")");
        preparedStatement.setString(1, idGroup);
        preparedStatement.setString(2, idUser);
        preparedStatement.setString(3, publicKey);
        preparedStatement.execute();
        preparedStatement.close();
    }

    public String getIpAddressPortUser(String id) throws SQLException {
        String sql = "SELECT `ip_address_port` FROM `user_sat` WHERE `id`='" + id + "'";
        System.out.println("Print query ipAddress user :");
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        String result = rs.getString("ip_address_port");
        rs.close();
        return result;
    }

    public void updateIpAddressUser(String id, String ip_address) throws SQLException {
        String sql = "UPDATE `user_sat` SET `ip_address_port` = '" + ip_address + "' WHERE `user_sat`.`id` = '" + id + "'";
        System.out.println("Executing Query = " + sql);
        stmt.executeUpdate(sql);
    }

    public void updateServerUser(String id, String serverId) throws SQLException {
        String sql = "UPDATE `user_sat` SET `id_server` = '" + serverId + "' WHERE `user_sat`.`id` = '" + id + "'";
        System.out.println("Executing Query = " + sql);
        stmt.executeUpdate(sql);
    }

    public String getPasswordUser(String id) throws SQLException {
        String sql = "SELECT `passwd` FROM `user_sat` WHERE `id`='" + id + "'";
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        String result = rs.getString("passwd");
        rs.close();
        return result;
    }

    public String getUsernameUser(String id) throws SQLException {
        String sql = "SELECT `prof_name` FROM `user_sat` WHERE `id`='" + id + "'";
        System.out.println("Print query get username :");
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        String result = rs.getString("prof_name");
        rs.close();
        return result;
    }

    public ArrayList<String> getOnline() throws SQLException {
        String sql = "SELECT `id`,`prof_name` FROM `user_sat` WHERE `current_status` = 'online'";
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<String> result = new ArrayList<>();
        while (rs.next()) {
            String id = rs.getString("id");
            System.out.println("Got Online Id : " + id);
            result.add(id);
        }
        rs.close();
        return result;
    }
    
    public ArrayList<String> getGroups(String id) throws SQLException {
        String sql = "SELECT `id_group` FROM `user_group` WHERE `id_member` = '" + id + "'";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<String> result = new ArrayList<>();
        while (rs.next()) {
            String getId = rs.getString("id_group");
            System.out.println("Got Group Id : " + getId);
            result.add(getId);
        }
        rs.close();
        return result;
    }

    public ArrayList<String> getOnlineExcept(String id) throws SQLException {
        String sql = "SELECT `id`,`prof_name` FROM `user_sat` WHERE `current_status` = 'online' and id !='" + id + "'";
        System.out.println(sql);
        ResultSet rs = stmt.executeQuery(sql);
        ArrayList<String> result = new ArrayList<>();
        while (rs.next()) {
            String getId = rs.getString("id");
            System.out.println("Got Online Id : " + getId);
            result.add(getId);
        }
        rs.close();
        return result;
    }

    public void updateStatusUser(String id, String status) throws SQLException {
        String sql = "UPDATE `user_sat` SET `current_status` ='" + status + "' WHERE `user_sat`.`id` = '" + id + "'";
        System.out.println("Executing Query = " + sql);
        stmt.executeUpdate(sql);
    }
}
