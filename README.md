# beymen-ui

Beymen ui test projesi
Testleri çalıştırmak ve raporu görüntülemek için maven ve allure command line kurulu olmalıdır.

Test
~~~
mvn clean -D test=SearchTest test
~~~

Test Raporu
~~~
allure serve .\target\allure-results\
~~~