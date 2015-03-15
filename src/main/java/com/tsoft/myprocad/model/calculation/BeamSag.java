package com.tsoft.myprocad.model.calculation;

/** Предельные прогибы в долях пролета балки, в см */
public enum BeamSag {
    FLOOR_LAP(1, "Междуэтажные перекрытия, 1/250", 1.0/250.0),
    ATTIC_LAP(2, "Чердачные перекрытия, 1/200", 1.0/200.0),
    RUN(3, "Прогоны, стропильные ноги, 1/200", 1.0/200.0),
    LATHING(4, "Обрешетка, настилы, 1/500", 1.0/500.0),
    GIRTH_RAIL(5, "Ригели (ендовы), накосные ноги, 1/400", 1.0/400.0);

    private int id;
    private String text;
    private double f;

    BeamSag(int id, String text, double f) {
        this.id = id;
        this.text = text;
        this.f = f;
    }

    public int getId() { return id; }

    public double getF() { return f; }

    @Override
    public String toString() { return text; }

    public static BeamSag findById(int id) {
        for (BeamSag sag : values()) {
            if (sag.getId() == id) return sag;
        }
        return null;
    }
}
