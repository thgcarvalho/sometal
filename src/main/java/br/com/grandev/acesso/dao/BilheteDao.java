package br.com.grandev.acesso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.grandev.acesso.model.Bilhete;

/**
 * @author Thiago Carvalho
 * 
 */
public class BilheteDao {
	
	public List<Bilhete> getAll() {
		Connection conn = LocalConnectionFactory.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Bilhete> bilhetes = new ArrayList<Bilhete>();
		Bilhete bilhete = null;
		try {
			ps = conn.prepareStatement("select \"NumInner\", \"Tipo\", \"Data\", \"Cartao\", \"Status\""
					+ " from \"tb_INNER\"");
			rs = ps.executeQuery();
			while (rs.next()) {
				bilhete = new Bilhete();
				bilhete.setOrigem(rs.getString(1));
				bilhete.setTipo(rs.getString(2));
				bilhete.setData(rs.getDate(3));
				bilhete.setHora(rs.getDate(4));
				bilhete.setCodigo(rs.getInt(5));
				bilhete.setStatus(rs.getInt(6));
				bilhetes.add(bilhete);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (ps != null) {
					ps.close();
				}
				if (rs != null) {
					rs.close();
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
		return bilhetes;
	}

	public void insert(Bilhete bilhete) {
		Connection conn = LocalConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into \"tb_INNER\" values(?,?,?,?)");
			ps.setString(1, bilhete.getOrigem());
			ps.setString(2, bilhete.getTipo());
			ps.setDate(3, (java.sql.Date) bilhete.getData());
			ps.setDate(4, (java.sql.Date) bilhete.getHora());
			ps.setInt(5, bilhete.getCodigo());
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
	
	public void update(Bilhete bilhete) {
		Connection conn = LocalConnectionFactory.getConnection();
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("update \"tb_INNER\" set \"Status\"=? where"
					+ " \"NumInner\"=? and \"Tipo\"=? and \"Data\"=? and \"Cartao\"=?");
			ps.setInt(1, bilhete.getStatus());
			ps.setString(2, bilhete.getOrigem());
			ps.setString(3, bilhete.getTipo());
			ps.setDate(4, (java.sql.Date) bilhete.getData());
			ps.setDate(4, (java.sql.Date) bilhete.getHora());
			ps.setInt(5, bilhete.getCodigo());
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
	
	public void delete(Bilhete bilhete) {
		Connection conn = LocalConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete \"tb_INNER\" where"
					+ " \"NumInner\"=? and \"Tipo\"=? and \"Data\"=? and \"Cartao\"=?");
			ps.setString(1, bilhete.getOrigem());
			ps.setString(2, bilhete.getTipo());
			ps.setDate(3, (java.sql.Date) bilhete.getData());
			ps.setDate(4, (java.sql.Date) bilhete.getHora());
			ps.setInt(4, bilhete.getCodigo());
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
