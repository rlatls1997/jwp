#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
* 애플리케이션이 초기화될 때 먼저 DB가 초기화된다. 설정한 DB접속정보에 따라 DB커넥션을 생성한다. DispatcherServlet은 초기화될 때 RequestMapping객체를 생성한다. RequestMapping객체는 request path에 따라 알맞는 Controller를 매핑하여 요청을 처리해준다. 

#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
* 

#### 7. next.web.qna package의 ShowController는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* 
