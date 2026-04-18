package ru.yandex.practicum;


public class UserInputNotFiveWord extends Exception {
    public UserInputNotFiveWord(String userInput) {
        super(userInput);
    }
}
