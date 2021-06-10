package com.music;

import java.util.Objects;

public class Tour {

    private String city;
    private int year;
    private long concertNumber;
    private Band band;

    public Tour(String city, int year, long concertNumber) {
        this.city = city;
        this.year = year;
        this.concertNumber = concertNumber;
    }

    public Band getBand() {
        return band;
    }

    public void setBand(Band band) {
        this.band = band;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public long getConcertNumber() {
        return concertNumber;
    }

    public void setConcertNumber(long concertNumber) {
        this.concertNumber = concertNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tour tour = (Tour) o;
        return year == tour.year && concertNumber == tour.concertNumber && Objects.equals(city, tour.city) && Objects.equals(band, tour.band);
    }

    @Override
    public int hashCode() {
        return Objects.hash(city, year, concertNumber, band);
    }

    @Override
    public String toString() {
        return "Tour{" +
                "city=" + city +
                ", year=" + year +
                ", concertNumber='" + concertNumber + '\'' +
                ", band=" + band +
                '}';
    }
}
