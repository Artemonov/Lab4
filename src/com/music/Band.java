package com.music;

import java.util.Objects;

public class Band {
    private String name;
    private String head;

    public Band(String name, String head) {
        this.name = name;
        this.head = head;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Band band = (Band) o;
        return Objects.equals(name, band.name) && Objects.equals(head, band.head);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, head);
    }

    @Override
    public String toString() {
        return "Band{" +
                "name='" + name + '\'' +
                ", head='" + head + '\'' +
                '}';
    }
}
