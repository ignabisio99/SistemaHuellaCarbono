package domain.entities.importacionDeDatos.adapters;

import domain.entities.huellaDeCarbono.FactorDeEmision;
import domain.entities.importacionDeDatos.actividades.Actividad;
import domain.entities.importacionDeDatos.consumos.Consumo;
import domain.entities.importacionDeDatos.consumos.ConsumoLogistica;
import domain.entities.importacionDeDatos.consumos.ConsumoSimple;
import domain.entities.importacionDeDatos.consumos.TipoDeConsumo;

import static domain.entities.importacionDeDatos.actividades.TipoDeActividad.*;

import domain.entities.actores.organizaciones.Organizacion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import spark.Request;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import java.io.IOException;
import java.io.InputStream;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AdapterLectorExcelApachePOI implements AdapterLectorExcel {
    private List<FactorDeEmision> factoresDeEmision;

    @Override
    public void setFactoresDeEmision(List<FactorDeEmision> factoresDeEmision) {
        this.factoresDeEmision = factoresDeEmision;
    }

    @Override
    public void leerArchivo(Organizacion organizacion, InputStream in) {
        assert in != null;
        try {
            XSSFWorkbook workbook = new XSSFWorkbook(in);
            Iterator<Row> iteradorFilas = workbook.getSheetAt(0).iterator();
            iteradorFilas.next(); // encabezados
            while(iteradorFilas.hasNext()) organizacion.agregarActividad(leerFila(iteradorFilas));
            in.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Actividad leerFila(Iterator<Row> iteradorFilas) {
        Iterator<Cell> iteradorCeldas = iteradorFilas.next().cellIterator();
        String tipoDeActividad = iteradorCeldas.next().getStringCellValue();

        Consumo consumo;

        if (tipoDeActividad.equals(LOGISTICA_DE_PRODUCTOS_Y_RESIDUOS.toString())) {
            consumo = new ConsumoLogistica();
            for (int i = 0; i < 3; i++) {
                ((ConsumoLogistica) consumo).agregarConsumo(leerConsumo(iteradorCeldas));
                iteradorCeldas = iteradorFilas.next().cellIterator();
                iteradorCeldas.next();
            }
            ((ConsumoLogistica) consumo).agregarConsumo(leerConsumo(iteradorCeldas));
        }
        else
            consumo = leerConsumo(iteradorCeldas);

        return new Actividad(
                tipoDeActividad,
                consumo,
                iteradorCeldas.next().getStringCellValue(),
                leerPerDeImputacion(iteradorCeldas.next())
        );
    }

    private ConsumoSimple leerConsumo(Iterator<Cell> iteradorCeldas) {
        TipoDeConsumo tipoDeConsumo = TipoDeConsumo.valueOf(iteradorCeldas.next().getStringCellValue());

        Cell celdaValor = iteradorCeldas.next();
        celdaValor.setCellType(CellType.STRING);
        String valorConsumo = celdaValor.getStringCellValue();

        Optional<FactorDeEmision> factorDeEmision =
                factoresDeEmision.stream().filter(f -> f.getTipoDeConsumo() == tipoDeConsumo).findFirst();;

        return factorDeEmision
                .map(f -> new ConsumoSimple(tipoDeConsumo, valorConsumo, f))
                .orElseGet(() -> new ConsumoSimple(tipoDeConsumo, valorConsumo));
    }

    private YearMonth leerPerDeImputacion(Cell celda) {
        switch (celda.getCellType()) {
            case STRING:
                return YearMonth.parse(celda.getStringCellValue(), DateTimeFormatter.ofPattern("MM/yyyy"));
            case NUMERIC:
                return YearMonth.now().withYear((int) celda.getNumericCellValue());
        }
        return null;
    }
}