# JwtAuthServerExample
### Стек Spring webflux, Postgresql, Flyway. 
#### Сервис для получения зарегистрированными пользователями в системе access и refresh token.
#### Скачайте и запустите приложение ( по умолчанию порт 8085), для запуска необходим запущенный Docker (поднимаем Postgresql). 
##### По эндпоинту "/api/v1/auth/user/login" отправьте запрос с телом {login:"user",password: "pass"} в ответ получим access и refresh token. 
##### По эндпоинту "/api/v1/auth/user/refresh-access" отправьте запрос с телом {token: <ранее полученный refresh token>} в ответ получим новый access token. 
