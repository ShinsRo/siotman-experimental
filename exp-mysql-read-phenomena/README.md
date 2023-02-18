이 모듈은 트랜젝션 격리수준 (Isolation Level) 중 발생할 수 있는 대표적인 현상들(Phenomena)인

1. 더티리드(Dirty Reads)
1. 논-리피터블 리드(Non-Repeatable Reads)
1. 팬텀리드 (Phantom Reads)

의 간단한 테스트 코드 예제를 담은 모듈이자, 관련 블로그(링크 WIP)에 대한 예제 테스트 코드를 담은 모듈입니다.

MySQL 이 설치된 환경이어야하며, 아래와 같은 테이블 생성이 필요합니다.

```SQL
CREATE TABLE employees
(
    emp_no     INT         NOT NULL,
    birth_date DATE        NOT NULL,
    full_name  VARCHAR(30) NOT NULL,
    gender     ENUM ('M','F') NOT NULL,
    PRIMARY KEY (emp_no)
);
```

테스트 코드 상 사용하는 디비 접속정보는 아래와 같습니다.

```yaml
DbUrl: jdbc:mysql://localhost:3306/employees
DbUsername: root
DbPassword: 
```
