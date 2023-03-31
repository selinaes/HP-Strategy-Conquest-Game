# Details

Date : 2023-03-30 15:46:22

Directory /Users/jwl/Documents/651 ECE/Risk_Game

Total : 65 files,  4670 codes, 1157 comments, 972 blanks, all 6799 lines

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)

## Files
| filename | language | code | comment | blank | total |
| :--- | :--- | ---: | ---: | ---: | ---: |
| [.gitlab-ci.yml](/.gitlab-ci.yml) | YAML | 41 | 0 | 7 | 48 |
| [Dockerfile](/Dockerfile) | Docker | 33 | 8 | 14 | 55 |
| [README.md](/README.md) | Markdown | 227 | 0 | 64 | 291 |
| [build.gradle](/build.gradle) | Gradle | 82 | 15 | 10 | 107 |
| [client/build.gradle](/client/build.gradle) | Gradle | 9 | 9 | 8 | 26 |
| [client/src/main/java/edu/duke/ece651/team16/client/App.java](/client/src/main/java/edu/duke/ece651/team16/client/App.java) | Java | 31 | 4 | 4 | 39 |
| [client/src/main/java/edu/duke/ece651/team16/client/Client.java](/client/src/main/java/edu/duke/ece651/team16/client/Client.java) | Java | 290 | 103 | 31 | 424 |
| [client/src/main/java/edu/duke/ece651/team16/client/Views.java](/client/src/main/java/edu/duke/ece651/team16/client/Views.java) | Java | 108 | 51 | 13 | 172 |
| [client/src/test/java/edu/duke/ece651/team16/client/AppTest.java](/client/src/test/java/edu/duke/ece651/team16/client/AppTest.java) | Java | 7 | 1 | 3 | 11 |
| [client/src/test/java/edu/duke/ece651/team16/client/ClientTest.java](/client/src/test/java/edu/duke/ece651/team16/client/ClientTest.java) | Java | 415 | 11 | 95 | 521 |
| [client/src/test/java/edu/duke/ece651/team16/client/ViewsTest.java](/client/src/test/java/edu/duke/ece651/team16/client/ViewsTest.java) | Java | 83 | 0 | 18 | 101 |
| [gradle/wrapper/gradle-wrapper.properties](/gradle/wrapper/gradle-wrapper.properties) | Java Properties | 5 | 0 | 1 | 6 |
| [gradlew.bat](/gradlew.bat) | Batch | 68 | 0 | 22 | 90 |
| [scripts/coverage_summary.sh](/scripts/coverage_summary.sh) | Shell Script | 3 | 1 | 5 | 9 |
| [scripts/emacs-bare.sh](/scripts/emacs-bare.sh) | Shell Script | 17 | 1 | 5 | 23 |
| [scripts/redeploy.sh](/scripts/redeploy.sh) | Shell Script | 3 | 3 | 4 | 10 |
| [scripts/run-tests-in-docker.sh](/scripts/run-tests-in-docker.sh) | Shell Script | 2 | 2 | 1 | 5 |
| [scripts/test-deployment.sh](/scripts/test-deployment.sh) | Shell Script | 28 | 4 | 8 | 40 |
| [scripts/test.sh](/scripts/test.sh) | Shell Script | 5 | 3 | 2 | 10 |
| [server/build.gradle](/server/build.gradle) | Gradle | 9 | 14 | 5 | 28 |
| [server/src/main/java/edu/duke/ece651/team16/server/App.java](/server/src/main/java/edu/duke/ece651/team16/server/App.java) | Java | 32 | 6 | 9 | 47 |
| [server/src/main/java/edu/duke/ece651/team16/server/AssignUnitRuleChecker.java](/server/src/main/java/edu/duke/ece651/team16/server/AssignUnitRuleChecker.java) | Java | 12 | 0 | 6 | 18 |
| [server/src/main/java/edu/duke/ece651/team16/server/AttackAdjacentRuleChecker.java](/server/src/main/java/edu/duke/ece651/team16/server/AttackAdjacentRuleChecker.java) | Java | 15 | 0 | 4 | 19 |
| [server/src/main/java/edu/duke/ece651/team16/server/AttackInputRuleChecker.java](/server/src/main/java/edu/duke/ece651/team16/server/AttackInputRuleChecker.java) | Java | 19 | 1 | 3 | 23 |
| [server/src/main/java/edu/duke/ece651/team16/server/AttackOrder.java](/server/src/main/java/edu/duke/ece651/team16/server/AttackOrder.java) | Java | 37 | 20 | 6 | 63 |
| [server/src/main/java/edu/duke/ece651/team16/server/BasicUnit.java](/server/src/main/java/edu/duke/ece651/team16/server/BasicUnit.java) | Java | 39 | 21 | 10 | 70 |
| [server/src/main/java/edu/duke/ece651/team16/server/Battle.java](/server/src/main/java/edu/duke/ece651/team16/server/Battle.java) | Java | 80 | 42 | 15 | 137 |
| [server/src/main/java/edu/duke/ece651/team16/server/Combat.java](/server/src/main/java/edu/duke/ece651/team16/server/Combat.java) | Java | 44 | 28 | 8 | 80 |
| [server/src/main/java/edu/duke/ece651/team16/server/Conn.java](/server/src/main/java/edu/duke/ece651/team16/server/Conn.java) | Java | 50 | 20 | 8 | 78 |
| [server/src/main/java/edu/duke/ece651/team16/server/Game.java](/server/src/main/java/edu/duke/ece651/team16/server/Game.java) | Java | 390 | 165 | 44 | 599 |
| [server/src/main/java/edu/duke/ece651/team16/server/GameMap.java](/server/src/main/java/edu/duke/ece651/team16/server/GameMap.java) | Java | 155 | 70 | 19 | 244 |
| [server/src/main/java/edu/duke/ece651/team16/server/MessageGenerator.java](/server/src/main/java/edu/duke/ece651/team16/server/MessageGenerator.java) | Java | 84 | 46 | 13 | 143 |
| [server/src/main/java/edu/duke/ece651/team16/server/MoveInputRuleChecker.java](/server/src/main/java/edu/duke/ece651/team16/server/MoveInputRuleChecker.java) | Java | 19 | 1 | 4 | 24 |
| [server/src/main/java/edu/duke/ece651/team16/server/MoveOrder.java](/server/src/main/java/edu/duke/ece651/team16/server/MoveOrder.java) | Java | 37 | 18 | 6 | 61 |
| [server/src/main/java/edu/duke/ece651/team16/server/MovePathRuleChecker.java](/server/src/main/java/edu/duke/ece651/team16/server/MovePathRuleChecker.java) | Java | 80 | 27 | 12 | 119 |
| [server/src/main/java/edu/duke/ece651/team16/server/Order.java](/server/src/main/java/edu/duke/ece651/team16/server/Order.java) | Java | 5 | 0 | 3 | 8 |
| [server/src/main/java/edu/duke/ece651/team16/server/OrderRuleChecker.java](/server/src/main/java/edu/duke/ece651/team16/server/OrderRuleChecker.java) | Java | 18 | 26 | 6 | 50 |
| [server/src/main/java/edu/duke/ece651/team16/server/Player.java](/server/src/main/java/edu/duke/ece651/team16/server/Player.java) | Java | 129 | 86 | 30 | 245 |
| [server/src/main/java/edu/duke/ece651/team16/server/ResearchOrder.java](/server/src/main/java/edu/duke/ece651/team16/server/ResearchOrder.java) | Java | 12 | 10 | 4 | 26 |
| [server/src/main/java/edu/duke/ece651/team16/server/Server.java](/server/src/main/java/edu/duke/ece651/team16/server/Server.java) | Java | 56 | 18 | 10 | 84 |
| [server/src/main/java/edu/duke/ece651/team16/server/Territory.java](/server/src/main/java/edu/duke/ece651/team16/server/Territory.java) | Java | 166 | 104 | 33 | 303 |
| [server/src/main/java/edu/duke/ece651/team16/server/Unit.java](/server/src/main/java/edu/duke/ece651/team16/server/Unit.java) | Java | 11 | 32 | 8 | 51 |
| [server/src/main/java/edu/duke/ece651/team16/server/UpgradeOrder.java](/server/src/main/java/edu/duke/ece651/team16/server/UpgradeOrder.java) | Java | 21 | 14 | 5 | 40 |
| [server/src/test/java/edu/duke/ece651/team16/server/AppTest.java](/server/src/test/java/edu/duke/ece651/team16/server/AppTest.java) | Java | 31 | 5 | 13 | 49 |
| [server/src/test/java/edu/duke/ece651/team16/server/AttackAdjacentRuleCheckerTest.java](/server/src/test/java/edu/duke/ece651/team16/server/AttackAdjacentRuleCheckerTest.java) | Java | 20 | 0 | 5 | 25 |
| [server/src/test/java/edu/duke/ece651/team16/server/AttackInputRuleCheckerTest.java](/server/src/test/java/edu/duke/ece651/team16/server/AttackInputRuleCheckerTest.java) | Java | 69 | 1 | 8 | 78 |
| [server/src/test/java/edu/duke/ece651/team16/server/AttackOrderTest.java](/server/src/test/java/edu/duke/ece651/team16/server/AttackOrderTest.java) | Java | 38 | 0 | 7 | 45 |
| [server/src/test/java/edu/duke/ece651/team16/server/BasicUnitTest.java](/server/src/test/java/edu/duke/ece651/team16/server/BasicUnitTest.java) | Java | 52 | 7 | 18 | 77 |
| [server/src/test/java/edu/duke/ece651/team16/server/BattleTest.java](/server/src/test/java/edu/duke/ece651/team16/server/BattleTest.java) | Java | 108 | 3 | 18 | 129 |
| [server/src/test/java/edu/duke/ece651/team16/server/CombatTest.java](/server/src/test/java/edu/duke/ece651/team16/server/CombatTest.java) | Java | 74 | 0 | 11 | 85 |
| [server/src/test/java/edu/duke/ece651/team16/server/ConnTest.java](/server/src/test/java/edu/duke/ece651/team16/server/ConnTest.java) | Java | 91 | 9 | 31 | 131 |
| [server/src/test/java/edu/duke/ece651/team16/server/GameMapTest.java](/server/src/test/java/edu/duke/ece651/team16/server/GameMapTest.java) | Java | 64 | 0 | 6 | 70 |
| [server/src/test/java/edu/duke/ece651/team16/server/GameTest.java](/server/src/test/java/edu/duke/ece651/team16/server/GameTest.java) | Java | 464 | 78 | 121 | 663 |
| [server/src/test/java/edu/duke/ece651/team16/server/MessageGeneratorTest.java](/server/src/test/java/edu/duke/ece651/team16/server/MessageGeneratorTest.java) | Java | 142 | 5 | 17 | 164 |
| [server/src/test/java/edu/duke/ece651/team16/server/MoveInputRuleCheckerTest.java](/server/src/test/java/edu/duke/ece651/team16/server/MoveInputRuleCheckerTest.java) | Java | 37 | 2 | 5 | 44 |
| [server/src/test/java/edu/duke/ece651/team16/server/MoveOrderTest.java](/server/src/test/java/edu/duke/ece651/team16/server/MoveOrderTest.java) | Java | 36 | 0 | 8 | 44 |
| [server/src/test/java/edu/duke/ece651/team16/server/MovePathRuleCheckerTest.java](/server/src/test/java/edu/duke/ece651/team16/server/MovePathRuleCheckerTest.java) | Java | 54 | 9 | 16 | 79 |
| [server/src/test/java/edu/duke/ece651/team16/server/OrderTest.java](/server/src/test/java/edu/duke/ece651/team16/server/OrderTest.java) | Java | 5 | 6 | 5 | 16 |
| [server/src/test/java/edu/duke/ece651/team16/server/PlayerTest.java](/server/src/test/java/edu/duke/ece651/team16/server/PlayerTest.java) | Java | 105 | 5 | 20 | 130 |
| [server/src/test/java/edu/duke/ece651/team16/server/ServerTest.java](/server/src/test/java/edu/duke/ece651/team16/server/ServerTest.java) | Java | 88 | 10 | 27 | 125 |
| [server/src/test/java/edu/duke/ece651/team16/server/TerritoryTest.java](/server/src/test/java/edu/duke/ece651/team16/server/TerritoryTest.java) | Java | 195 | 16 | 39 | 250 |
| [settings.gradle](/settings.gradle) | Gradle | 2 | 8 | 2 | 12 |
| [shared/build.gradle](/shared/build.gradle) | Gradle | 3 | 8 | 2 | 13 |
| [shared/src/main/java/edu/duke/ece651/team16/shared/MyName.java](/shared/src/main/java/edu/duke/ece651/team16/shared/MyName.java) | Java | 6 | 0 | 2 | 8 |
| [shared/src/test/java/edu/duke/ece651/team16/shared/MyNameTest.java](/shared/src/test/java/edu/duke/ece651/team16/shared/MyNameTest.java) | Java | 9 | 0 | 5 | 14 |

[Summary](results.md) / Details / [Diff Summary](diff.md) / [Diff Details](diff-details.md)