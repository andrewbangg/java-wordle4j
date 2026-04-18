package ru.yandex.practicum;

import java.io.*;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {
    public WordleDictionary load(String filename) throws IOException {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            while (reader.ready()) {
                String word = reader.readLine().trim().toLowerCase().replace('ё', 'е');
                if (word.length() == 5) {
                    words.add(word);
                }
            }
        }
        return new WordleDictionary(words);
    }
}