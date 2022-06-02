# **Задача № 1 Лифт**

## **Цель**:
1. Создать программу, которая считывает данные из консоли, ожидая ввода номера этажа. После ввода каждого числа (номера этажа) добавляет значение в очередь дальнейшего перемещения лифта. Как только пользователь введет 0, программа должна последовательно вывести список всех этажей, на которых лифт делал остановки, в формате: "этаж 1 -> этаж 22 -> этаж 0". Если пользователь ввел этаж вне диапазона 0-25, проигнорировать ввод таких данных. Для реализации хранения введенных пользователем этажей отлично подойдет структура на основе интерфейса очередь ```Queue```. По мере ввода мы сможем добавлять в конец очереди новые значения.
1. Иметь возможность ввода номера этажей;
2. Иметь возможность получения информации о посещенных этажах в порядке добавления в формате: "этаж 1 -> этаж 22 -> этаж 0".


### *Пример*:
``` Пример 1
1. В бесконечном цикле считывать номера этажей до тех пор, пока не будет введен нулевой этаж.

2. Перед каждым вводом запросить номер следующего этажа - вывести сообщение "Ожидаю ввода этажа: (для завершения введите 0)", чтобы пользователь понимал, что ему вводить.

3. Добавить проверку: если введенный этаж входит в диапазон допустимых значений 0-25, продолжить работу программы; а если не входит, то вывести на экран сообщение: "Такого этажа нет в доме".

4. Добавить проверку: если введенный этаж = 0, нужно выйти из цикла "чтение данных из консоли".

5. Если введенный этаж входит в диапазон 0-25, то нужно добавить значение в очередь и запросить ввод данных повторно.

6. Для вывода информации на экран, используя метод интерфейса Queue poll, прочитать последовательно все значения и вывести на экран.
```

### **Моя реализация**:
1. Реализация осуществлена в парадигме ООП.
2. Создал структуру классов:

* **Program** - класс, отвечающий за запуск программы, путем инициирования метода *start()* с инициированием внутри себя
  вспомогательных ```void``` методов: 
  * *printMenu()* - выводит меню команд программы на экран;
  * *floorsMoved()* - моделирует работу лифта.  

#### Класс **Program**:
``` java
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
```

2. Использовал кодирование цвета текста (ANSI).

#### Класс **Utils**:
``` java
public class Utils {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    public static void printDelim() {
        System.out.println(ANSI_GREEN + "*********************************************" + ANSI_RESET);
    }
}
```

3. Использовал ```try-catch```, чтобы избежать падение программы в исключения.

#### Метод *main()* в классе **Main**:
``` java
public class Main {
    public static void main(String[] args) {
        var program = new Program();
        program.start();
    }
}
```

## *Вывод в консоль*:

* меню:
``` 
Добро пожаловать! Эта программа эмулирует работу лифта.
Возможные команды программы:
0 или выход: выход из программы.
1: задать условия работы программы.
2: выбрать этаж.
3: вывести информацию о пройденных этажах и времени.
```

* Демонстрация работы:
```
1
Чтобы задать условия работы программы введите через пробел: количество этажей, время следования лифта между этажами и время закрытия дверей лифта.
25 10 4
Количество этажей: 25, время следования лифта: 10 сек., время закрытия дверей лифта: 4 сек.

2
Сейчас вы находитесь на 0 этаже. Введите этаж:
2

2
Сейчас вы находитесь на 2 этаже. Введите этаж:
1


2
Сейчас вы находитесь на 1 этаже. Введите этаж:
4

2
Сейчас вы находитесь на 4 этаже. Введите этаж:
6

2
Сейчас вы находитесь на 6 этаже. Введите этаж:
8

2
Сейчас вы находитесь на 8 этаже. Введите этаж:
9

2
Сейчас вы находитесь на 9 этаже. Введите этаж:
11

2
Сейчас вы находитесь на 11 этаже. Введите этаж:
1

3
Лифт проследовал по следующим этажам:
0 этаж.
↑
Общее время в пути: 15 сек.
2 этаж.
↑
Общее время в пути: 39 сек.
1 этаж.
↓
Общее время в пути: 53 сек.
4 этаж.
↑
Общее время в пути: 87 сек.
6 этаж.
↑
Общее время в пути: 111 сек.
8 этаж.
↑
Общее время в пути: 135 сек.
9 этаж.
↑
Общее время в пути: 149 сек.
11 этаж.
↑
Общее время в пути: 173 сек.
1 этаж.
↓
```