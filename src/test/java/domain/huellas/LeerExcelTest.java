package domain.huellas;

import domain.BaseTest;
import domain.entities.importacionDeDatos.actividades.Actividad;
import domain.entities.importacionDeDatos.consumos.Consumo;
import domain.entities.importacionDeDatos.consumos.ConsumoLogistica;
import domain.entities.importacionDeDatos.consumos.ConsumoSimple;

import org.junit.Assert;
import org.junit.Test;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class LeerExcelTest extends BaseTest {
    @Test
    public void imprimirDatos() {
        List<Actividad> actividades = organizacionA.getListadoDeImportaciones();
        for (Actividad actividad : actividades)
            imprimirActividad(actividad);
        Assert.assertEquals(14, actividades.size());
    }

    @Test
    public void huella2021() {
        Assert.assertEquals(HC2021, organizacionA.calcularHuellaAnual(2021), 0);
    }

    @Test
    public void huella2020() {
        Assert.assertEquals(HC2020, organizacionA.calcularHuellaAnual(2020), 0);
    }

    protected void imprimirActividad(Actividad actividad) {
        System.out.println("Actividad: " + actividad.getTipoDeActividad());
        imprimirConsumo(actividad.getConsumo());
        System.out.println("Periodicidad: " + actividad.getPeriodicidad());
        System.out.println("Periodo de imputación: " + actividad.getPerDeImputacion().format(DateTimeFormatter.ofPattern("MM/yyyy")));

        String alcance;
        if(actividad.getAlcance() == 1) alcance = "EMISIONES_DIRECTAS";
        else alcance = "EMISIONES_INDIRECTAS";

        System.out.println("Alcance: " + alcance);
        System.out.println();
    }

    private void imprimirConsumo(Consumo consumo) {
        if (consumo.getClass() == ConsumoSimple.class)
            imprimirConsumoSimple(consumo);
        else
            for (Consumo consumoSimple : ((ConsumoLogistica) consumo).getConsumos())
                imprimirConsumoSimple(consumoSimple);
    }

    private void imprimirConsumoSimple(Consumo consumoSimple) {
        System.out.println("\nTipoDeConsumo: " + consumoSimple.getNombreConsumo());
        System.out.println("\tValor: " + ((ConsumoSimple) consumoSimple).getValor());
        System.out.println("\tUnidad: " + ((ConsumoSimple) consumoSimple).getUnidad());
        System.out.println();
    }
}
