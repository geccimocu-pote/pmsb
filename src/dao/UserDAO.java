package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import bean.User;

public class UserDAO extends DAO {

public int updateData(User userBean) {
	int getResult =0;//結果返却用変数（整数）
	try {
		//1.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
		Connection con = getConnection();
		//2.SQLをセット(ビーンの情報をUserビーンを参照して？にセットする
		PreparedStatement sqlset = con.prepareStatement(
"UPDATE USER_MASTER set name =?,password=?,dept=?,etc=?,hire_date=?"
+ " WHERE user_id= ?");

		sqlset.setString(1,userBean.getName());
		sqlset.setString(2,userBean.getPassword());
		sqlset.setString(3,userBean.getDept());
		sqlset.setString(4,userBean.getEtc());
		sqlset.setString(5,userBean.getHireDate());

		sqlset.setString(6,userBean.getUserId());

		//3.SQLを実行
		getResult = sqlset.executeUpdate();
        sqlset.close();
        con.close();
	} catch (Exception e) {
		e.printStackTrace();
	}
	return getResult;
}
	/**
	 * 受け取ったIDで検索する。
	 * @param recieveID
	 * @return　User
	 */
	public User search(String recieveID) {
		//1.戻り値(User）用の変数を宣言する。
		User user = new User();
		try {
			//2.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
			Connection con = getConnection();


			//3.検索用SQL
			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM USER_MASTER WHERE USER_ID=?");
			//4.？に検索キーワードをセットする。　文字列型なので、整数に変換。
			sqlset.setString(1,recieveID);
			//4.SQLを実行した結果を　getDataに格納する。
			ResultSet getData = sqlset.executeQuery();
			//5.取得したレコードをUserビーンに格納する。
			while (getData.next()) {
				//getRecordにテーブルから取得したレコード
				//データを列名＆型を指定してビーンに格納する。
				user.setUserId(getData.getString("user_Id"));//getIntからgetStringに変更
				user.setName(getData.getString("name"));
				user.setPassword(getData.getString("password"));
				user.setDept(getData.getString("dept"));
				user.setEtc(getData.getString("etc"));//getIntからgetStringに変更
				user.setHireDate(getData.getString("hire_Date"));
			}
			sqlset.close();//SQLクローズ処理
			con.close();//データベース切断処理
		}catch (Exception e ) {
			e.printStackTrace();
		}
			return user;
	}


	//user_masterテーブルの全レコードを取得して、UserビーンのListで値を戻すメソッド
	/**
	 * user_masterテーブルの全レコードを取得
	 * @param 無
	 * @return List<User>
	 */
	public List<User> allGetUserTableData() {
		//1.戻り値(List<User>）用の変数を宣言する。
		List<User> userList = new ArrayList<User>();
		try {
			//2.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
			Connection con = getConnection();
			//3.SQLをセット(このメソッドはuser_infoテーブルのすべてを取得する為に、
			// SQLはWHERE句で条件指定無で、IDでソートする。）
			PreparedStatement sqlset = con.prepareStatement(
					"SELECT * FROM USER_MASTER ORDER BY USER_ID");
			//4.SQLを実行した結果を　getDataに格納する。
			ResultSet getData = sqlset.executeQuery();
			//5.取得したレコードを一行ずつUserビーンに格納して、
			//戻り値用のuserListに追加していく。
			while (getData.next()) {
				//userListに追加するためのUserビーンをインスタン化する。
				User getRecord = new User();
				//getRecordにテーブルから取得したレコード１行分の
				//データを列名＆型を指定してビーンに格納する。
				getRecord.setUserId(getData.getString("user_Id"));
				getRecord.setName(getData.getString("name"));
				getRecord.setPassword(getData.getString("password"));
				getRecord.setDept(getData.getString("dept"));
				getRecord.setEtc(getData.getString("etc"));
				getRecord.setHireDate(getData.getString("hire_Date"));
				//ビーンをuserListに追加する。
				userList.add(getRecord);
			}
			sqlset.close();
			con.close();
		} catch (Exception e) {
			// TODO 自動生成された catch ブロック
			e.printStackTrace();
		}

		return userList;
	}

	/**
	 * USER_MASTERテーブルに新しいレコードを追加するメソッド
	 * @param userBean
	 * @return 成功：新ID番号  失敗:0
	 */
	public int touroku(User userBean, HttpServletRequest request) {
		try {
			//1.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
			Connection con = getConnection();
			//改造①シーケンスの番号を事前に取得しておく
			//2.SQLをセット(ビーンの情報をUserビーンを参照して？にセットする
			HttpSession session = request.getSession();
			String um = (String) session.getAttribute("Login");
			PreparedStatement sqlset = con.prepareStatement(
					"SELECT LPAD (USER_ID.NEXTVAL,6,'0') AS NEW_NO FROM DUAL");
	//SQLを実行、結果をgetNoで取得
			ResultSet getNo=sqlset.executeQuery();
			getNo.next();//値を取得するためにこの命令を一回実行する。
			String getUserNo=getNo.getString("NEW_NO");

			sqlset = con.prepareStatement(
					"INSERT INTO USER_MASTER VALUES(?,?,?,?,?,?,?,?)");

			sqlset.setString(1, getUserNo);
			sqlset.setString(2, userBean.getName());
			sqlset.setString(3, userBean.getPassword());
			sqlset.setString(4, userBean.getDept());
			sqlset.setString(5, userBean.getEtc());
			sqlset.setString(6, userBean.getHireDate());
			//今日の日付を取得する。
			Calendar today = Calendar.getInstance();
			String setDay = today.get(Calendar.YEAR) + "/"
					+ (today.get(Calendar.MONTH) + 1) + "/"
					+ today.get(Calendar.DATE);
			sqlset.setString(7, setDay);
			sqlset.setString(8, um);

			//4.SQLを実行
			int getResult = sqlset.executeUpdate();
            sqlset.close();
            con.close();
			if (getResult == 1) {
				return getResult;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}
	//削除用メソッド
		public int delete(User userBean) {

				try {
					//1.接続　 接続にはスーパークラスのgetConnection()メソッドを使用する。
					Connection con = getConnection();
					//改造①シーケンスの番号を事前に取得しておく
					//2.SQLをセット(ビーンの情報をUserビーンを参照して？にセットする
					PreparedStatement sqlset = con.prepareStatement(

							"DELETE FROM USER_MASTER WHERE USER_ID=?");
					sqlset.setString(1, userBean.getUserId());

					//4.SQLを実行
					int getResult = sqlset.executeUpdate();
		            sqlset.close();
		            con.close();
					if (getResult == 1) {
						return getResult;
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				return 0;
			}
}