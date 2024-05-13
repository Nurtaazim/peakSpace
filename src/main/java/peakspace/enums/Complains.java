package peakspace.enums;

public enum Complains {
    User("Это спам"),
    Groups("Враждебные высказывания или символы"),
    Hashtag("Насилие или опасные организации"),
    Photos("Продажа незаконных или подлежащих правовому регулированию товаров"),
    Videos("Травля и преследование");

    private final String russianName;
}
