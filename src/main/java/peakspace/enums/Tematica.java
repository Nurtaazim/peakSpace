package peakspace.enums;

public enum Tematica {

    MOVIE("Фильм"),
    IT("ИТ"),
    MULTFILM("Мультфильм"),
    ANIME("Аниме"),
    HORROR("Ужастик"),
    MUSIC("Музыка"),
    SPORT("Спорт"),
    MEDICINE("Медицина"),
    TRANSPORT("Транспорт"),
    CONSTRUCTION("Строительство"),
    FINANCE("Финансы"),
    TOURISM("Туризм");

    private final String russianName;

    Tematica(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }

}