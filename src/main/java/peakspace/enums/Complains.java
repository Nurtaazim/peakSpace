package peakspace.enums;

public enum Complains {
        A("Это спам"),
        B("Враждебные высказывания или символы"),
        C("Насилие или опасные организации"),
        D("Продажа незаконных или подлежащих правовому регулированию товаров"),
        E("Травля и преследование"),
        F("Нарушение прав на интеллектуальную собственность"),
        G("Самоуйство или нанесение себе увечий"),
        H("Мошенничество или обман"),
        I("Наркотические средства"),
        J("Ложная информация"),
        K("Мне не нравится");

        private final String russianName;

        Complains(String russianName) {
            this.russianName = russianName;
        }

        public String getRussianName() {
            return russianName;
        }
}
