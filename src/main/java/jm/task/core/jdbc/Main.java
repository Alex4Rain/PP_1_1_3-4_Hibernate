package jm.task.core.jdbc;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.service.UserService;
import jm.task.core.jdbc.service.UserServiceImpl;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        //Class.forName("com.mysql.cj.jdbc.Driver");
        UserService userService = new UserServiceImpl();
        try {
            userService.createUsersTable();

            userService.saveUser("Alex", "Rain", (byte) 32);
            userService.saveUser("Colin", "Gotr", (byte) 41);
            userService.saveUser("Niolax", "Toomberg", (byte) 28);
            userService.saveUser("Adolf", "Wolf", (byte) 54);

            userService.removeUserById(1L);

            for (User user : userService.getAllUsers()) {
                System.out.println(user);
            }

            userService.cleanUsersTable();

            userService.dropUsersTable();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
