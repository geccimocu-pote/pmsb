package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.Login;
import bean.User;
import bean.UserMaster;

public class UserMasterDAO extends DAO {

	public UserMaster searchUserMaster(Login namepass) {
		UserMaster userBean = new UserMaster();
		try {
			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM USER_MASTER" +
					" WHERE USER_ID=? and PASSWORD=?");

			sqlset.setString(1, namepass.getUserid());
			sqlset.setString(2, namepass.getPassword());

			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				userBean.setUserId(getData.getString("user_id"));
				userBean.setName(getData.getString("name"));
				userBean.setPassword(getData.getString("password"));
				userBean.setDept(getData.getString("dept"));
				userBean.setEtc(getData.getString("etc"));
				userBean.setHireDate(getData.getString("hire_date"));
				userBean.setRegistdate(getData.getString("registdate"));
				userBean.setRegistuser(getData.getString("registuser"));

			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userBean;
	}

	public User searchUserId(String userId) {
		User userBean = new User();
		try {
			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM USER_MASTER WHERE USER_ID=?");

			sqlset.setString(1,userId);

			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				userBean.setUserId(getData.getString("user_id"));
				userBean.setName(getData.getString("name"));


			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userBean;
	}

}
