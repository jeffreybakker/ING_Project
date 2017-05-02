# ING Project README

## Introduction

This project is part of one of the many projects in the current ING Honours Track in which we currently are testing the agility of a system. Everyone starts out with a given set of initial requirements and has to implement those, nothing more, nothing less. After the initial implementation each group receives a set of requirements changes that they have to implement. While implementing the new requirements every-one has to keep track of the amount of time spent on those changes so that we can use that as a relative metric for the agility of a system.

## Implementation

For implementing the initial set of requirements we have used the Spring framework which provides us with easy to use functions for storing, retrieving and updating data. And combined with a JSON RPC library we could easily create an JSON RPC API for our project.

### General Architecture

We have subdivided everything into 4 different packages:
- account for everything related to bank accounts
- card for everything related to debit cards
- customer for customer objects and their controllers
- transaction for handling transactions between accounts a and b

Each package has:
- a model class which represents some object or something in java
- a repository class which interacts with the database
- a service class which defines the JSON RPC methods
- a service implementation class which implements the previousely defined methods using the model and repository

## Dependencies

- Spring Boot
- Spring web
- Spring Data JPA
- mysql-connector-java
- JSON RPC 4J

## Assumptions

- The client (PIN machine, ATM, front end web application, etc) makes all of the API calls required for something to happen
- No security is needed

## Instructions

1. Create a MySQL database on `localhost:3306` called `ing_db` for a user with username `ing_project` and an empty or no password
2. Run `honours.ing.banq.Application`

All of the JSON RPC methods are described in the `**Service` classes
