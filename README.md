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

### Reference

- https://blog.ex-em.com/1700