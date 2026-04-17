package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {


    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("Input name file:");
            String fileName = scanner.nextLine();
            WordleDictionaryLoader loader = new WordleDictionaryLoader();
            WordleDictionary dictionary = loader.load(fileName);
            WordleGame wordleGame = new WordleGame(dictionary);

            System.out.println("Game start! Good luck!");
            wordleGame.startGame(scanner);
        } catch (Exception e) {
            logError(e);
        }

    }

    private static void logError(Exception e) {
        try (FileWriter fw = new FileWriter("error.log", true);
             PrintWriter pw = new PrintWriter(fw)) {

            pw.println("ERROR: " + e.getMessage());
            pw.println("Stack trace:");
            e.printStackTrace(pw);
            System.out.println("Произошла ошибка.");

        } catch (IOException ioException) {
            System.err.println("Не удалось записать ошибку в лог-файл: " + ioException.getMessage());
            e.printStackTrace(); // Выводим в консоль как запасной вариант
        }
    }
}





