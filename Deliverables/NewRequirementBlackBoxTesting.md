# New Requirement Black Box Testing

#### Authors: Alessandro Versace, Alessandro Landra, Ivan Lombardi, Zhou Chenghan, 

Date: 19/5/2021

Version: V1.0

---

# Black Box Unit Tests

```
<Define here criteria, predicates and the combination of predicates for each function of each class.
Define test cases to cover all equivalence classes and boundary conditions.
In the table, report the description of the black box test case and the correspondence with the JUnit black box test case name/number>
```

### Method DBManager.addRFID (which list should be inserted)

**Criteria for method DBManager.addRFID**

- report any errors from database
- connection status after operation
- existence of new data in query

**Predicates for method DBManager.addRFID**

| Criterion                         | Predicate |
| --------------------------------- | --------- |
| report any errors from database   | yes       |
|                                   | no        |
| connection status after operation | null      |
|                                   | others    |
| existence of new data in query    | yes       |
|                                   | no        |



| report any errors from database | connection status after operation | existence of new data in query | Valid/Invalid | Description of the test case | JUnit test case |
| ------------------------------- | --------------------------------- | ------------------------------ | ------------- | ---------------------------- | --------------- |
| yes                             | -                                 | -                              | invalid       | SQLException<br />false      |                 |
| no                              | close                             | yes                            | valid         | true                         |                 |
| ''                              | ''                                | no                             | valid         | true (nothing match)         |                 |
| no                              | others                            | yes                            | valid         | true                         |                 |
| ''                              | ''                                | no                             | valid         | true (nothing match)         |                 |



### Method DBManager.deleteRFID

**Criteria for method DBManager.deleteRFID**

- report any errors from database
- connection status after operation
- existence of deleted data in query

**Predicates for method DBManager.deleteRFID**

| Criterion                          | Predicate |
| ---------------------------------- | --------- |
| report any errors from database    | yes       |
|                                    | no        |
| connection status after operation  | null      |
|                                    | others    |
| existence of deleted data in query | exist     |
|                                    | not exist |

 **Combination of predicates for method DBManager.deleteRFID**

| report any errors from database | connection status after operation | existence of deleted data in query | Valid/Invalid | Description of the test case | JUnit test case |
| ------------------------------- | --------------------------------- | ---------------------------------- | ------------- | ---------------------------- | --------------- |
| yes                             | -                                 | -                                  | invalid       | SQLException<br />false      |                 |
| no                              | close                             | no                                 | valid         | true                         |                 |
| ''                              | ''                                | yes                                | invalid       | true (error)                 |                 |



### Method RFIDValidator.RFIDValidator

**Criteria for method RFIDValidator.RFIDValidator**

- length of RFID string
- character included in RFID

**Predicates for method RFIDValidator.RFIDValidator**

| Criterion                  | Predicate          |
| -------------------------- | ------------------ |
| length of RFID string      | 10                 |
|                            | others             |
| character included in RFID | 0-9                |
|                            | 'a'-'z' or 'A'-'Z' |
|                            | special character  |

 **Combination of predicates for method RFIDValidator.RFIDValidator**

| character included in RFID | length of RFID string | Valid/Invalid | Description of the test case | JUnit test case |
| -------------------------- | --------------------- | ------------- | ---------------------------- | --------------- |
| -                          | others                | valid         | false                        |                 |
| 'a'-'z' or 'A'-'Z'         | -                     | valid         | false                        |                 |
| special character          | -                     | valid         | false                        |                 |
| 0-9                        | 10                    | valid         | true                         |                 |



# Integration Tests

| Classes                                                      | Junit test cases                  |
| ------------------------------------------------------------ | --------------------------------- |
| EZShop+OderObj+ProductTypeObj+DBManager                      | testRecordOrderArrivalRFIDtest    |
| EZShop+RFIDValidator+SaleTransactionObj+DBManager            | testAddProductToSaleRFIDtest      |
| EZShop+RFIDValidator+SaleTransactionObj+DBManager+ProductTypeObj | testDeleteProductFromSaleRFIDtest |
| EZShop+RFIDValidator+ReturnTransactionObj                    | testReturnProductRFIDtest         |



### Method recordOrderArrivalRFID

**Criteria for method recordOrderArrivalRFID**

- value of order ID
- length of RFID string
- characters in RFID string
- existence of location information
- order state
- Role of log in user
- existence of order

