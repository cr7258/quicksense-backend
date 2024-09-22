start-dependencies:
	docker-compose up -d

stop-dependencies:
	docker-compose down

start-app:
	./gradlew bootRun

test:
	./gradlew test

build:
	./gradlew build

start-all: start-dependencies start-app

.PHONY: start-dependencies stop-dependencies
