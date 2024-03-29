로컬에서 프록시를 구성하여 
TLS 패킷을 복호화해보기 위한 프로젝트.

## 개념

---

SSL 인증서에는 아래와 같은 정보가 담겨있다. 

1. 인증서 소유자 이름
2. 인증서 소유자 공개키
3. 인증서 유효기간
3. UID
4. 기타 해시값 ( 지문 )

인증서는 계층 구조이며, 최상위 Root 인증서는 일반적으로 브라우저 상 내장되어있다.

하위 인증서의 주체는 상위 인증서의 주체에게 당사 인증서의 해시값을 상위 인증자의 비밀 키로 암호화 요청하고, 
암호화받음으로써 신뢰 체인 (Chanin of Trust) 를 구성할 수 있다.
클라이언트는 Root Cert 의 공개키로 암호화된 2계층 인증서를 복호화함으로써 신뢰가능함을 입증할 수 있기 때문이다.

이러한 중간 인증서를 ICA ( Intermediate CA ) 란 용어로 Root CA ( Certificate Authority ) 와 구분할 수 있다.

개인 인증서의 경우, 일반적으로 이러한 ICA 를 상위 CA 로 삼아 인증서 서명 요청 ( CSR ) 하게 된다. 

| Root 인증서는 상위 CA 가 없으므로, Self-Signed 한다.

브라우저를 기준으로 미리 등록된 인증서의 공개키로 복호화 불가한 인증서는 각 클라이언트 마다의 방법으로
신뢰할 수 없는 인증서임을 경고하거나, 접근을 허용하지 않는다.

#### TLS 수립 과정

1. 클라 : 웹사이트 접근
2. 서버 : 자신의 인증서와 ICA 인증서를 클라이언트에 전송
3. 클라 : 가진 공개키로 ICA, 사이트 인증서를 검증
4. 클라 : 검증 통과 후, 사이트 인증서의 공개 키를 사용하여
5. 클라 : PMS (Pre-Master Secret) 를 암호화한 뒤 서버에 전송

(참고 : 연결 수립 전 Handshake 상에서는 서버 클라가 각자의 Nonce 값을 주고 받는다.)

비대칭키 암복호화는 대칭키에 비해 자원 사용이 많으므로, 
서버 <> 클라 간 첫 연결 수립 시에만 비대칭키를 사용하고, 
연결 수립 뒤에는 대칭 키로 패킷을 암호화한다.

---

TLS 패킷을 읽기 위해 내가 생각한 구조는 이렇다.

1. 프록시 서버에 내 Self-Signed 인증서 설치
3. 프록시 서버를 통해 아이폰에 인증서 설치
4. 아이폰 공기계에 내 Proxy 서버를 프록시 서버로 설정
5. CONNECT 요청에 대한 응답과 함께 실제 서버를 faking 하고
6. Request 를 읽어서 로깅 후
7. 새로운 세션을 통해 실제 서버에 다시 요청 및 응답을 아이폰에 전달.

이게 잘 될진 모르겠으나 한번 해보자.

---

## Self-Signed 인증서 생성하기

