package at.renehollander.chat.util;

public interface CallbackWithError<E, D> {

    void call(E e, D data);

}
