package com.mycompany.DAO;

import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.Models.Item;
import com.mycompany.Models.ItemWithService;
import com.mycompany.Models.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement findByIdWithServices;

    public ItemDAO(Connection conn) throws CustomDatabaseException {
        try {
            insert = conn.prepareStatement("INSERT INTO item (service_id, square_meter, order_id) VALUES (?,?,?)");
            delete = conn.prepareStatement("DELETE FROM item WHERE order_id = ?");
            findByIdWithServices = conn.prepareStatement("SELECT * FROM item LEFT JOIN service ON service.id = item.service_id WHERE order_id = ?");
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex);
        }
    }

    public void insert(Item item) throws CustomDatabaseException {
        try {
            insert.setInt(1, item.getServiceId());
            insert.setDouble(2, item.getSquareMeter());
            insert.setInt(3, item.getOrderId());
            insert.executeUpdate();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex);
        }
    }

    public void delete(int id) throws CustomDatabaseException {
        try {
            delete.setInt(1, id);
            delete.executeUpdate();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    public List<ItemWithService> findByIdWithService(int id) throws CustomDatabaseException {
        try {
            findByIdWithServices.setInt(1, id);
            ResultSet rs = findByIdWithServices.executeQuery();
            List<ItemWithService> itemWithServiceList = makeList(rs);
            rs.close();
            return itemWithServiceList;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    private List<ItemWithService> makeList(ResultSet rs) throws CustomDatabaseException {
        try {
            List<ItemWithService> itemWithServiceList = new ArrayList<>();
            while (rs.next()) {
                itemWithServiceList.add(makeOne(rs));
            }
            return itemWithServiceList;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    private ItemWithService makeOne(ResultSet rs) throws CustomDatabaseException {
            Item item = new Item();
            Service service = new Service();
        try {
            item.setId(rs.getInt("id"));
            item.setServiceId(rs.getInt("service_id"));
            item.setSquareMeter(rs.getDouble("square_meter"));
            item.setOrderId(rs.getInt("order_id"));
            service.setName(rs.getString("name"));
            service.setPrice(rs.getInt("price"));
            ItemWithService itemWithService = new ItemWithService(item, service);
            return itemWithService;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    public void close() throws CustomDatabaseException {
        try {
            this.insert.close();
            this.delete.close();
            this.findByIdWithServices.close();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

}
