package dot.empire.counter;

import android.support.annotation.NonNull;

/**
 * Counter. Created by siD on 23 Jan 2018.
 *
 * @author Matthew Van der Bijl
 */
public final class Counter implements Comparable<Counter> {

    public long value;
    private String name;

    public Counter(String name) {
        this.name = name;
        this.value = 0l;
    }


    @Override
    public int compareTo(@NonNull final Counter other) {
        return getName().compareTo(other.getName());
    }

    @Override
    public String toString() {
        return "Counter{" +
                "name='" + name + '\'' +
                ", value=" + value +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Counter counter = (Counter) o;

        if (getValue() != counter.getValue()) return false;
        return getName() != null ? getName().equals(counter.getName()) : counter.getName() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (int) (getValue() ^ (getValue() >>> 32));
        return result;
    }
}
