package dot.empire.counter;

import android.graphics.drawable.Drawable;

/**
 * Single selectable settings option. Added to settings list view. Created by siD on 23 Jan 2018.
 *
 * @author Matthew Van der Bijl
 */
public final class SettingOption {

    private final String name;
    private final Drawable icon;

    public SettingOption(String name, Drawable icon) {
        this.name = name.trim();
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public Drawable getIcon() {
        return icon;
    }

    @Override
    public String toString() {
        return "SettingOption{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SettingOption setting = (SettingOption) o;

        if (getName() != null ? !getName().equals(setting.getName()) : setting.getName() != null)
            return false;
        return getIcon() != null ? getIcon().equals(setting.getIcon()) : setting.getIcon() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getIcon() != null ? getIcon().hashCode() : 0);
        return result;
    }
}
