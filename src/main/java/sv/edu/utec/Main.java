package sv.edu.utec;

import sv.edu.utec.api.ProveedorAPI;
import sv.edu.utec.datos.ProductoDAO;
import sv.edu.utec.servicio.SincronizacionService;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class Main {
    public static void main(String[] args) {
        // URL de la base de datos H2 (archivo local en la carpeta del proyecto)
        String urlDb = "jdbc:h2:./db/inventario";

        try {
            // 1. Inicializar la tabla de productos si no existe
            try (Connection conn = DriverManager.getConnection(urlDb);
                 Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS productos (" +
                        "id INT PRIMARY KEY, " +
                        "nombre VARCHAR(50), " +
                        "stock INT)";
                stmt.execute(sql);
            }

            // 2. Instanciar dependencias
            ProveedorAPI proveedor = new ProveedorAPI();
            ProductoDAO dao = new ProductoDAO(urlDb);
            SincronizacionService servicio = new SincronizacionService(proveedor, dao);

            // 3. Ejecutar la sincronización (por ejemplo, sincronizar 10 productos)
            System.out.println("Iniciando sincronización con la API...");
            int[] resultado = servicio.sincronizar(10);

            // 4. Mostrar resultados en consola
            System.out.println("¡Sincronización completada con éxito!");
            System.out.println("Nuevos productos insertados: " + resultado[0]);
            System.out.println("Productos actualizados: " + resultado[1]);

        } catch (Exception e) {
            System.err.println("Ocurrió un error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }
}