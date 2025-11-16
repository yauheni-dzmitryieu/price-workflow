```shell
mvn gatling:test 
  -Dusers=200 
  -Dduration=20 
  -Dcount=1000
  -Dconcurrency=50
  -DbaseUrl=http://localhost:8087
```

# GET
```shell
mvn gatling:test -DbaseUrl=http://localhost:8087 -Dgatling.simulationClass=com.yd.PriceGetSimulation -Dusers=50 -Dduration=30 -Dcount=5000 -Dconcurrency=20
```