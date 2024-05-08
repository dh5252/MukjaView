
all : build up
DC = docker-compose

build:
	$(DC) build

up:
	$(DC) up -d

down:
	$(DC) down

start:
	$(DC) start

restart:
	$(DC) restart

logs:
	$(DC) logs -f

exec:
	$(DC) exec

stop:
	$(DC) stop
