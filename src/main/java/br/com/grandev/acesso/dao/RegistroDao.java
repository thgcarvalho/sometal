package br.com.grandev.acesso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.grandev.acesso.model.Registro;

public class RegistroDao {
	
	public boolean insert(Registro registro) {
		Connection conn = WebConnectionFactory.getConnection();
		PreparedStatement ps = null;
		boolean saved = false;
		try {
			ps = conn.prepareStatement("insert into registro (origen, tipo, codigo) values(?,?,?)");
			ps.setString(1, registro.getOrigem());
			ps.setString(2, registro.getTipo());
			ps.setInt(3, registro.getCodigo());
//			ps.setDate(x, (Date) registro.getData());
//			ps.setDate(x, (Date) registro.getHora());
			int i = ps.executeUpdate();
			if (i != 0) {
				System.out.println("Inserted");
				saved = true;
			} else {
				System.out.println("not Inserted");
				saved = false;
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
		return saved;
	}

}
