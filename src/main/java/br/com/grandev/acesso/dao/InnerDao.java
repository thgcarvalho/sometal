package br.com.grandev.acesso.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import br.com.grandev.acesso.Inner;

public class InnerDao {
	
	
	public List<Inner> getAll() {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Inner> inners = new ArrayList<Inner>();
		Inner inner = null;
		try {
			ps = conn.prepareStatement("select \"NumInner\", \"Tipo\", \"Data\", \"Cartao\", \"Status\""
					+ " from \"tb_INNER\"");
			rs = ps.executeQuery();
			while (rs.next()) {
				inner = new Inner();
				inner.setNumInner(rs.getInt(1));
				inner.setTipo(rs.getString(2));
				inner.setData(rs.getDate(3));
				inner.setCartao(rs.getString(4));
				inner.setStatus(rs.getInt(5));
				inners.add(inner);
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
		return inners;
	}

	public void insert(Inner inner) {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("insert into \"tb_INNER\" values(?,?,?,?)");
			ps.setInt(1, inner.getNumInner());
			ps.setString(2, inner.getTipo());
			ps.setDate(3, (java.sql.Date) inner.getData());
			ps.setString(4, inner.getCartao());
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
	
	public void update(Inner inner) {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		
		try {
			ps = conn.prepareStatement("update \"tb_INNER\" set \"Status\"=? where"
					+ " \"NumInner\"=? and \"Tipo\"=? and \"Data\"=? and \"Cartao\"=?");
			ps.setInt(1, inner.getStatus());
			ps.setInt(2, inner.getNumInner());
			ps.setString(3, inner.getTipo());
			ps.setDate(4, (java.sql.Date) inner.getData());
			ps.setString(5, inner.getCartao());
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
	
	public void delete(Inner inner) {
		Connection conn = ConnectionFactory.getConnection();
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("delete \"tb_INNER\" where"
					+ " \"NumInner\"=? and \"Tipo\"=? and \"Data\"=? and \"Cartao\"=?");
			ps.setInt(1, inner.getNumInner());
			ps.setString(2, inner.getTipo());
			ps.setDate(3, (java.sql.Date) inner.getData());
			ps.setString(4, inner.getCartao());
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
