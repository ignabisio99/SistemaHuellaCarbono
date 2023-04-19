package domain.entities.importacionDeDatos;

import domain.entities.huellaDeCarbono.FactorDeEmision;
import domain.entities.importacionDeDatos.adapters.AdapterLectorExcel;
import domain.entities.importacionDeDatos.adapters.AdapterLectorExcelApachePOI;
import domain.entities.actores.organizaciones.Organizacion;
import lombok.Setter;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class LectorExcel {
    @Setter
    public static AdapterLectorExcel adapter;

    static {
        adapter = new AdapterLectorExcelApachePOI();
    }

    public static void setFactoresDeEmision(List<FactorDeEmision> factoresDeEmision) {
        adapter.setFactoresDeEmision(factoresDeEmision);
    }

    public void cargarExcel(Organizacion organizacion, String path) {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(path);
        adapter.leerArchivo(organizacion, in);
    }

    public static void cargarExcel(Organizacion organizacion, Request request) {
        try {
            request.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
            InputStream in = request.raw().getPart("uploaded_file").getInputStream();
            adapter.leerArchivo(organizacion, in);
        } catch (IOException | ServletException e) {
            throw new RuntimeException(e);
        }
    }
}
