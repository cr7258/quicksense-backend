start-dependencies:
	docker-compose up -d

stop-dependencies:
	docker-compose down

test:
	./gradlew test

test-skip-frontend:
	./gradlew test -PskipFrontendBuild

build:
	./gradlew build

start-app:
	java -jar ./backend/build/libs/backend-0.0.1-SNAPSHOT.jar

build-and-start: build start-all

start-all: start-dependencies start-app

.PHONY: start-dependencies stop-dependencies
