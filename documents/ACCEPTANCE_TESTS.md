# How to run 
1. Run with build `mvn -f ../pom.xml clean install -DskipITs=true`
2. Independently run `mvn -f ../pom.xml failsafe:integration-test -DskipITs=false`


## Cucumber report location
After execution tests will generate report `../targer/cucumber-report/...`

# Useful InteliJ Idea Plugins for work with Cucumber
* [Cucumber+](https://plugins.jetbrains.com/plugin/16289-cucumber-)
* [Cucumber for Java](https://plugins.jetbrains.com/plugin/7212-cucumber-for-java)
* [Gherkin](https://plugins.jetbrains.com/plugin/9164-gherkin)