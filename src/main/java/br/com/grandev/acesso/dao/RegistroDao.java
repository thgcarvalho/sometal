package br.com.grandev.acesso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import br.com.grandev.acesso.Registro;

public class RegistroDao {

	public void insert(Registro registro) {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into lockheader values(?,?)");
			ps.setString(1, registro.getTipo());
			ps.setString(2, registro.getTipo());
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
	
	public void update(Registro registro) {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("update lockheader set function=? where id=?");
			ps.setString(1, registro.getTipo());
			ps.setString(2, registro.getTipo());
			int i = ps.executeUpdate();
			if (i != 0) {
				System.out.println("updated");
			} else {
				System.out.println("not updated");
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
	
	public void delete(Registro registro) {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete lockheader where id=?");
			ps.setString(1, registro.getTipo());
			int i = ps.executeUpdate();
			if (i != 0) {
				System.out.println("deleted");
			} else {
				System.out.println("not deleted");
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
