package peakspace.enums;

public enum Choise {
    User("Пользователи"),
    Groups("Группы"),
    Hashtag("Хештеги"),
    Photos("Фотографии"),
    Videos("Видео");

    private final String russianName;

    Choise(String russianName) {
        this.russianName = russianName;
    }

    public String getRussianName() {
        return russianName;
    }
}
