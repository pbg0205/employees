# employees

## 09. 실행 계획

### 1. 쿼리의 동작 방식
**`1.쿼리 캐시(Query Cache)`**

- 쿼리를 분석하기 전에 쿼리 캐시를 확인합니다. 쿼리 캐시는 Select문에 대한 결과 집합을 저장하고 있다.
- 이미 캐싱된 쿼리와 동일한 쿼리를 요청하면 서버는 SQL Parser, Optmizer 단계를 건너뛰고 결과를 반환한다.
- 하지만, MySQL 8.0 이후 버전부터는 동시 처리 성능 저하를 유발하여 쿼리 캐시가 완전히 제거 되었다.

<br>

**`2.Parser`**

- SQL의 문법을 체크하고 Parse Tree 를 생성하여 내부적으로 해당 구문을 처리할 수 있도록 분리됩니다.

<br>

**`3.Preprocessor`**

- Parser에서 문법적 오류를 체크한 후에 추가적으로 의미를 분석합니다.
  (e.g. 해당 컬럼이 테이블 존재 여부, 이름과 별칭 확인)
- SQL을 수행이 가능한지 권한을 체크한다.

<br>

**`4.Query Optimizer`**

- Query를 효과적으로 실행하기 있도록 DB 내부에서 가공하는 단계이다. 
- MySQL의 경우, 효율적인 실행계획을 세우기 위해 성능(비용)을 기반으로 계획하는 CBO를 사용한다.
  (Cost Base Optimization)

<br>

**`5.Query Execution Engine`**

- Query Query Optimizer에 의해 생성된 실행계획대로 Query를 실행하는 역할을 수행한다.

<br>

**`6.Storage Engine`**

- Handler API를 통해 Query Execution Engine과 통신하며, Query Execution Engine 요청에 따라 Data를 디스크에 저장하고 읽어오는 역할을 한다.
- MySQL에서 대표적인 예시로는 InnoDB가 있다.

<br>

## 10.2.1. 실행 계획 출력 포맷

- MySQL에서는 FORMAT 옵션을 통해 실행 계획 표시를 JSON 또는 Tree, 단순 테이블을 선택할 수 있다. (default = TABLE)

```mysql
EXPLAIN FORMAT=JSON
    SELECT * FROM employees e
        INNER JOIN salaries s ON s.emp_no=e.emp_no
        WHERE first_name = 'ABC';
```
```json
{
  "query_block": {
    "select_id": 1,
    "cost_info": {
      "query_cost": "3.03"
    },
    "nested_loop": [
      {
        "table": {
          "table_name": "e",
          "access_type": "ref",
          "possible_keys": [
            "PRIMARY",
            "ix_firstname"
          ],
          "key": "ix_firstname",
          "used_key_parts": [
            "first_name"
          ],
          "key_length": "58",
          "ref": [
            "const"
          ],
          "rows_examined_per_scan": 1,
          "rows_produced_per_join": 1,
          "filtered": "100.00",
          "cost_info": {
            "read_cost": "0.99",
            "eval_cost": "0.10",
            "prefix_cost": "1.09",
            "data_read_per_join": "136"
          },
          "used_columns": [
            "emp_no",
            "birth_date",
            "first_name",
            "last_name",
            "gender",
            "hire_date"
          ]
        }
      },
      {
        "table": {
          "table_name": "s",
          "access_type": "ref",
          "possible_keys": [
            "PRIMARY"
          ],
          "key": "PRIMARY",
          "used_key_parts": [
            "emp_no"
          ],
          "key_length": "4",
          "ref": [
            "employees.e.emp_no"
          ],
          "rows_examined_per_scan": 9,
          "rows_produced_per_join": 9,
          "filtered": "100.00",
          "cost_info": {
            "read_cost": "1.01",
            "eval_cost": "0.93",
            "prefix_cost": "3.04",
            "data_read_per_join": "149"
          },
          "used_columns": [
            "emp_no",
            "salary",
            "from_date",
            "to_date"
          ]
        }
      }
    ]
  }
}
```
<br>

