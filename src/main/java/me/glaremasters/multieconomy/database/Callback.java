package me.glaremasters.multieconomy.database;

/**
 * Created by GlareMasters on 5/31/2018.
 */
public interface Callback<T, E extends Exception> {

    void call(T result, E exception);
}