**Predicates for method recordOrderArrivalRFID**

| Criterion                         | Predicate          |
| --------------------------------- | ------------------ |
| value of order ID                 | [-inf,0]           |
|                                   | [1,+inf]           |
| length of RFID string             | 12                 |
|                                   | >12 or <12         |
| characters in RFID string         | 'a'-'z' or 'A'-'Z' |
|                                   | 0-9                |
|                                   | special characters |
|                                   | null               |
| existence of location information | exist              |
|                                   | not exist          |
| order state                       | 'issued'           |
|                                   | 'payed'            |
|                                   | 'completed'        |
| Role of log in user               | 'Administrator'    |
|                                   | 'ShopManager'      |
|                                   | 'Cashier'          |
| existence of order                | exist              |
|                                   | not exist          |

 **Combination of predicates for method recordOrderArrivalRFID**

| value of order ID | length of RFID string | characters in RFID string | existence of location information | order state | Role of log in user | existence of order | Valid/Invalid | Description of the test case | JUnit test case |
| ----------------- | --------------------- | ------------------------- | --------------------------------- | ----------- | ------------------- | ------------------ | ------------- | ---------------------------- | --------------- |
| [-inf,0]          | -                     | -                         | -                                 | -           | -                   | -                  | invalid       | InvalidOrderIdException      |                 |
| -                 | <12 or >12            | -                         | -                                 | -           | -                   | -                  | invalid       | InvalidRFIDException         |                 |
| -                 | -                     | null                      | -                                 | -           | -                   | -                  | invalid       | InvalidRFIDException         |                 |
| -                 | -                     | 'a'-'z' or 'A'-'Z'        | -                                 | -           | -                   | -                  | invalid       | InvalidRFIDException         |                 |
| -                 | -                     | special characters        | -                                 | -           | -                   | -                  | invalid       | InvalidRFIDException         |                 |
| -                 | -                     | -                         | not exist                         | -           | -                   | -                  | invalid       | InvalidLocationException     |                 |
| -                 | -                     | -                         | -                                 | -           | not logged in       | -                  | invalid       | UnauthorizedException        |                 |
| -                 | -                     | -                         | -                                 | -           | 'Cashier'           | -                  | invalid       | UnauthorizedException        |                 |
| [1,+inf]          | 12                    | 0-9                       | exist                             | payed       | 'Administrator'     | not exist          | valid         | false                        |                 |
| ''                | ''                    | ''                        | ''                                | ''          | ''                  | exist              | valid         | true                         |                 |
| ''                | ''                    | ''                        | ''                                | ''          | 'ShopManager'       | not exist          | valid         | false                        |                 |
| ''                | ''                    | ''                        | ''                                | ''          | ''                  | exist              | valid         | true                         |                 |
| ''                | ''                    | ''                        | ''                                | issue       | 'Administrator'     | not exist          | valid         | false                        |                 |
| ''                | ''                    | ''                        | ''                                | ''          | ''                  | exist              | valid         | false                        |                 |
| ''                | ''                    | ''                        | ''                                | ''          | 'ShopManager'       | not exist          | valid         | false                        |                 |
| ''                | ''                    | ''                        | ''                                | ''          | ''                  | exist              | valid         | false                        |                 |
| ''                | ''                    | ''                        | ''                                | complete    | 'Administrator'     | not exist          | valid         | false                        |                 |
| ''                | ''                    | ''                        | ''                                | ''          | ''                  | exist              | valid         | true                         |                 |
| ''                | ''                    | ''                        | ''                                | ''          | 'ShopManager'       | not exist          | valid         | false                        |                 |
| ''                | ''                    | ''                        | ''                                | ''          | ''                  | exist              | valid         | true                         |                 |



### Method addProductToSaleRFID

**Criteria for method addProductToSaleRFID**

- availability of transaction ID
- transaction status
- characters in RFID
- length of RFID string
- existence of RFID
- role of user

**Predicates for method addProductToSaleRFID**

