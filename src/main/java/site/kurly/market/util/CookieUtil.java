package site.kurly.market.util;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.SerializationUtils;

import java.util.Base64;

// 브라우저의 쿠키를 다루는 유틸

public class CookieUtil {
    // 요청값(이름, 값, 만료 기간)을 바탕으로 쿠키 추가
    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    // 쿠키의 이름을 입력받아 쿠키 삭제
    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    // 객체를 직렬화해 쿠키의 값으로 반환
    public static String serialize(Object obj) {
        return Base64.getUrlEncoder()
                .encodeToString(SerializationUtils.serialize(obj));
    }

    // 쿠키를 역직렬화해 객체로 변환
    // 아래는 deprecated된 방법
    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        return cls.cast(
                SerializationUtils.deserialize(
                        Base64.getUrlDecoder().decode(cookie.getValue())
                )
        );
    }

    // 쿠키를 역직렬화해 객체로 변환 - ObjectMapper를 이용한 방법 (Jackson 라이브러리)
    // 제네릭은 자바에서 컴파일 시 타입 정보를 제공하여 코드의 재사용성을 높이고 타입 안정성을 보장하는 기능이다.
    // <T>와 Class<T>는 제네릭을 사용하는 두 가지 다른 형태를 나타낸다.

    /*
     * <T> (타입 매개변수)
     * 제네릭 메서드나 클래스에서 사용되는 타입 매개변수를 나타낸다
     * <T>는 임의의 타입을 나타내며, 호출할 때 실제 타입으로 대체된다.
     * 아래의 예에서는 <T>는 어떤 타입의 데이터를 받을 건지, T는 실제로 받은 데이터를 어떤 타입으로 반환할 건지를 나타낸다.
     * 제네릭 메서드에서 타입 매개변수가 메서드의 매개변수에 사용되면, 반환 타입 앞에도 타입 매개변수를 선언해줘야 한다.
     * */

    /*
     * Class<T> (클래스 객체)
     * Class<T>는 제네릭이 적용된 클래스를 나타낸다.
     * T에 해당하는 클래스의 Class 객체를 전달하는 데 사용된다
     * Class<T>를 사용하면 런타임에도 타입 정보를 얻을 수 있다.
     * 예를 들어, 객체를 생성하거나 다양한 런타임 작업을 수행할 때 유용하다
     * */

//    public static <T> T deserialize(Cookie cookie, Class<T> cls) throws IOException {
//        ObjectMapper objectMapper = new ObjectMapper();
//        byte[] data = Base64.getUrlDecoder().decode(cookie.getValue());
//
//        return objectMapper.readValue(data, cls);
//    }
}









