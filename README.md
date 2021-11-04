# 이번 정류장 #
- 진행기간 : 2020.08 ~ 2021.02   
- 주요내용   
 \- 이번 정류장은 까지 진행된 프로젝트로 출∙퇴근으로 불편을 겪는 직장인들을 위한 서비스입니다. 많은 이용자들의 출발, 도착 수요를 이용하여 버스 노선을 구성, 제공하여 직장인들에게 편리함을 제공하는 것을 목표로 하는 프로젝트입니다.   
 \- 사용자는 간단한 회원가입을 한 후 자신의 출발지(집), 도착지(회사), 도착 시간을 '노선 생성하기' 버튼을 통해 서버에 반영합니다.   
 \- 서버에서 모인 사용자들의 정보를 통해 여러 사용자들의 출발지와 도착지가 밀집한 곳에 가상의 정류장을 만들어, 이를 경유하는 노선을 구성합니다.   
 \- 그 후 전세버스를 운영하는 개인/회사와의 계약하여 월 10만원 이하의 합리적인 금액으로 만들어진 노선을 이용할 수 있도록 제공합니다.
- 역할 : 후배 1명과 팀을 이뤄 안드로이드 애플리케이션 개발을 진행했으며, 서버 연동, 핵심 UI 및 애니메이션, 결제 시스템 연동 등의 기능을 구현했습니다.
- 사용된 언어 및 기술 : Java, Retrofit, GSON
- 어려웠던 점 : UX를 위해 디자인 팀이 요구했던 UI 요소 및 애니메이션을 구현, 결제 연동, Kakao map API 연동
- 결과 : 핵심 멤버 이탈로 인해 아쉽게도 프로젝트가 중간에 중단되었습니다.


-------
### 스플래쉬 화면, 메인 화면 ###
<img src="https://user-images.githubusercontent.com/65817235/136540367-ea1c85b4-6ec9-41dd-b4e6-a7ed74d34a9e.jpg"  width="360" height="720"> <img src="https://user-images.githubusercontent.com/65817235/136540760-1bb612a1-7d44-429c-878d-56daea8252b5.jpg"  width="360" height="720">
------
# 구현 소스 #
바닥부터 공부하며 진행한 프로젝트라 코드가 깔끔하지 않으나, 어렵게 구현한 몇몇 부분은 비슷한 문제를 마주하신 저같은 초보자분들에게 도움이 되셨으면 해서 참고 사항으로 올립니다.

### 카카오맵 api 끌어서 위치 지정 ###
![이번-정류장-노선-신청](https://user-images.githubusercontent.com/65817235/136542288-805c1a6f-2478-4a95-8087-b89751f960b9.gif)

코드 : https://github.com/cms7371/ThuStop/blob/master/app/src/main/java/com/thustop/thestop/AddRouteMapFragment.java  
카카오 MapVIew의 MapViewEventListener에서 onMapViewMoveFinished를 이용하여 움직임이 끝났을 때 MapView centerPoint를 이용하여 좌표 값을 얻고, 이를 MapReverseGeoCoder를 이용하여 주소를 얻어오는 방식으로 주소 지정을 구현하였습니다.
### FAQ, 탑다운, 슬라이드인, 슬라이드아웃 애니메이션 ###
![이번-정류장-FAQ](https://user-images.githubusercontent.com/65817235/136542400-84b5d412-32d6-4261-84a8-c155606cea5b.gif)

코드 : https://github.com/cms7371/ThuStop/blob/master/app/src/main/java/com/thustop/thestop/NavServiceFAQFragment.java

### Wheel view, 카드 모양, 리플 애니메이션 ###
![이번-정류장-무료-티켓](https://user-images.githubusercontent.com/65817235/136542991-1035fb47-f8d1-4790-a2ed-0a0bd9f9a994.gif)

라이브러리 : https://github.com/gtomato/carouselview

코드 : https://github.com/cms7371/ThuStop/blob/master/app/src/main/java/com/thustop/thestop/FreeTicketIntroFragment.java  
가장 어렵게 구현했던 부분으로 기억합니다. Carouselview 라이브러리의 WheelviewTransformer를 이용하여 배치를 구성하였고, OntouchListener의 motionEvent 인자를 이용하여 사용자의 터치 입력이 +y 방향으로 가는 경우 RecyclerView의 suppressLayout 속성을 활성화하여 좌우 스와이프를 막고, 터치 입력의 y offset만큼 ViewHolder의 위치를 ViewPropertyAnimator를 통해 옮겨줌으로 슬라이드 모션을 구현했습니다. 또한 카드 뒤의 Ripple 또한 터치 입력의 y 축 변화를 감지하여 Animator를 통해 크기를 조정하고 visibility를 바꾸어 리플 애니메이션처럼 보이도록 뷰를 조절했습니다.
