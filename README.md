# RSocket Employees Management

This is a reactive microservice built with **Spring RSocket**, supporting request-response, request-stream, channel, and fire-and-forget interaction models.  
You can manage employees, including creation, login, search, and cleanup.

## 📦 How to Run

1. Make sure your server is running on `tcp://localhost:7001`.
2. Run commands using [`rsc.jar`](https://github.com/making/rsc) (RSocket CLI tool).

---

## 📮 RSocket Commands

### ✅ 1.  Create a New Employee (Request-Response)

```bash
java -jar rsc.jar \
  --request \
  --route RequestResponseCreateEmployee \
  --data '{
    "email":"yazan@afeka.ac.il",
    "name":"Yazan",
    "password":"123123",
    "birthdate":{"day":"01","month":"01","year":"1980"},
    "roles":["software engineer"]
  }' \
  tcp://localhost:7001
```

### 🔐 2. Login (Request-Response)

```bash
java -jar rsc.jar \
  --request \
  --route RequestResponseLoginEmployee \
  --data '{
    "email": "yazan@afeka.ac.il",
    "password": "123123"
  }' \
  tcp://localhost:7001
```
### 📄 3. Get Employees (Request-Stream)
```
java -jar rsc.jar \
--stream \
--route RequestStreamGetEmployees \
--data '{
"page": 0,
"size": 10
}' \
tcp://localhost:7001
```

### 🔁 4. Get Employees by Channel (PaginationBoundary)
```
java -jar rsc.jar \
  --channel \
  --route ChannelOfEmployees \
  --data '-' \
  tcp://localhost:7001
``` 
then
```
{"page": 0, "size": 2}
{"page": 1, "size": 2}
```

### 🧼 5. Cleanup All Employees (Fire-and-Forget)
```
java -jar rsc.jar \
  --fnf \
  --route FireAndFotgetCleanup \
  tcp://localhost:7001
```
### 🎯 6.  Bonus: Channel Search by Criteria
```
java -jar rsc.jar \
  --channel \
  --route ChannelOfEmployeesByCriteria \
  --data '-' \
  tcp://localhost:7001
```
then
```
{"searchCriteria":"byRole", "value":"software engineer", "page":0, "size":10}
{"searchCriteria":"byEmailDomain", "value":"afeka.ac.il", "page":0, "size":10}

```
