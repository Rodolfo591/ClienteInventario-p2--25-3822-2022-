package sv.edu.utec.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import sv.edu.utec.modelo.Producto;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ProveedorAPI {
    public List<Producto> obtenerProductos(int limite) throws IOException, InterruptedException {
        String url = "https://dummyjson.com/products?limit=" + limite;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        ObjectMapper mapper = new ObjectMapper();
        RespuestaProductos respuesta = mapper.readValue(response.body(), RespuestaProductos.class);

        List<Producto> productosFinales = new ArrayList<>();
        if (respuesta != null && respuesta.getProducts() != null) {
            for (ProductoApi pApi : respuesta.getProducts()) {
                String nombreCorto = pApi.getTitle();
                if (nombreCorto != null && nombreCorto.length() > 50) {
                    nombreCorto = nombreCorto.substring(0, 50);
                }
                productosFinales.add(new Producto(pApi.getId(), nombreCorto, pApi.getStock()));
            }
        }
        return productosFinales;
    }
}