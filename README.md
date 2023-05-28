#### 1. Tomcat 서버를 시작할 때 웹 애플리케이션이 초기화하는 과정을 설명하라.
* 애플리케이션이 초기화될 때 먼저 DB가 초기화된다. 설정한 DB접속정보에 따라 DB커넥션을 생성한다. DispatcherServlet은 초기화될 때 RequestMapping객체를 생성한다. RequestMapping객체는 request path에 따라 알맞는 Controller를 매핑하여 요청을 처리해준다. 웹 애플리케이션은 요청을 받을 준비가 된다. 

#### 2. Tomcat 서버를 시작한 후 http://localhost:8080으로 접근시 호출 순서 및 흐름을 설명하라.
* 요청이 들어오면 dispatcherServlet에서 요청에 맞는 적절한 controller를 매핑하고 요청을 처리하여 응답한다. request path가 '/'이므로 HomeController에서 요청을 처리하게 된다. jsp파일에 model값을 매핑하여 만든 html text를 클라이언트 브라우저에 반환하면 브라우저는 반환된 텍스트를 읽어가며 필요한 자원(css파일, 이미지파일 등)을 추가로 요청하고 DOM트리를 만들고 CSS데이터를 결합하여 화면을 렌더링한다.    

#### 7. next.web.qna package의 ShowController는 멀티 쓰레드 상황에서 문제가 발생하는 이유에 대해 설명하라.
* ShowController의 멤버로 정의해둔 question, answers는 전역변수이기 때문에 멀티스레드 환경에서 1, 2번 스레드가 있다고 가정할 때 1번 스레드에서 지정한 값이 2번 스레드의 결과로 반환되는 등의 결과를 초래할 수 있다. 스레드세이프 하지 않다. 메서드 내 지역변수에 초기화하여 문제를 해결할 수 있다.
