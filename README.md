# Midas
Project repo for the JPMC Advanced Software Engineering Forage program
# Midas Core – Forage JPMorgan Chase Simulation

## Overview
This project is part of the JPMorgan Chase Forage Software Engineering simulation.
Midas Core processes financial transactions using Kafka and Spring Boot.

## What I Implemented
- Kafka consumer to process transactions
- Incentive API integration via REST
- Balance updates with incentives added to recipients
- REST API to fetch user balances (`GET /balance`)
- Spring Boot configuration running on port 33400

## Tech Stack
- Java
- Spring Boot
- Spring Kafka
- H2 Database
- REST APIs

## How to Run
1. Start Incentive API (`transaction-incentive-api.jar`)
2. Run Midas Core
3. Execute Task tests (TaskThree–TaskFive)
