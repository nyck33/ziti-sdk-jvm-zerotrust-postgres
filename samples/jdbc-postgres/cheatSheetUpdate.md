Certainly. Here's a cleanly formatted overview of the provided cheat sheet:

---

# Ziti Environment Setup with Docker Compose

## **SETUP Work**

1. **Prerequisites**:
- Ensure Java 11+ is installed and accessible from the terminal:
```bash
java -version
```

- This example utilizes docker-compose. Ensure you have `docker` and `docker-compose` installed.

2. **Retrieve Ziti Repository**:
- Clone the Ziti repository:
```bash
git clone git@github.com:openziti/ziti.git
```
- Alternatively, directly download the necessary files:
```bash
curl https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/docker-compose.yml > /tmp/docker-compose.yml
curl https://raw.githubusercontent.com/openziti/ziti/release-next/quickstart/docker/.env > /tmp/.env
```

3. **Modify Docker Compose File**:
- Modify the `postgres-db` service as shown below:
```yaml
postgres-db:
  image: postgres
  #ports:
  #  - 5432:5432
  networks:
    - zitiblue
  volumes:
    - ./data/db:/var/lib/postgresql/data
  environment:
    - POSTGRES_DB=postgres
    - POSTGRES_USER=postgres
    - POSTGRES_PASSWORD=postgres
```

4. **Launch Docker Environment**:
### from tmp folder in jdbc-postgres
```bash
docker-compose -p pg down -v clear
docker-compose -p pg up
# get logs
docker-compose -p pg logs -f postgres-db


#clear; docker-compose -f /tmp/docker-compose.yml -p pg up
```

### get into blue router container to
5. **Bootstrap PostgreSQL**: 
```bash
docker exec -it -u root pg-ziti-private-blue-1 /bin/bash
apt update && apt install postgresql-client -y --fix-missing
PGPASSWORD=postgres psql -h postgres-db -U postgres
```

Once connected to PostgreSQL, execute the following SQL commands:
```sql
DROP DATABASE simpledb;
CREATE DATABASE simpledb;
ALTER DATABASE simpledb OWNER TO postgres;
\connect simpledb;

# stop here for new set of commands

CREATE TABLE simpletable(chardata varchar(100), somenumber int);
INSERT INTO simpletable VALUES('a', 1);
INSERT INTO simpletable VALUES('b', 2);
INSERT INTO simpletable VALUES('c', 3);
INSERT INTO simpletable VALUES('d', 4);
INSERT INTO simpletable VALUES('e', 5);
INSERT INTO simpletable VALUES('f', 6);
INSERT INTO simpletable VALUES('g', 7);
INSERT INTO simpletable VALUES('h', 8);
INSERT INTO simpletable VALUES('i', 9);
INSERT INTO simpletable VALUES('j', 0);
SELECT * FROM simpletable;

```

Confirm that PostgreSQL's port 5432 is not exposed:
```bash
docker ps
```

## **ZITI BOOTSTRAPPING**

1. **Access the Ziti Controller**:
```bash
docker exec -it pg-ziti-controller-1 bash
```

2. **Login to the Local Ziti Instance**:
```bash
ziti edge login ziti-edge-controller:1280 -u admin -p admin
```
or use the shorthand:
```bash
zitiLogin
```

### **CLEANUP COMMANDS**:

Use these commands if you want to restart without recreating the Docker containers:

```bash
ziti edge delete service private-postgres
ziti edge delete config private-postgres-intercept.v1
ziti edge delete config private-postgres-host.v1
ziti edge delete service-policy postgres-dial-policy
ziti edge delete service-policy postgres-bind-policy
ziti edge delete edge-router-policy public-router-access
ziti edge delete identity tunneler-id 
ziti edge delete identity java-identity

```

### **CREATE/UPDATE COMMANDS**:

Set up configurations, services, and policies:

```bash
ziti edge create config private-postgres-intercept.v1 intercept.v1 '{"protocols":["tcp"],"addresses":["zitified-postgres"], "portRanges":[{"low":5432, "high":5432}]}'
ziti edge create config private-postgres-host.v1 host.v1 '{"protocol":"tcp", "address":"postgres-db","port":5432 }'
ziti edge create service private-postgres --configs private-postgres-intercept.v1,private-postgres-host.v1 -a "private-postgres-services"
ziti edge create service-policy postgres-dial-policy Dial --identity-roles '#postgres-clients' --service-roles '#private-postgres-services'
ziti edge create service-policy postgres-bind-policy Bind --identity-roles '@ziti-private-blue' --service-roles '#private-postgres-services'

```

### **CREATE JAVA ID for SDK Demo**:

Replace the path accordingly:

```bash
ziti edge create identity user java-identity -o java-identity.jwt -a "postgres-clients" 
ziti edge enroll java-identity.jwt -o java-identity.json

# exit docker
docker cp pg-ziti-controller-1:/persistent/java-identity.json .
```

### **Hosts File Modifications**:

You can add `ziti-edge-controller` and `ziti-edge-router` to your hosts file. Ensure to remove them afterwards:

```bash
echo "127.0.0.1       ziti-edge-controller" | sudo tee -a /etc/hosts
echo "127.0.0.1       ziti-edge-router" | sudo tee -a /etc/hosts
```

---

This cheat sheet provides a step-by-step guide to set up a Ziti environment tailored for a PostgreSQL service using Docker Compose. It's recommended to follow the instructions sequentially to ensure a successful setup.