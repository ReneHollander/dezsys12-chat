package at.renehollander.mobileapp;

public interface Callback<E, D> {

    void call(E e, D data);

}
