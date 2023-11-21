package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import bean.CustomerMaster;

public class CustomerMasterDAO extends DAO {

	public CustomerMaster searchCustomerMaster(String customerNo) {
		CustomerMaster customerBean = new CustomerMaster();

		try {

			Connection con = getConnection();

			PreparedStatement sqlset = con.prepareStatement("SELECT * FROM CUSTOMER_MASTER WHERE CUSTOMER_NO=?");
			sqlset.setString(1, customerNo);
			ResultSet getData = sqlset.executeQuery();

			while (getData.next()) {
				customerBean.setCustomerNo(getData.getString("CUSTOMER_NO"));
				customerBean.setCustomerName(getData.getString("CUSTOMER_NAME"));
				customerBean.setBranchName(getData.getString("BRANCH_NAME"));
				customerBean.setZipNo(getData.getString("ZIP_NO"));
				customerBean.setAddress1(getData.getString("ADDRESS1"));
				customerBean.setAddress2(getData.getString("ADDRESS2"));
				customerBean.setAddress3(getData.getString("ADDRESS3"));
				customerBean.setTel(getData.getString("TEL"));
				customerBean.setFax(getData.getString("FAX"));
				customerBean.setManager(getData.getString("MANAGER"));
				customerBean.setDelivaryLeadtime(getData.getInt("DELIVARY_LEADTIME"));
				customerBean.setEtc(getData.getString("ETC"));
				customerBean.setRegistdate(getData.getString("REGISTDATE"));
				customerBean.setRegistuser(getData.getString("REGISTUSER"));
			}
			sqlset.close();
			con.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerBean;

	}
}
