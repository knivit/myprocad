package com.tsoft.myprocad.report;

public class MaterialRecord {
    public String size;

    private long weight;
    private long volume;
    private long price;

    public long amount;

    public MaterialRecord(String size) { this.size = size; }

    public void add(double weight, double volume, double price) {
        //support 3 digits after the comma
        this.weight += Math.ceil(weight * 1000);
        this.volume += Math.ceil(volume * 1000);
        this.price += Math.ceil(price);
    }

    public float getWeight() { return weight / 1000f; }

    public float getVolume() { return volume / 1000f; }

    public long getPrice() { return price; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MaterialRecord account = (MaterialRecord) o;

        if (!size.equals(account.size)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return size.hashCode();
    }
}