### 10.2.2 쿼리 실행 시간 확인

- MySQL 8.0.18 이후 버전부터 `EXPLAIN ANALYSIS` 쿼리 실행 계획과 단계별 소요 시간에 대한 정보를 확인할 수 있다.
- `SHOW PROFILE`를 통해 실행 시간을 확인할 수 있지만 어떤 부분에서 시간이 많이 소요 되었는지 확인하기는 어렵다.

```sql
EXPLAIN ANALYZE
SELECT avg(s.salary)
FROM employees e
         INNER JOIN salaries s on e.emp_no = s.emp_no
    AND s.salary > 50000
    AND s.from_date <= '1900-01-01'
    AND s.to_date > '1990-01-01'
WHERE e.first_name = 'Matt'
GROUP BY e.hire_date;
```

```
-> Table scan on <temporary>  (actual time=0.008..0.008 rows=0 loops=1)
    -> Aggregate using temporary table  (actual time=211.390..211.390 rows=0 loops=1)
        -> Nested loop inner join  (cost=707.21 rows=121) (actual time=211.337..211.337 rows=0 loops=1)
            -> Index lookup on e using ix_firstname (first_name='Matt')  (cost=254.72 rows=233) (actual time=40.056..77.830 rows=233 loops=1)
            -> Filter: ((s.salary > 50000) and (s.from_date <= DATE'1900-01-01') and (s.to_date > DATE'1990-01-01'))  (cost=1.01 rows=1) (actual time=0.560..0.560 rows=0 loops=233)
                -> Index lookup on s using PRIMARY (emp_no=e.emp_no)  (cost=1.01 rows=9) (actual time=0.448..0.498 rows=10 loops=233)

```
- TreeFormat 에서 들여쓰기는 실행 순서를 의미하며 실행순서는 아래와 같다.
  - 들여쓰기는 같은 레벨에서 상단에 위치한 라인이 먼저 실행
  - 들여쓰기가 다른 레벨에서는 가장 안쪽에 위치한 라인이 먼저 실행

- 위의 실행 계획을 설명하면 아래와 같다.
  1. employees 테이블의 PRIMARY KEY를 찾아서 emp_no와 같은 레코드를 탐색한다.
  2. ix_firstname index를 탐색해 first_name = 'Matt' 인 employee row를 검색한다.
  3. 그리고 나서 salary > 50000 이고 s.from_date < '1900-01-01' 인 조건이 일치하는 건만 가져와서
  4. 1번과 3번의 결과를 조인해
  5. 임시 테이블에 결과를 저장하면서 GROUP BY 집계를 실행하고
  6. 임시 테이블의 결과를 읽어서 결과를 반환한다.

<br>

### 10.3 실행 계획 분석

1. id 컬럼 : 단위 쿼리로 부여되는 식별자 값이다.
   - 단위 쿼리 : SELECT 키워드로 구분한 쿼리
   - 하지만 쿼리 컬럼 번호가 실행 순서를 의미하지 않는다.

**[쿼리 컬럼 번호가 실행 순서를 의미하지 않는 예시]**

```mysql
EXPLAIN
SELECT *
FROM dept_emp de
WHERE de.emp_no = (SELECT e.emp_no
                   FROM employees e
                   WHERE e.first_name = 'Georgi'
                     AND e.last_name = 'Facello'
                   LIMIT 1);
```

<img width="1359" alt="image" src="https://user-images.githubusercontent.com/48561660/186447589-9a46da93-110d-4db2-ae68-e822307e6df7.png">
<img width="481" alt="image" src="https://user-images.githubusercontent.com/48561660/186448395-78009cd0-2d6f-46f2-9f5a-f4c03f176b3f.png">

- 윗 이미지를 확인하면 id = 1은 de table, id = 2는 e table을 선언하였다.
- 실행 쿼리 트리를 확인해보면 우선 서브쿼리를 수행하고 나서 de.emp_no WHERE 절을 처리하는 것을 확인할 수 있다.

<br>

### Reference

- https://blog.ex-em.com/1700