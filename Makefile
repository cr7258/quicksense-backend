start-dependencies:
	docker-compose up -d

stop-dependencies:
	docker-compose down

.PHONY: start-dependencies stop-dependencies
