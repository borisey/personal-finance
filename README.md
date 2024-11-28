<h1>Система управления личными финансами</h1>

<h2>Пример реализации</h2>
<p>Пример реализации приложения можно посмотреть по ссылке: <a href="https://walletus.net/">walletus.net</a></p>

<h2>Быстрый старт</h2>
<p>1. Создайте директорию проекта, например, "personal-finance"</p>
<p>2. Перейдите в директорию проекта в консоли и скопируйте репозиторий проекта в директорию проекта: выполните команду <strong>git clone git@github.com:borisey/personal-finance.git ./</strong></p>
<p>3. Установите MySQL в Docker-контейнере: в консоли (в корневой директории проекта) выполните команду <strong>docker-compose up</strong></p>
<p>4. Запустите приложение в IDE IDEA: откройте файл <strong>/src/main/java/com/borisey/personal_finance/PersonalFinanceApplication.java</strong> и выполните команду <strong>"RUN"</strong></p>
<p>5. Зарегистрируйтесь в приложении (по ссылке: <a href="https://walletus.net/register">walletus.net/register</a>)</p>
<p>6. Авторизуйтесь в приложении, используя логин и пароль, указанные при регистрации (по ссылке: <a href="https://walletus.net/login">walletus.net/login</a>)</p>
<p>7. Создайте <strong>счет</strong> в Дашборде или в соответствующем разделе меню</p>
<p>8. Создайте <strong>категорию дохода</strong> в Дашборде или в соответствующем разделе меню</p>
<p>9. Создайте <strong>категорию расхода</strong> в Дашборде или в соответствующем разделе меню</p>
<p>10. Добавьте <strong>доход</strong> в Дашборде или в соответствующем разделе меню</p>
<p>11. Добавьте <strong>расход</strong> в Дашборде или в соответствующем разделе меню</p>
<p>12. Перейдите в раздел <strong>Аналитика</strong> для просмотра сводной информации о ваших личных финансах (по ссылке: <a href="https://walletus.net/analytics">walletus.net/analytics</a>)</p>

<h2>Описание приложения</h2>
<h3>1. Реализация авторизации пользователей</h3>
<p>Регистрация пользователя доступна по ссылке: <a href="https://walletus.net/register">walletus.net/register</a></p>
<p>Авторизация зарегистрированного пользователя доступна по ссылке: <a href="https://walletus.net/login">walletus.net/login</a></p>
<p>Авторизация зарегистрированного пользователя доступна по ссылке: <a href="https://walletus.net/login">walletus.net/login</a></p>
<p>Выход из приложения осуществляется при переходе по ссылке: <a href="https://walletus.net/login?logout">walletus.net/login?logout</a></p>
<h3>2. Взаимодействие с пользователем</h3>
<h3>3. Управление доходами и расходами</h3>
<h3>4. Работа с кошельком пользователя</h3>
<h3>5. Вывод информации</h3>
<h3>6. Оповещения пользователя</h3>
<h3>7. Сохранение и загрузка данных</h3>
<h3>8. Валидация данных</h3>
<h3>9. Дополнительные возможности</h3>
<ul>
    <li>Переводы между кошельками</li>
    <li>Отдельный раздел с аналитикой, содержащей графики доходов, расходов, прогнозов бюджетов и аналитики по счетам</li>
    <li>Вывод аналитики по конкретным, указанным пользователем, датам (раздел аналитики доступен по ссылке: <a href="https://walletus.net/analytics">walletus.net/analytics</a>)</li>
</ul>
<h3>10. Разделение функционала по классам</h3>
