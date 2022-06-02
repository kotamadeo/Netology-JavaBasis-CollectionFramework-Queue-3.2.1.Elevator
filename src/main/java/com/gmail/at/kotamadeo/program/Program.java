package com.gmail.at.kotamadeo.program;

import com.gmail.at.kotamadeo.utils.Utils;

import java.util.Deque;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

public class Program {
    private final Scanner scanner = new Scanner(System.in);
    private final Deque<Integer> floors = new LinkedList<>();
    private int waitDoorsInSeconds = 10;
    private int waitMoveInSeconds = 5;
    private int totalSeconds;

    public void start() {
        String input;
        String[] allInput;
        var floorsInBuild = 25;
        floors.offer(0);
        totalSeconds = waitMoveInSeconds + waitDoorsInSeconds;
        while (true) {
            try {
                printMenu();
                input = scanner.nextLine();
                if ("выход".equalsIgnoreCase(input) || "0".equals(input)) {
                    scanner.close();
                    break;
                } else {
                    var operationNumber = Integer.parseInt(input);
                    switch (operationNumber) {
                        case 1:
                            System.out.println(Utils.ANSI_BLUE + "Чтобы задать условия работы программы введите " +
                                    "через пробел: количество этажей, время следования лифта между этажами и время " +
                                    "закрытия дверей лифта." + Utils.ANSI_RESET);
                            allInput = scanner.nextLine().split(" ", 3);
                            floorsInBuild = Integer.parseInt(allInput[0]);
                            waitMoveInSeconds = Integer.parseInt(allInput[1]);
                            waitDoorsInSeconds = Integer.parseInt(allInput[2]);
                            System.out.printf("%sКоличество этажей: %s, время следования лифта: %s сек., " +
                                            "время закрытия дверей лифта: %s сек.%s%n", Utils.ANSI_GREEN, floorsInBuild,
                                    waitMoveInSeconds, waitDoorsInSeconds, Utils.ANSI_RESET);
                            break;
                        case 2:
                            System.out.printf("%sСейчас вы находитесь на %s этаже. Введите этаж:%s%n",
                                    Utils.ANSI_BLUE, floors.peekLast(), Utils.ANSI_RESET);
                            input = scanner.nextLine();
                            if (Integer.parseInt(input) >= 0 && Integer.parseInt(input) <= floorsInBuild) {
                                floors.offer(Integer.parseInt(input));
                            } else {
                                System.out.printf("%sВ здании всего %s этажей, а вы ввели %s!%s%n", Utils.ANSI_RED,
                                        floorsInBuild, input, Utils.ANSI_RESET);
                            }
                            break;
                        case 3:
                            floorsMoved(floors);
                            break;
                        default:
                            System.out.println(Utils.ANSI_RED + "Вы ввели неверный номер операции!" + Utils.ANSI_RESET);
                    }
                }
            } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                System.out.println(Utils.ANSI_RED + "Ошибка ввода!" + Utils.ANSI_RESET);
            }
        }
    }

    private static void printMenu() {
        System.out.println(Utils.ANSI_YELLOW + "Добро пожаловать! Эта программа эмулирует работу лифта." +
                Utils.ANSI_RESET);
        System.out.println(Utils.ANSI_PURPLE + "Возможные команды программы:" + Utils.ANSI_RESET);
        System.out.println("0 или выход: выход из программы.");
        System.out.println("1: задать условия работы программы.");
        System.out.println("2: выбрать этаж.");
        System.out.println("3: вывести информацию о пройденных этажах и времени.");
    }

    private void floorsMoved(Queue<Integer> floors) {
        var previousFloor = -1;
        var currentFloor = 0;
        System.out.printf("%sЛифт проследовал по следующим этажам:%s%n", Utils.ANSI_GREEN, Utils.ANSI_RESET);
        while (!floors.isEmpty()) {
            if (previousFloor != -1) {
                currentFloor = floors.peek();
                totalSeconds += (Math.abs(currentFloor - previousFloor)) * waitMoveInSeconds;
                if (previousFloor == currentFloor) {
                    System.out.println(Utils.ANSI_BLUE + "Никуда не едем, но двери закрылись!" +
                            Utils.ANSI_RESET);
                }
                totalSeconds += waitDoorsInSeconds;
            }
            var isElevatorUp = previousFloor < currentFloor;
            previousFloor = floors.peek();
            System.out.println(Utils.ANSI_CYAN + (isElevatorUp ? floors.poll() + " этаж.\n" + "↑" :
                    floors.poll() + " этаж.\n" + "↓") + Utils.ANSI_RESET);
            System.out.println(Utils.ANSI_CYAN + "Общее время в пути: " + totalSeconds + " сек." + Utils.ANSI_RESET);
        }
        if (floors.isEmpty()) {
            totalSeconds = 0;
        }
    }
}
