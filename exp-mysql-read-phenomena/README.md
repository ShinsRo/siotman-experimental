이 모듈은 트랜젝션 격리수준 (Isolation Level) 중 발생할 수 있는 대표적인 현상들(Phenomena)인

1. 더티리드(Dirty Reads)
1. 논-리피터블 리드(Non-Repeatable Reads)
1. 팬텀리드 (Phantom Reads)

의 간단한 테스트 코드 예제를 담은 모듈이자, [관련 블로그](https://velog.io/@shins/%EB%8D%94%ED%8B%B0%EB%A6%AC%EB%93%9C%EC%99%80-%EB%85%BC-%EB%A6%AC%ED%94%BC%ED%84%B0%EB%B8%94-%EB%A6%AC%EB%93%9C-%EA%B7%B8%EB%A6%AC%EA%B3%A0-%ED%8C%AC%ED%85%80%EB%A6%AC%EB%93%9C-MySQL-%EC%9D%84-%EC%A4%91%EC%8B%AC%EC%9C%BC%EB%A1%9C)에 대한 예제 테스트 코드를 담은 모듈입니다.

MySQL 이 설치된 환경이어야하며, 아래와 같이 데이터베이스와 계정을 생성해두었습니다.

```sql
create database read_phenomena_test;

create user 'shinsro'@'localhost';
create user 'karina'@'localhost';

grant all privileges on read_phenomena_test.* to 'shinsro'@'localhost';
grant all privileges on read_phenomena_test.* to 'karina'@'localhost';
```