| Criterion                      | Predicate                                |
| ------------------------------ | ---------------------------------------- |
| availability of transaction ID | null                                     |
|                                | [-inf, 0]                                |
|                                | [1, +inf]                                |
| transaction status             | open                                     |
|                                | closed                                   |
|                                | payed                                    |
| characters in RFID             | 'a'-'z' or 'A'-'Z' or special characters |
|                                | null                                     |
|                                | 0-9                                      |
| length of RFID string          | 12                                       |
|                                | <12 or >12                               |
| existence of RFID              | exist                                    |
|                                | not exist                                |
| role of user                   | 'Administrator'                          |
|                                | 'ShopManager'                            |
|                                | 'Cashier'                                |
|                                | not logged in                            |

 **Combination of predicates for method addProductToSaleRFID**

| availability of transaction ID | transaction status | characters in RFID                       | length of RFID string | existence of RFID | role of user    | Valid/Invalid | Description of the test case  | JUnit test case |
| ------------------------------ | ------------------ | ---------------------------------------- | --------------------- | ----------------- | --------------- | ------------- | ----------------------------- | --------------- |
| null                           | -                  | -                                        | -                     | -                 | -               | invalid       | InvalidTransactionIdException |                 |
| [-inf, 0]                      | -                  | -                                        | -                     | -                 | -               | invalid       | InvalidTransactionIdException |                 |
| -                              | -                  | 'a'-'z' or 'A'-'Z' or special characters | -                     | -                 | -               | invalid       | InvalidRFIDException          |                 |
| -                              | -                  | null                                     | -                     | -                 | -               | invalid       | InvalidRFIDException          |                 |
| -                              | -                  | -                                        | <12 or >12            | -                 | -               | invalid       | InvalidRFIDException          |                 |
| -                              | -                  | -                                        | -                     | -                 | not log in      | invalid       | UnauthorizedException         |                 |
| [1, +inf]                      | open               | 0-9                                      | 12                    | exist             | 'Administrator' | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'ShopManager'   | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'Cashier'       | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | not exist         | 'Administrator' | valid         | true                          |                 |
| -                              | -                  | -                                        | -                     | -                 | 'ShopManager'   | valid         | true                          |                 |
| -                              | -                  | -                                        | -                     | -                 | 'Cashier'       | valid         | true                          |                 |
| -                              | closed             | -                                        | -                     | exist             | 'Administrator' | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'ShopManager'   | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'Cashier'       | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | not exist         | 'Administrator' | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'ShopManager'   | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'Cashier'       | valid         | false                         |                 |
| -                              | payed              | -                                        | -                     | exist             | 'Administrator' | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'ShopManager'   | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'Cashier'       | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | not exist         | 'Administrator' | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'ShopManager'   | valid         | false                         |                 |
| -                              | -                  | -                                        | -                     | -                 | 'Cashier'       | valid         | false                         |                 |



### Method deleteProductFromSaleRFID

**Criteria for method deleteProductFromSaleRFID**

- value of sale transaction ID
- transaction status
- characters in RFID
- length of RFID string
- role of user
- existence of product code with corresponding RFID in sale

**Predicates for method deleteProductFromSaleRFID**

| Criterion                                                 | Predicate                                |
| --------------------------------------------------------- | ---------------------------------------- |
| value of sale transaction ID                              | [-inf 0]                                 |
|                                                           | [1,+inf]                                 |
|                                                           | null                                     |
| transaction status                                        | open                                     |
|                                                           | closed                                   |
|                                                           | payed                                    |
| characters in RFID                                        | 'a'-'z' or 'A'-'Z' or special characters |
|                                                           | null                                     |
|                                                           | 0-9                                      |
| length of RFID string                                     | 12                                       |
|                                                           | <12 or >12                               |
| role of user                                              | not logged in                            |
|                                                           | 'Administrator'                          |
|                                                           | 'Cashier'                                |
|                                                           | 'ShopManager'                            |
| existence of product code with corresponding RFID in sale | exist                                    |
|                                                           | not exist                                |

 **Combination of predicates for method deleteProductFromSaleRFID**

