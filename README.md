# AndroHID

Пример Android-приложения на Java, выступающего в роли HID-устройства по Bluetooth.

## Сборка

Проект использует Maven и плагин `android-maven-plugin`. Для сборки APK:

```bash
mvn clean package
```

## Тестирование

Интеграционные тесты реализованы с использованием Robolectric и запускаются командой:

```bash
mvn test
```

## CI

В репозитории настроен workflow GitHub Actions, который собирает APK и очищает предыдущие артефакты.
