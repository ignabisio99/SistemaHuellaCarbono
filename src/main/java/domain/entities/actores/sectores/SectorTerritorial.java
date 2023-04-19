package domain.entities.actores.sectores;

import domain.entities.actores.Actor;

import java.time.YearMonth;
import java.util.Map;

public abstract class SectorTerritorial extends Actor {

    public abstract Map<String, Double> composicion(YearMonth fecha);

    public abstract String getNombre();

}