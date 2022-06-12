package net.dzikoysk.presenceofmind.shared;

public class Unsafe {

    public static <R> R cast(Object object) {
        //noinspection unchecked
        return (R) object;
    }

}
