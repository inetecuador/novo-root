package com.base.security.audit;

/**
 * IKeycloakUserInfo.
 *
 * @author vsangucho on 10/03/2023
 * @version 1.0
 */
public interface IKeycloakUserInfo {

    String getUserId();

    String getUserName();

    String getIp();


}
