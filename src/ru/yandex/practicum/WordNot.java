package ru.yandex.practicum;

public class WordNot extends Exception {
    public WordNot() {
        super("Не найдено подходящих слов в словаре");
    }
}
