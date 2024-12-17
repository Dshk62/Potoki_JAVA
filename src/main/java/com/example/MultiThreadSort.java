package com.example;

import java.io.*;
import java.util.Arrays;
import java.util.Random;
import java.util.Comparator;


public class MultiThreadSort {
    public static void main(String[] args) {
        File inputFile = new File("input.txt");
        File outputFile = new File("output.txt");

        Integer[] array;
        boolean ascending = true;

        // Чтение данных из файла
        try (BufferedReader reader = new BufferedReader(new FileReader(inputFile))) {
            String sizeLine = reader.readLine();
            if (sizeLine == null) {
                System.err.println("Ошибка: файл пустой или отсутствует размерность массива.");
                return;
            }

            int size = Integer.parseInt(sizeLine.trim());
            if (size <= 0) {
                System.out.println("Размерность должна быть положительным числом.");
                return;
            }

            // Генерация случайного массива чисел от 1 до size
            array = new Integer[size];
            Random random = new Random();
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(size) + 1;
            }
            System.out.println("Исходный массив: " + Arrays.toString(array));
            String sortOrderLine = reader.readLine();
            if (sortOrderLine == null) {
                System.err.println("Ошибка: отсутствует порядок сортировки.");
                return;
            }

            String sortOrder = sortOrderLine.trim();
            if ("desc".equalsIgnoreCase(sortOrder)) {
                ascending = false;
            } else if (!"asc".equalsIgnoreCase(sortOrder)) {
                System.out.println("Некорректный ввод порядка сортировки. Ожидается 'asc' или 'desc'.");
                return;
            }

        } catch (IOException | NumberFormatException e) {
            System.err.println("Ошибка чтения из файла: " + e.getMessage());
            return;
        }

        // Создание и запуск потока Sort
        boolean finalAscending = ascending;
        Thread sortThread = new Thread(() -> {
            if (finalAscending) {
                Arrays.sort(array);
            } else {
                Comparator<Integer> descendingComparator = (a, b) -> Integer.compare(b, a);
                Arrays.sort(array, descendingComparator);
            }
        });

        sortThread.start();

        try {
            // Ожидание завершения работы потока
            sortThread.join();
        } catch (InterruptedException e) {
            System.err.println("Сортировка прервана: " + e.getMessage());
            return;
        }

        // Запись отсортированного массива в файл и вывод на консоль
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            writer.write(Arrays.toString(array));
            writer.newLine();
            System.out.println("Отсортированный массив: " + Arrays.toString(array));
        } catch (IOException e) {
            System.err.println("Ошибка записи в файл: " + e.getMessage());
        }
    }
}