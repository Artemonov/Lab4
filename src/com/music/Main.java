package com.music;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws IOException {
        // write your code here
        Path path = Paths.get("src/com/music/files/database.txt");
        Pattern tourPattern = Pattern.compile("(?<tour>Tour:) (?<year>\\d{4}), (?<city>[A-ZА-ЯІЇ][a-zа-яії]+\\scity), (?<concertNumbers>\\d*\\sconcerts)", Pattern.CASE_INSENSITIVE);
        Pattern bandPattern = Pattern.compile("(?<band>Band:) (?<name>[\\w\\s]+ )(?<head>- [A-ZА-ЯІЇ][a-zа-яії]+)", Pattern.CASE_INSENSITIVE);
        List<Tour> tours = new ArrayList<>();
        readFromFile(path, tourPattern, bandPattern, tours);

        Scanner sc = new Scanner(System.in);
        while (true) {
            System.out.println("Виберіть дію:\n1 - Гастрольна поїздка з максимальною кількістю концертів; \n2 - Список гастрольних поїздок у певне місто; \n3 - Остання літера в прізвищі керівника; \n4 - Показати всі записи; \nq - Вихід; \ne - Редагування");
            switch (sc.nextLine()) {
                case "1": {
                    maxConcertNumberTour(sc, tours);
                    break;
                }
                case "2": {
                    listOfTourInTheSpecificCity(sc, tours);
                    break;
                }
                case "3": {
                    bandHeadLastLetterOfSurname(sc, tours);
                    break;
                }
                case "4": {
                    readFromFile(path, tourPattern, bandPattern, tours);
                    break;
                }
                case "q": {
                    return;
                }
                case "e": {
                    startCRUD(sc, tours, path);
                    break;
                }
                default: {
                    throw new IncorrectChoiceException("Неправильна дія! ");
                }
            }
        }
    }

    private static void startCRUD(Scanner sc, List<Tour> tours, Path path) {
        System.out.println("Виберіть дію:\n1 - Видалити запис; \n2 - Редагувати запис; \n3 - Додати новий запис; \nq - Вихід;");
        switch (sc.nextLine()) {
            case "1": {
                delete(sc, tours, path);
                System.out.println("Успішно видалено!");
                break;
            }
            case "2": {
                edit(sc, tours, path);
                break;
            }
            case "3": {
                save(sc, path);
                break;
            }
            default: {
                break;
            }
        }
    }

    private static void showAllWithNum(List<Tour> tours) {
        for (int i = 0; i < tours.size(); i++) {
            System.out.println((i + 1) + " " + tours.get(i).toString());
        }
    }

    private static void delete(Scanner sc, List<Tour> tours, Path path) {
        showAllWithNum(tours);
        System.out.println("Виберіть номер запису для видалення: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        tours.remove(index);
        save(tours, path);
    }

    private static void edit(Scanner sc, List<Tour> tours, Path path) {
        showAllWithNum(tours);
        System.out.println("Виберіть номер запису для редагування: ");
        int index = Integer.parseInt(sc.nextLine()) - 1;
        Tour tour = tours.get(index);
        System.out.println(tour);
        System.out.println("1 - Змінити назву групи; \n2 - Змінити прізвище лідера групи; \n3 - Змінити рік гастролей; \n4 - Змінити місто гастролей; \n5 - Змінити кількість концертів \n");
        switch (sc.nextLine()) {
            case "1": {
                System.out.println("Введіть нову назву групи");
                tour.getBand().setName(sc.nextLine());
                break;
            }
            case "2": {
                System.out.println("Введіть нове прізвище лідера групи");
                tour.getBand().setHead(sc.nextLine());
                break;
            }
            case "3": {
                System.out.println("Введіть новий рік гастролей");
                tour.setYear(Integer.parseInt(sc.nextLine()));
                break;
            }
            case "4": {
                System.out.println("Введіть нове місто гастролей");
                tour.setCity(sc.nextLine());
                break;
            }
            case "5": {
                System.out.println("Введіть нову кількість концертів");
                tour.setConcertNumber(Integer.parseInt(sc.nextLine()));
                break;
            }
        }
        tours.remove(index);
        tours.add(index, tour);
        save(tours, path);
        System.out.println("Зміни збережено!");
        System.out.println();
    }

    private static String mapToFileFormat(Tour wd) {
        StringBuilder sb = new StringBuilder();
        sb.append("Tour: ")
                .append(wd.getYear())
                .append(", ")
                .append(wd.getCity()).append(" city, ")
                .append(wd.getConcertNumber()).append(" concerts")
                .append("\r\n");
        sb.append("Band: ")
                .append(wd.getBand().getName())
                .append(" - ")
                .append(wd.getBand().getHead())
                .append("\r\n");
        sb.append("-------------------------------------------------------");

        return sb.toString();
    }

    private static void save(List<Tour> tours, Path path) {
        try {
            List<String> toursResult = tours.stream()
                    .map(Main::mapToFileFormat)
                    .collect(Collectors.toList());
            Files.write(path, toursResult, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save(Scanner sc, Path path) {
        System.out.println("Рік гастролей: ");
        Integer year = Integer.parseInt(sc.nextLine().trim());
        System.out.println("Місто проведення: ");
        String city = sc.nextLine();
        System.out.println("Кількість концертів: ");
        int concertNumber = Integer.parseInt(sc.nextLine());
        System.out.println("Назва групи ");
        String name = sc.nextLine();
        System.out.println("Прізвище керівника: ");
        String head = sc.nextLine();
        Tour tour = new Tour(city, year, concertNumber);
        tour.setBand(new Band(name, head));
        System.out.println(tour);
        try {
            Files.write(path, ("\r\n" + mapToFileFormat(tour)).getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
            System.out.println("Збережено!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println();
    }

    private static void bandHeadLastLetterOfSurname(Scanner sc, List<Tour> tours) {
        System.out.println("Введіть назву групи");
        String name = sc.nextLine();
        String finalName = name;
        Optional<Tour> tour = tours.stream()
                .filter(wd -> wd.getBand().getName().equals(finalName))
                .findFirst();
        if (tour.isPresent()) {
            String head = tour.get().getBand().getHead();
            System.out.println("Остання літера в прізвищі керівника: " + head.charAt(head.length() - 1));
        } else {
            System.out.println("Такої групи не існує");

        }
    }

    private static void listOfTourInTheSpecificCity(Scanner sc, List<Tour> tours) {
        System.out.println("Введіть назву міста");
        String сity = sc.nextLine();

        String finalСity = сity;
        List<Tour> collect = tours.stream()
                .filter(wd -> wd.getCity().equals(finalСity))
                .collect(Collectors.toList());
        if (collect.isEmpty())
            System.out.println("Не було гастрольних поїздок у дане місто");
        else {
            System.out.println("Список гастрольних поїздок у певне місто:\n");
            collect.forEach(System.out::println);
        }


        System.out.println();
    }

    private static void maxConcertNumberTour(Scanner sc, List<Tour> tours) {
        Tour tour = tours
                .stream().max(Comparator.comparingLong(Tour::getConcertNumber))
                .get();
        System.out.println("Гастрольна поїздка з максимальною кількістю концертів:\n" + tour);

        System.out.println();
    }

    private static void readFromFile(Path path, Pattern tourPattern, Pattern bandPattern, List<Tour> tours) throws IOException {
        List<String> lines = Files.readAllLines(path);
        Matcher matcher;
        int year = 0;
        String city = "";
        long concertNumbers = 0;
        String name = "";
        String head = "";
        for (String line : lines) {
            if (line.startsWith("Tour")) {
                year = 0;
                city = "";
                concertNumbers = 0;
                matcher = tourPattern.matcher(line);
                if (matcher.find()) {
                    year = Integer.parseInt(matcher.group("year"));
                    city = matcher.group("city").split("\\s")[0];
                    concertNumbers = Integer.parseInt(matcher.group("concertNumbers").split("\\s")[0]);
                }

            } else if (line.startsWith("Band")) {
                name = "";
                head = "";
                matcher = bandPattern.matcher(line);
                if (matcher.find()) {
                    name = matcher.group("name").trim();
                    head = matcher.group("head").split("- ")[1];
                }
            } else if (line.startsWith("-")) {
                Band band = new Band(name, head);
                Tour tour = new Tour(city, year, concertNumbers);
                tour.setBand(band);
                tours.add(tour);

                System.out.println(tour);
            }
        }
    }
}
