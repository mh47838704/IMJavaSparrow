package dbserver.dao;

import dbserver.entity.UserInfoVo;

/**
 *
 */
public interface UserDao {
    public UserInfoVo find(String username);
}
