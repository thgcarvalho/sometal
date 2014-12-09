package br.com.grandev.acesso.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.grandev.acesso.model.Registro;

public class RegistroDao {
	
	public void insert(Registro registro) {
		Connection conn = WebConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into registro values(?,?,?,?,?)");
			ps.setString(4, registro.getTipo());
			ps.setString(5, registro.getOrigem());
			ps.setDate(2, (Date) registro.getData());
			ps.setDate(3, (Date) registro.getHora());
			ps.setInt(1, registro.getCodigo());
			int i = ps.executeUpdate();
			if (i != 0) {
				System.out.println("Inserted");
			} else {
				System.out.println("not Inserted");
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			try {
				if (conn != null) {
					conn.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
