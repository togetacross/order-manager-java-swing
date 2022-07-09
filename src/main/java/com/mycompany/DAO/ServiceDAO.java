package com.mycompany.DAO;

import com.mycompany.CustomExceptions.CustomDatabaseException;
import com.mycompany.Models.Service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {

    private PreparedStatement findAll;
    private PreparedStatement findById;
    private PreparedStatement save;


    public ServiceDAO(Connection conn) throws CustomDatabaseException {
        try {
            this.findAll = conn.prepareStatement("SELECT * FROM service");
            this.findById = conn.prepareStatement("SELECT * FROM service WHERE id = ?");
            this.save = conn.prepareStatement("INSERT INTO service (name, price) VALUES (?, ?)");
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }
    
    public void save(Service service) throws CustomDatabaseException {
        try {
            this.save.setString(1, service.getName());
            this.save.setInt(2, (int) service.getPrice());
            this.save.executeUpdate();
        }catch (SQLException ex){
            throw new CustomDatabaseException(ex);
        }
    }

    public List<Service> findAll() throws CustomDatabaseException {
        try {
            ResultSet all = this.findAll.executeQuery();
            List<Service> serviceLista = makeList(all);
            all.close();
            return serviceLista;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    public Service findById(int id) throws CustomDatabaseException {
        try {
            this.findById.setInt(1, id);
            ResultSet rs = this.findById.executeQuery();
            Service service = null;
            while (rs.next()) {
                service = makeOne(rs);
            }
            rs.close();
            return service;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    public void close() throws CustomDatabaseException {
        try {
            this.findAll.close();
            this.findById.close();
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    private List<Service> makeList(ResultSet rs) throws CustomDatabaseException {
        try {
            List<Service> serviceList = new ArrayList<Service>();
            while (rs.next()) {
                serviceList.add(makeOne(rs));
            }
            return serviceList;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

    private Service makeOne(ResultSet rs) throws CustomDatabaseException {
        Service service = new Service();
        try {
            service.setId(rs.getInt("id"));
            service.setName(rs.getString("name"));
            service.setPrice(rs.getInt("price"));
            return service;
        } catch (SQLException ex) {
            throw new CustomDatabaseException(ex.getMessage());
        }
    }

}
