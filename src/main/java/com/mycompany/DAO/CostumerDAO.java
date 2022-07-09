package com.mycompany.DAO;

import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.Models.Costumer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handle Costumer database table
 */
public class CostumerDAO {

    private PreparedStatement insert;
    private PreparedStatement delete;
    private PreparedStatement update;
    private PreparedStatement findAll;
    private PreparedStatement findById;

    /**
     * Prepare queries for methods
     *
     * @throws CustomDatabaseException database connection failed or statements fail
     */
    public CostumerDAO(Connection conn) throws CustomDatabaseException {
        try {
            this.insert = conn.prepareStatement("INSERT INTO costumer (name, address, phone_number, email, vat_number) VALUES (?,?,?,?,?)");
            this.delete = conn.prepareStatement("DELETE FROM costumer WHERE id = ?");
            this.update = conn.prepareStatement("UPDATE costumer SET name = ?, address = ?, phone_number = ?, email = ?, vat_number = ? WHERE id = ?");
            this.findAll = conn.prepareStatement("SELECT * FROM costumer");
            this.findById = conn.prepareStatement("SELECT * FROM costumer WHERE id = ?");
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * Save a new Costumer
     *
     * @param costumer Costumer object
     * @throws CustomDatabaseException insert fail
     */
    public void insert(Costumer costumer) throws CustomDatabaseException {
        try {
            this.insert.setString(1, costumer.getName());
            this.insert.setString(2, costumer.getAddress());
            this.insert.setString(3, costumer.getPhoneNumber());
            this.insert.setString(4, costumer.getEmail());
            this.insert.setString(5, costumer.getVatNumber());
            this.insert.executeUpdate();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }
    /**
     * Costumer cells update
     *
     * @param costumer Costumer object
     * @throws CustomDatabaseException update fail
     */
    public void update(Costumer costumer) throws CustomDatabaseException {
        try {
            this.update.setString(1, costumer.getName());
            this.update.setString(2, costumer.getAddress());
            this.update.setString(3, costumer.getPhoneNumber());
            this.update.setString(4, costumer.getEmail());
            this.update.setString(5, costumer.getVatNumber());
            this.update.setInt(6, costumer.getId());
            this.update.executeUpdate();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * Costumer delete from database
     *
     * @param id Costumer id
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
     * List all Costumer from database
     *
     * @return List of Costumer
     * @throws CustomDatabaseException database error
     */
    public List<Costumer> findAll() throws CustomDatabaseException {
        try {
            ResultSet all = this.findAll.executeQuery();
            List<Costumer> costumerList = makeList(all);
            all.close();
            return costumerList;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * Get Costumer from database by Costumer identifier
     *
     * @param id Costumer id
     * @return Costumer
     * @throws CustomDatabaseException database error
     */
    public Costumer findById(int id) throws CustomDatabaseException {
        try {
            this.findById.setInt(1, id);
            ResultSet rs = this.findById.executeQuery();
            Costumer costumer = null;
            while (rs.next()) {
                costumer = makeOne(rs);
            }
            rs.close();
            return costumer;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    /**
     * Close PreparedStatements
     *
     * @throws CustomDatabaseException if closing not success
     */
    public void close() throws CustomDatabaseException {
        try {
            this.insert.close();
            this.update.close();
            this.delete.close();
            this.findAll.close();
            this.findById.close();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    private List<Costumer> makeList(ResultSet rs) throws CustomDatabaseException {
        try {
            List<Costumer> costumerList = new ArrayList<Costumer>();
            while (rs.next()) {
                costumerList.add(makeOne(rs));
            }
            return costumerList;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    private Costumer makeOne(ResultSet rs) throws CustomDatabaseException {
        Costumer costumer = new Costumer();
        try {
            costumer.setId(rs.getInt("id"));
            costumer.setName(rs.getString("name"));
            costumer.setAddress(rs.getString("address"));
            costumer.setPhoneNumber(rs.getString("phone_number"));
            costumer.setEmail(rs.getString("email"));
            costumer.setVatNumber(rs.getString("vat_number"));
            return costumer;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

}
