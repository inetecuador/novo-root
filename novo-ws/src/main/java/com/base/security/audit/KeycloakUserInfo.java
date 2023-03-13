package com.base.security.audit;

import javax.servlet.http.HttpServletRequest;
import com.base.security.AuthSecurityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * KeycloakUserInfo.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
public class KeycloakUserInfo implements IKeycloakUserInfo {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserId() {
        return AuthSecurityUtil.getUserLogin().getUserId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUserName() {
        return AuthSecurityUtil.getUserLogin().getUserName();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIp() {
        String ipAddress;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
            ipAddress = request.getHeader("x-original-forwarded-for");
            if (StringUtils.isBlank(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            } else {
                ipAddress = ipAddress.split(",")[0];
            }
        } catch (Exception e) {
            ipAddress = "0.0.0.0";
        }
        return StringUtils.isBlank(ipAddress) ? "NONE" : ipAddress;
    }
}
