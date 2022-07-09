package com.mycompany.DAO;

import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.Models.Costumer;
import com.mycompany.Models.Order;
import com.mycompany.Models.OrderWithUser;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Handle database Order table
 */
public class OrderDAO {

    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement findAllWithUser;
    private PreparedStatement findTotalPrice;

    /**
     * Prepare queries for methods
     *
     * @throws CustomDatabaseException if statements error or connection failed
     */
    public OrderDAO(Connection conn) throws CustomDatabaseException {
        try {
            this.insert = conn.prepareStatement("INSERT INTO offer.order (date, costumer_id) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS);
            this.delete = conn.prepareStatement("DELETE FROM offer.order WHERE id = ?");
            this.findAllWithUser = conn.prepareStatement("SELECT * FROM offer.order LEFT JOIN offer.costumer ON costumer.id = order.costumer_id");
            this.findTotalPrice = conn.prepareStatement("SELECT SUM(service.price * item.square_meter)"
                    + " FROM offer.service INNER JOIN offer.item ON item.service_id = service.id"
                    + " WHERE item.order_id = ?;");
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * Save Order to database
     *
     * @param order Order object
     * @throws CustomDatabaseException if insert fail
     */
    public int insert(Order order) throws CustomDatabaseException {
        try {
            this.insert.setDate(1, Date.valueOf(order.getDate()));
            this.insert.setInt(2, order.getCostumerId());
            this.insert.executeUpdate();

            ResultSet generatedKeys = insert.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Insert is not success!");
            }

        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * Delete Order from database by Order identifier
     *
     * @param id Order identifier
     * @throws CustomDatabaseException delete fail
     */
    public void delete(int id) throws CustomDatabaseException {
        try {
            this.delete.setInt(1, id);
            this.delete.executeUpdate();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * Get Order Total Price by Order identifier
     *
     * @param id Order identifier
     * @return Product of Order items square meters and them service prices
     *
     * @throws CustomDatabaseException if database error
     */
    public int totalPrice(int id) throws CustomDatabaseException {
        try {
            findTotalPrice.setInt(1, id);
            ResultSet rs = this.findTotalPrice.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            } else {
                throw new SQLException("");
            }
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * List all Order with them User from database
     *
     * @return List of Order
     * @throws CustomDatabaseException if database error
     */
    public List<OrderWithUser> findAllWithUser() throws CustomDatabaseException {
        try {
            ResultSet all = this.findAllWithUser.executeQuery();
            List<OrderWithUser> orderList = makeList(all);
            all.close();
            return orderList;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    private List<OrderWithUser> makeList(ResultSet rs) throws CustomDatabaseException {
        try {
            List<OrderWithUser> orderList = new ArrayList<>();
            while (rs.next()) {
                orderList.add(makeOne(rs));
            }
            return orderList;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    private OrderWithUser makeOne(ResultSet rs) throws CustomDatabaseException {
        Order order = new Order();
        Costumer costumer = new Costumer();
        try {
            order.setId(rs.getInt("id"));
            order.setDate(rs.getDate("date").toLocalDate());
            order.setCostumerId(rs.getInt("costumer_id"));
            Optional<String> userName = Optional.ofNullable(rs.getString("name"));
            costumer.setName(userName.isPresent() ? userName.get() : "(deleted user)");
            OrderWithUser orderWithUser = new OrderWithUser(order, costumer);
            return orderWithUser;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * Close PrepareStatements
     *
     * @throws CustomDatabaseException if database error
     */
    public void close() throws CustomDatabaseException {
        try {
            this.insert.close();
            this.delete.close();
            this.findAllWithUser.close();
            this.findTotalPrice.close();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

}
