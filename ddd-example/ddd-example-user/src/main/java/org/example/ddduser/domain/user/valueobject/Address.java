package org.example.ddduser.domain.user.valueobject;

import io.github.chensheng.dddboot.tools.text.TextUtil;
import io.github.chensheng.dddboot.web.core.BizException;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Address {
    private String country;

    private String province;

    private String city;

    private String county;

    private String detail;

    public Address(String country, String province, String city, String county, String detail) {
        if(TextUtil.isNotBlank(detail)
                && (TextUtil.isBlank(country)
                || TextUtil.isBlank(province)
                || TextUtil.isBlank(city)
                || TextUtil.isBlank(county))) {
            throw new BizException("国家/省/市/区县信息不能为空");
        }

        this.country = country;
        this.province = province;
        this.city = city;
        this.county = county;
        this.detail = detail;
    }

    public String getFullAddress() {
        if(TextUtil.isBlank(detail)) {
            return null;
        }
        return country + province + city + county + detail;
    }
}
