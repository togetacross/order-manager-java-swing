# order-manager-java-swing

## Inspiration

Hobby project for practise.

## Description

Simple desktop java swing app for manage taking orders for costumers.

An offer manager application for a home renovation company. 
We can handle:
- add/modify/delete customers and main services
- create an order with the app calculated price of the services per square meter for a selected costumer  
- rewiew/delete saved in database orders
- write selected order to file to selected place with based on "official document" template
- modify main company data in file for template based document

## Features

- Java 11
- Maven
- MySQL 8.0.23
- JDBC

## Installation

setup mysql tables (dump file included in project root folder)
```
mvn clean package
```
run order-manager.exe

## License 

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
