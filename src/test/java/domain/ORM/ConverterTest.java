package domain.ORM;

import db.EntityManagerHelper;
import domain.entities.huellaDeCarbono.HuellaDeCarbono;
import domain.entities.importacionDeDatos.actividades.Periodicidad;
import org.junit.Assert;
import org.junit.Test;

import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

//TODO: revisar tests
public class ConverterTest {
    @Test
    public void calendarADate() {
        HuellaDeCarbono huellaDeCarbono = new HuellaDeCarbono(0.0, Periodicidad.MENSUAL, YearMonth.of(2022, 3));
        HuellaDeCarbono otraHuellaDeCarbono = new HuellaDeCarbono(0.0, Periodicidad.ANUAL, YearMonth.now().withYear(2021));

        EntityManagerHelper.beginTransaction();
        EntityManagerHelper.persist(huellaDeCarbono);
        EntityManagerHelper.persist(otraHuellaDeCarbono);
        EntityManagerHelper.commit();
    }

    @Test
    public void dateACalendar() {
        HuellaDeCarbono huellaDeCarbono = (HuellaDeCarbono) EntityManagerHelper
                .getEntityManager().createQuery("from HuellaDeCarbono where id=1").getSingleResult();
        HuellaDeCarbono otraHuellaDeCarbono = (HuellaDeCarbono) EntityManagerHelper
                .getEntityManager().createQuery("from HuellaDeCarbono where id=2").getSingleResult();

        System.out.println(huellaDeCarbono.getPerDeImputacion().format(DateTimeFormatter.ofPattern("MM/yyyy")));

        Assert.assertEquals(2022, huellaDeCarbono.getPerDeImputacion().getYear());
        Assert.assertEquals(3, huellaDeCarbono.getPerDeImputacion().getMonthValue());
        Assert.assertEquals(2021, otraHuellaDeCarbono.getPerDeImputacion().getYear());
    }
}
