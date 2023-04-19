package domain.entities.importacionDeDatos.adapters;

import domain.entities.huellaDeCarbono.FactorDeEmision;
import domain.entities.actores.organizaciones.Organizacion;
import spark.Request;

import java.io.InputStream;
import java.util.List;

public interface AdapterLectorExcel {
    void setFactoresDeEmision(List<FactorDeEmision> factoresDeEmision);
    void leerArchivo(Organizacion organizacion, InputStream in);
}
