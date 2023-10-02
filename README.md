# 칠링필링 (Chilling Feeling)
취향에 맞는 주류 추천 및 정보 제공 웹 서비스 <br>

<hr>

- <b> Service architecture </b>

![서비스 아키텍처](https://github.com/REZ-s/chilling-feeling/assets/50026903/9eba7a14-0a42-4a5a-9743-9c3bd7f8a2d2)

<hr>

- <b> ERD </b>

![ERD](https://github.com/REZ-s/chilling-feeling/assets/50026903/f17d1954-61ab-4788-bd8a-fefc321f89ce)

<b> 모든 엔티티 클래스는 기록을 위해서 아래의 엔티티 리스너를 상속합니다. (위 ERD 에는 생략되어 있습니다.) </b> <br>

```
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeStamp implements Serializable {
    @CreatedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime createdDate;

    @LastModifiedDate
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime updatedDate;

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }
}
``` 

<hr>

- <b> 서비스 살펴보기 </b>
  
<b> Main page </b> <br>
![image](https://github.com/REZ-s/chilling-feeling/assets/50026903/401a2e8c-4aab-4cb7-9835-eedd4ff97732)

<hr> 

<b> Cart page </b> <br>
![장바구니](https://github.com/REZ-s/chilling-feeling/assets/50026903/ecfe5d8a-a61d-41a3-a17e-8bf05e15227d)

<hr> 

- <b> What's the meaning [chilling feeling](https://chillingfeeling.com)? </b><br>
chilling 의 의미 중에 놀고 휴식을 취하며 시간을 보낸다는 의미로 많이 사용되는 단어로 <br>
기분을 뜻하는 feeling 과 결합하여 '<b>한 잔하고 싶은 기분</b>' 을 표현했습니다. <br>

- <b> Team members <br>
풀스택 개발자 1명 (본인), 디자이너 1명 </b> <br>

- <b> [Service link](https://chillingfeeling.com) </b> <br>

<br>

# 프로젝트 주요 목표 및 성과
- 취향에 맞는 주류 추천 및 정보 제공
- 부분적인 기능에서 끝나지 않고 하나의 완성된 서비스를 구축
- 설계부터 배포까지 전체 제품 사이클 개발 및 개선
- 트래픽이 몰렸을 때를 고려한 개발 (초당 1000번의 요청까지 서비스 가용성 확보)
- 확장성을 고려한 설계 <br>
(ERD 상으로나 코드 베이스가 작성이 되어있으나 실제 서비스로 제공하지 않는 부분도 있습니다. <br>
실제 웹서비스의 많은 부분을 경험해보고 싶어서 작성했습니다. 계속 관리하며 추가해나갈 계획입니다.) <br>

<br>

# 기술적 이슈 해결 과정
- 성능 테스트 - 개선 일지 (요약 정리) <br>
https://chillingfeeling.notion.site/9263a0f4b511446b96c7634f676901c1?pvs=4

- 검색 성능 높이기 - Full Text Search <br>
https://chillingfeeling.notion.site/Full-Text-Search-4a0e671c8d0245a996e5cedc7debafe6?pvs=4

- 느린 쿼리 개선하기 <br>
https://chillingfeeling.notion.site/5fbc41d4d44044d2a075e8be95244cc4?pvs=4

- Redis Cache 적용하기 <br>
https://chillingfeeling.notion.site/Redis-Cache-d098498b0e7941bda04412a19752f817?pvs=4

- 스레드 설정 변경하기 <br>
https://chillingfeeling.notion.site/883fb6981b234f74b5dd51f81a6d2b84?pvs=4

- K6 로 부하 테스트 결과 한 번에 보기 <br>
https://chillingfeeling.notion.site/K6-214259d9170f4a96b5af498861ace242?pvs=4

- Postman 성능 테스트 결과와 해석 <br>
https://chillingfeeling.notion.site/Postman-f4c4e0f7243649cfbdc38e72e6961a85?pvs=4

- ERD 와 스키마에 따른 도메인 디렉터리 구조 <br>
https://chillingfeeling.notion.site/ERD-e1a69b9a4b774e20bd30ec07d369da84?pvs=4

- MySQL 마이그레이션 <br>
https://chillingfeeling.notion.site/MySQL-121e6a2aaabb498a815d20a6c2ee1a15?pvs=4

- 이슈 해결 기록 - CI 빌드 <br>
https://chillingfeeling.notion.site/CI-8b310994027e46449c6766d7cbb10bd3?pvs=4

- 이슈 해결 기록 - CD 배포 <br>
https://chillingfeeling.notion.site/e9d328acbfd84d6b8b1a2834b84df646?pvs=4

- 이슈 해결 기록 - 검색 기능 <br>
https://chillingfeeling.notion.site/c0fd1ecd92524679b89c36b20254eba7?pvs=4

- API 요청 응답에 대한 명세 작성 및 표준화 <br>
https://chillingfeeling.notion.site/API-cd63a1a60e0846efaba3966abfbf6c2c?pvs=4

<br>

# 프로젝트와 관련된 기록
- 서비스 아키텍처 <br>
https://chillingfeeling.notion.site/b6c7be9cf0a14ba59f3846a8c3b336bf?pvs=4

- 프로젝트 프론트엔드 디자인 관련 링크 <br>
https://chillingfeeling.notion.site/dcd3814b3bbb410cb1cce3b30d31aaf3?pvs=4

- 그 외 설정 및 모니터링 결과 <br>
https://chillingfeeling.notion.site/8c5f18c1071e480bbb5f9156fc8d6ff7?pvs=4

- 인텔리제이 다루기 <br>
https://chillingfeeling.notion.site/000588e3acee48ffbcb9ba413f34ca29?pvs=4

<br>

# 프로젝트를 진행하며 기술적인 측면에서 고려한 것들
- 로그인 상태인지 비로그인 상태를 체크하기 위한 로직이 반복되고 중복되어 AOP 로 분리
- 폼 로그인과 소셜 로그인 둘다 지원
- @Transactional 을 활용해 빌링 관련 API 에는 트랜잭션 격리 수준을 높게 설정 <br> 예) @Transactional(readOnly = true, isolation = Isolation.REPEATABLE_READ)
- 인증 방식으로 JWT 를 사용하기로 결정
- JWT 를 사용할 때 취약점을 최대한 방지하고자 refresh token 갱신 방식을 RTR (Refresh Token Rotation) 방식으로 토큰 재발급
- 토큰을 빠르게 조회하기 위해서 Redis 에 같이 저장하기로 결정
- 트랜잭션이 모두 성공했을 시점에 Redis 에 저장하도록 코드 순서 고려
- MySQL Workbench 를 이용해 쿼리 실행계획과 실제 걸린 시간을 비교하여 인덱스 설정
- join 문 사용시 한 번 더 쿼리 실행계획 확인
- @Async 를 상품 조회 API 에 적용하여 리소스 사용률과 실제 응답속도 비교
- 중복 요청 방지를 위한 필터 추가
- AWS 프리티어로 최대한 서비스 구성하기
- 성능 테스트 도구로 jmeter, k6, postman 를 사용해보고 비교 (nGringer 는 Java 8 까지 지원하여 사용 불가)
- 어플리케이션 성능 모니터링 도구로 Elastic APM 사용
- API 요청과 응답에 대한 명세 작성 및 표준화
  
<br>


