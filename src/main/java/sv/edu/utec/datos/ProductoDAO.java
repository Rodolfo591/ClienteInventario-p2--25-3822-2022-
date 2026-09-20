package sv.edu.utec.datos;

import sv.edu.utec.modelo.Producto;

import java.sql.*;

public class ProductoDAO {
    private final String urlConexion;

    public ProductoDAO(String urlConexion) {
        this.urlConexion = urlConexion;
    }

    private Connection conectar() throws SQLException {
        return DriverManager.getConnection(urlConexion);
    }

    public boolean existe(int id) throws SQLException {
        String sql = "SELECT COUNT(*) FROM productos WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    public void insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO productos (id, nombre, stock) VALUES (?, ?, ?)";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, producto.getId());
            pstmt.setString(2, producto.getNombre());
            pstmt.setInt(3, producto.getStock());
            pstmt.executeUpdate();
        }
    }

    public void actualizar(Producto producto) throws SQLException {
        String sql = "UPDATE productos SET nombre = ?, stock = ? WHERE id = ?";
        try (Connection conn = conectar();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, producto.getNombre());
            pstmt.setInt(2, producto.getStock());
            pstmt.setInt(3, producto.getId());
            pstmt.executeUpdate();
        }
    }
}