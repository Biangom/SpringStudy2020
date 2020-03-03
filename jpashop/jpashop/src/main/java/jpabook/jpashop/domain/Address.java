package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

// 내장타입이라는걸 알리기
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    // JPA 구현 라비으러리가 객체를 생성할 때
    // 리플렉션 같은 기술을 사용할 수 있도록 지원하기 위함



    // jpa 스펙에서는 protected까지 허용
    protected Address() {
    }


    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }






    // 생성할때만 값을 딱 제공하고
    // Setter를 제공하지 않게 한다.




}
