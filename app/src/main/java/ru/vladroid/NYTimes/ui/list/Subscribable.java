package ru.vladroid.NYTimes.ui.list;

/**
 * Примитивная реализация подписки на загрузку данных
 */
public interface Subscribable<T> {
    void onStateChanged(LoadingState state);
    void onNewsLoad(T data);
    void onLoadingFailed(Exception e);
}
