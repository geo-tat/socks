package com.geotat.socks.swagger;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Documentation {

    // Константы для кодов ответа и описания
    public static final String HTTP_OK = "200";
    public static final String HTTP_BAD_REQUEST = "400";
    public static final String HTTP_NOT_FOUND = "404";
    public static final String BAD_REQUEST_DESCRIPTION = "Неверные параметры запроса";
    public static final String OK_RESPONSE_DESCRIPTION = "Успешное завершение операции";
    public static final String NOT_FOUND_DESCRIPTION = "Сущность не найдена";
    public static final String POST_BATCH_ERROR_DESCRIPTION = "Ошибка при загрузке из файла";

    // Константы для эндпоинта регистрации поступления носков
    public static final String POST_INCOME_SUMMARY = "Регистрация поступления носков на склад";
    public static final String POST_INCOME_DESCRIPTION = "Этот эндпоинт позволяет зарегистрировать поступление носков на склад, добавив их с заданным процентом хлопка и количеством.";


    // Константы для эндпоинта регистрации списания носков
    public static final String POST_OUTCOME_SUMMARY = "Регистрация списания носков со склада";
    public static final String POST_OUTCOME_DESCRIPTION = "Этот эндпоинт позволяет списывать носки со склада.";

    // Константы для эндпоинта получения количества носков
    public static final String PARAMETER_COLOR = "Цвет носков";
    public static final String PARAMETER_OPERATOR = "Оператор сравнения процентного содержания хлопка";
    public static final String PARAMETER_COTTON_PERCENTAGE = "Процентное содержание хлопка (целое число)";
    public static final String GET_SOCKS_SUMMARY = "Получить количество носков по фильтрам";
    public static final String GET_SOCKS_DESCRIPTION = "Этот эндпоинт позволяет получить количество носков на складе по фильтрам цвета, процента хлопка и оператора сравнения.";

    // Константы для эндпоинта получения списка носков
    public static final String GET_SOCKS_LIST_SUMMARY = "Получить список носков по диапазону процентного содержания хлопка";
    public static final String GET_SOCKS_LIST_DESCRIPTION = "Этот эндпоинт позволяет получить список носков в пределах заданного диапазона процентного содержания хлопка с возможностью сортировки по цвету или процентному содержанию хлопка.";

    // Константы для эндпоинта обновления носков
    public static final String PUT_SOCKS_SUMMARY = "Обновить данные носков";
    public static final String PUT_SOCKS_DESCRIPTION = "Этот эндпоинт позволяет обновить данные носков по их ID (например, цвет, процент хлопка, количество).";

    // Константы для эндпоинта загрузки партии носков
    public static final String POST_BATCH_SUMMARY = "Загрузить партию носков";
    public static final String POST_BATCH_DESCRIPTION = "Этот эндпоинт позволяет загрузить CSV файл с партиями носков, содержащими цвет, процентное содержание хлопка и количество.";
}

