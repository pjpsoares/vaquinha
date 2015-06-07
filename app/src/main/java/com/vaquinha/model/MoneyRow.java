package com.vaquinha.model;

public class MoneyRow {

    private final long id;
    private float value;
    private String description;
    private String date;

    public MoneyRow(long id, float value, String description, String date) {
        this.id = id;
        this.value = value;
        this.description = description;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyRow moneyRow = (MoneyRow) o;

        if (id != moneyRow.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (id ^ (id >>> 32));
    }

    @Override
    protected MoneyRow clone() {
        return new MoneyRow(this.id, this.value, this.description, this.date);
    }
}
