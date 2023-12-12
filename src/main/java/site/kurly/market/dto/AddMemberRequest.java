package site.kurly.market.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class AddMemberRequest {
    private String auth;
    private String email;
    private String password;
    private String nickname;
    private String name;
    private String phoneNumber;
    private String address;
    private String addressDetail;
    private String addressPostcode;
    private String gender;
    private LocalDateTime birthday;
}
