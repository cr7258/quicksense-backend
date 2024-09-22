start-dependencies:
	docker-compose up -d

stop-dependencies:
	docker-compose down

start-app:
	./gradlew bootRun

start-all: start-dependencies start-app

.PHONY: start-dependencies stop-dependencies
