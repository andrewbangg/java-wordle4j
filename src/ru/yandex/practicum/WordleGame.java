package ru.yandex.practicum;

import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private final String answer;

    private int steps = 6;

    private final WordleDictionary dictionary;

    private final List<String> historyWord = new ArrayList<>();

    private final HashMap<Integer, Character> wordAndIndex = new HashMap<>();

    private final HashMap<Integer, Character> wordAnotherPlace = new HashMap<>();

    private final HashSet<Character> extra = new HashSet<>();


    public WordleGame(WordleDictionary dictionary) {
        this.dictionary = dictionary;
        this.answer = dictionary.getRandomWord();
    }

    public void inputWord(String inputWord) {
        historyWord.add(inputWord);
        clue(inputWord);
    }


    public boolean contains(String computerRandomWord, String inputWord) {
        return computerRandomWord.equals(inputWord);
    }

    public void clue(String inputWord) {
        if (inputWord.length() != 5) {
            return;
        }

        // Создаем копию ответа для отслеживания использованных букв
        StringBuilder remainingAnswer = new StringBuilder(answer);
        char[] result = new char[5];

        // Сначала отмечаем точные совпадения (+)
        for (int i = 0; i < 5; i++) {
            if (inputWord.charAt(i) == answer.charAt(i)) {
                result[i] = '+';
                remainingAnswer.setCharAt(i, ' '); // Убираем использованную букву
            }
        }

        // Затем ищем буквы в других позициях (^)
        for (int i = 0; i < 5; i++) {
            if (result[i] == '+') {
                continue;
            }

            char currentChar = inputWord.charAt(i);
            int foundIndex = -1;

            // Ищем эту букву в оставшихся буквах ответа
            for (int j = 0; j < 5; j++) {
                if (remainingAnswer.charAt(j) == currentChar) {
                    foundIndex = j;
                    break;
                }
            }

            if (foundIndex != -1) {
                result[i] = '^';
                remainingAnswer.setCharAt(foundIndex, ' ');
                // Обновляем информацию для подсказок
                wordAnotherPlace.put(i, currentChar);
            } else {
                result[i] = '-';
                // Добавляем букву в "запрещенные", если она не встречается в ответе нигде
                if (answer.indexOf(currentChar) == -1) {
                    extra.add(currentChar);
                }
            }
        }

        // Обновляем точные совпадения для подсказок
        for (int i = 0; i < 5; i++) {
            if (inputWord.charAt(i) == answer.charAt(i)) {
                wordAndIndex.put(i, inputWord.charAt(i));
                wordAnotherPlace.remove(i);
            }
        }

        System.out.println(inputWord);
        System.out.println(new String(result));
    }


    public Integer leftStep() {
        return steps;
    }

    public void startGame(Scanner scanner) {
        boolean isGame = true;

        while (isGame && steps > 0) {
            System.out.println("Осталось попыток: " + steps);
            System.out.println("Введите слово (5 букв) или нажмите Enter для подсказки:");

            String userInput = scanner.nextLine();

            // Пустая строка - подсказка (не тратит попытку)
            if (userInput.isEmpty()) {
                searchClue();
                continue;
            }

            // Проверка длины
            if (userInput.length() != 5) {
                System.out.println("Слово должно состоять из 5 букв!");
                continue;
            }

            // Проверка наличия в словаре
            if (!dictionary.contains(userInput)) {
                System.out.println("Такого слова нет в словаре!");
                continue;
            }


            // Проверка на победу
            if (contains(answer, userInput)) {
                clue(userInput);
                System.out.println("Поздравляем! Вы отгадали слово \"" + answer + "\"!");
                break;
            }

            // Обычный ход - показываем подсказку
            clue(userInput);

            // Проверка на поражение
            if (steps == 0) {
                System.out.println("Game over! Загаданное слово: " + answer);
                break;
            }
            steps--;
        }
    }

    public boolean searchWordInWordAndIndex(String word) {
        if (wordAndIndex.isEmpty()) {
            return true;
        }

        for (Map.Entry<Integer, Character> entry : wordAndIndex.entrySet()) {
            Integer index = entry.getKey();
            Character ch = entry.getValue();

            if (word.charAt(index) != ch) {
                return false;
            }
        }
        return true;
    }

    public boolean searchInExtra(String word) {
        if (extra.isEmpty()) {
            return true;
        }
        for (Character ch : extra) {
            if (word.contains(ch.toString())) {
                return false;
            }
        }
        return true;
    }

    public boolean searchWordAnotherPlace(String word) {
        if (wordAnotherPlace.isEmpty()) {
            return true;
        }

        for (Map.Entry<Integer, Character> entry : wordAnotherPlace.entrySet()) {
            Integer index = entry.getKey();
            Character ch = entry.getValue();

            if (!word.contains(ch.toString())) {
                return false;
            }
            if (word.charAt(index) == ch) {
                return false;
            }
        }
        return true;
    }

    public void searchClue() {
        for (String word : dictionary.getWords()) {
            if (historyWord.contains(word)) {
                continue;
            }

            if (searchInExtra(word) &&
                    searchWordInWordAndIndex(word) &&
                    searchWordAnotherPlace(word)) {

                System.out.println("Компьютер подсказывает: " + word);
                inputWord(word);
                return;
            }
        }
        System.out.println("Не найдено подходящих слов в словаре");
    }
}