| value of sale transaction ID | transaction status | characters in RFID                       | length of RFID string | role of user  | existence of product code with corresponding RFID in sale | Valid/Invalid | Description of the test case  | JUnit test case |
| ---------------------------- | ------------------ | ---------------------------------------- | --------------------- | ------------- | --------------------------------------------------------- | ------------- | ----------------------------- | --------------- |
| null                         | -                  | -                                        | -                     | -             | -                                                         | invalid       | InvalidTransactionIdException |                 |
| [-inf, 0]                    | -                  | -                                        | -                     | -             | -                                                         | invalid       | InvalidTransactionIdException |                 |
| -                            | -                  | 'a'-'z' or 'A'-'Z' or special characters | -                     | -             | -                                                         | invalid       | InvalidRFIDException          |                 |
| -                            | -                  | null                                     | -                     | -             | -                                                         | invalid       | InvalidRFIDException          |                 |
| -                            | -                  | -                                        | <12 or >12            | -             | -                                                         | invalid       | InvalidRFIDException          |                 |
| -                            | -                  | -                                        | -                     | not log in    | -                                                         | invalid       | UnauthorizedException         |                 |
| [1,+inf]                     | open               | 0-9                                      | 12                    | Administrator | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | true                          |                 |
| ''                           |                    | ''                                       | ''                    | Cashier       | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | true                          |                 |
| ''                           |                    | ''                                       | ''                    | ShopManager   | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | true                          |                 |
| ''                           | closed             | ''                                       | ''                    | Administrator | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    | Cashier       | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    | ShopManager   | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | false                         |                 |
| ''                           | payed              | ''                                       | ''                    | Administrator | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    | Cashier       | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    | ShopManager   | not exist                                                 | valid         | false                         |                 |
| ''                           |                    | ''                                       | ''                    |               | exist                                                     | valid         | false                         |                 |



### Method returnProductRFID

**Criteria for method returnProductRFID**

- value of returned transaction ID
- characters in RFID
- length of RFID string
- role of user
- existence of transaction  
- existence of RFID in transaction
- existence of product with corresponding RFID

**Predicates for method returnProductRFID**

| Criterion                                    | Predicate                                |
| -------------------------------------------- | ---------------------------------------- |
| value of returned transaction ID             | null                                     |
|                                              | [-inf, 0]                                |
|                                              | [1, +inf]                                |
| characters in RFID                           | 'a'-'z' or 'A'-'Z' or special characters |
|                                              | null                                     |
|                                              | 0-9                                      |
| length of RFID string                        | 12                                       |
|                                              | <12 or >12                               |
| role of user                                 | not logged in                            |
|                                              | 'Administrator'                          |
|                                              | 'Cashier'                                |
|                                              | 'ShopManager'                            |
| existence of transaction                     | exist                                    |
|                                              | not exist                                |
| existence of RFID in transaction             | exist                                    |
|                                              | not exist                                |
| existence of product with corresponding RFID | exist                                    |
|                                              | not exist                                |

 **Combination of predicates for method returnProductRFID**

| value of returned transaction ID | characters in RFID                       | length of RFID string | role of user  | existence of transaction | existence of RFID in transaction | existence of product with corresponding RFID | Valid/Invalid | Description of the test case  | JUnit test case |
| -------------------------------- | ---------------------------------------- | --------------------- | ------------- | ------------------------ | -------------------------------- | -------------------------------------------- | ------------- | ----------------------------- | --------------- |
| null                             | -                                        | -                     | -             | -                        | -                                | -                                            | invalid       | InvalidTransactionIdException |                 |
| [-inf, 0]                        | -                                        | -                     | -             | -                        | -                                | -                                            | invalid       | InvalidTransactionIdException |                 |
| -                                | 'a'-'z' or 'A'-'Z' or special characters | -                     | -             | -                        | -                                | -                                            | invalid       | InvalidRFIDException          |                 |
| -                                | null                                     | -                     | -             | -                        | -                                | -                                            | invalid       | InvalidRFIDException          |                 |
| -                                | -                                        | <12 or >12            | -             | -                        | -                                | -                                            | invalid       | InvalidRFIDException          |                 |
| -                                | -                                        | -                     | not logged in | -                        | -                                | -                                            | invalid       | UnauthorizedException         |                 |
| [1, +inf]                        | 0-9                                      | 12                    | Administrator | exist                    | exist                            | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | not exist                        | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | not exist                | exist                            | exist                                        | valid         | true                          |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | not exist                        | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | Cashier       | exist                    | exist                            | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | not exist                        | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | not exist                | exist                            | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | not exist                        | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ShopManager   | exist                    | exist                            | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | not exist                        | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | not exist                | exist                            | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | not exist                        | exist                                        | valid         | false                         |                 |
| ''                               | ''                                       | ''                    | ''            | ''                       | ''                               | not exist                                    | valid         | false                         |                 |

