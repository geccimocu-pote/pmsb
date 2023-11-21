package main;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.User;
import dao.UserDAO;

/**
 * Servlet implementation class UserMain
 */
@WebServlet("/userMain")
public class UserMain extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println("JSP（画面）から実行してください。");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//文字コードを設定する。個別（サーブレット毎に設定するかフィルターですべてのサーブレットを
		//対象にするかは選択できる。　すべてのサーブレットの場合はbookー＞scrー＞toolパッケージ内の
		//EncodingFilter.javaをこのプロジェクトのパッケージ内にコピーすれば良い。
		//個別の場合は下記の二行を追加する。
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		//	セッション情報を取得してログインされていなければログイン画面へ

		HttpSession session = request.getSession();
		boolean login = loginCheck(session);
		if (login == false) {
			request.setAttribute("alertMessage", "ログインができてません。ログインしてください。");
			request.getRequestDispatcher("login.jsp").forward(request, response);
		} else {

			//リクエストパラメータのbunkiの値を取得する。
			String getBunki = request.getParameter("bunki");

			//画面の入力情報を取得してUserビーンに収める。
			User userBean = getScreenValue(request);

			//bunkiの値の内容で処理を分岐する。
			switch (getBunki) {



			case "searchId"://idが入力された場合
				User getUser = searchIdMethod(request);//idで検索するメソッド
				//入力されたIDがデータベーステーブルに未登録の場合
				//エラーメッセージを、登録ありの場合、userBeanに値を渡す。
				if (getUser.getName() == null) {
					//アラート用のメッセージをリクエスト属性で保存
					request.setAttribute(
							"alertMessage", "入力されたIDは登録されていません。新規登録してください。");

					//隠し要素で品番がデータベースにあれば登録ボタンにdisabled
					request.setAttribute("flag", 0);
					//状態表示
					request.setAttribute("state", "新規登録中");

					request.getRequestDispatcher("user.jsp")
					.forward(request, response);
				} else {
					userBean = getUser;
					//隠し要素で品番がデータベースにあれば更新ボタン、削除ボタンにdisabled
					request.setAttribute("flag", 1);
					request.setAttribute("state", "更新・削除中");
				}
				break;

			case "touroku"://登録ボタンがクリックされた場合(登録と更新）
				boolean checkOk = checkValue(request);//入力値チェック用メソッド
				if (checkOk == true) {
					//ここで更新か登録かを分岐する。
					//現在入力中のIDが存在すれば更新メソッド、しなければ新規登録メソッド
					UserDAO ud = new UserDAO();//UserDAOをインスタンス化する。
					User getUserBean = ud.search(userBean.getUserId());//IDで検索

					if (getUserBean.getUserId() == null) {//取得結果のidがnull（そのidで登録されていない）
						touroku(request);//登録用メソッドを呼び出す。
						userBean = null;
					} else {
						//アラート用のメッセージをリクエスト属性で保存
						request.setAttribute(
								"alertMessage", "入力されたIDは既に登録されています。");
					}

					//			{//取得結果のidがnull以外の場合は更新メソッドを呼び出す。
					//					updateUser(request, userBean);
					//				}
					//
				}
				break;

			case "kosin": //更新する場合の処理
				//			  //更新用メソッドを呼び出す。requestと
				//			 //画面情報が格納されているuserBeanを引数として渡す。
				updateUser(request, userBean);
							 userBean=null;
				break;

			case "clear"://クリアーボタンがクリックされた場合
				userBean = null;
				break;

			case "delete"://削除する場合の処理
				//削除用メソッドを呼び出す。
				delete(request, userBean);
				userBean = null;
				break;

			default:
			}
			//画面に表示する情報（ビーン）をリクエスト属性で保存する。
			//名前："user”　値：userBean
			request.setAttribute("user", userBean);

			//画面を遷移する。
			request.getRequestDispatcher("user.jsp").forward(request, response);
		}
	}

	//}上とる時にこれもとる

	/**
	 * 削除用メソッド
	 */
	private void delete(HttpServletRequest request, User userBean) {
		//UserDAOをインスタンス化する。
		UserDAO ud = new UserDAO();
		//更新用メソッドを呼び出す。　引数は受け取ったUserビーン
		int result = ud.delete(userBean);
		//更新成功ならば1、更新失敗なら１以外が戻ってくる。
		//どちらの場合もメッセージをリクエスト属性に保存する。
		if (result != 1) {
			request.setAttribute("alertMessage", "削除に失敗しました。");
		} else {
			request.setAttribute("alertMessage", "削除に成功しました。");
		}
	}

	/**
	 * 更新用メソッド
	 * @param request
	 * @param userBean
	 */
	private void updateUser(HttpServletRequest request, User userBean) {
		//UserDAOをインスタンス化する。
		UserDAO ud = new UserDAO();
		//更新用メソッドを呼び出す。　引数は受け取ったUserビーン
		int result = ud.updateData(userBean);
		//更新成功ならば1、更新失敗なら１以外が戻ってくる。
		//どちらの場合もメッセージをリクエスト属性に保存する。
		if (result != 1) {
			request.setAttribute("alertMessage", "更新に失敗しました。");
		} else {
			request.setAttribute("alertMessage", "更新に成功しました。");
			//更新されたIDを一覧に表示する
			//userBeanに格納されているIDで検索を行う
			User getUserId = ud.search(userBean.getUserId());
			//結果をjsp表示用に準備したlistUserに追加する
			List<User> listUser = new ArrayList<>();
			listUser.add(getUserId);
			//リクエスト属性で保存する
			request.setAttribute("listuser", listUser);
		}
	}

	/**
	 * リクエストパラメータidの値を取得して、データベーステーブルに
	 * 問い合わせて、Userビーンで受け取って
	 * Userビーンで戻す。
	 * @param request
	 * @return User
	 */
	private User searchIdMethod(HttpServletRequest request) {
		//1.リクエストパラメータuser_idから値を取得する。
		String getUserId = request.getParameter("userId");
		//2.UserDAOをインスタンス化する。
		UserDAO ud = new UserDAO();
		//3.serchメソッドを呼び出す。引数はgetUser_Id,戻り値は
		//Userビーンなので、Userビーンで受け取る。
		//この下二個のgetUser_IdをgetUseridにするとエラーが出るのでそのまま
		User getUserI = ud.search(getUserId);
		//4.受け取ったUserビーンを戻す。
		return getUserI;
	}

	/**
	 * 画面のリクエストパラメーターを取得し、Userビーンに格納するメソッド
	 * @param request
	 * @return User
	 */
	private User getScreenValue(HttpServletRequest request) {
		// 画面に入力された情報をUserビーンに格納する。
		User userBean = new User();//このuserBeanを戻り値とする。
		//jspの name="userId" の値を取得して、UserBeanのuserIdにセットする。
		userBean.setUserId(request.getParameter("userId"));
		userBean.setName(request.getParameter("name"));
		userBean.setPassword(request.getParameter("password"));
		userBean.setDept(request.getParameter("dept"));
		userBean.setEtc(request.getParameter("etc"));
		userBean.setHireDate(request.getParameter("hireDate"));
		//画面情報が格納されたuserBeanを戻す。
		return userBean;
	}

	/**
	 * 全入力値をチェックするメソッド
	 * 空白及び数字項目が数字なのかをチェックする。
	 * @param request
	 * @return チェックOK: true  ,  NGの場合:false
	 */
	private boolean checkValue(HttpServletRequest request) {
		//エラーメッセージ格納用変数
		String errorMessage = "";
		/*String errorUserId = "0";
		String errorName = "0";
		String errorPassword = "0";
		String errorDept = "0";
		String errorEtc = "0";
		String errorHireDate = "0";*/

		//IDチェック  リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		// 数字かどうかをチェックするにはメソッド.chars().allMatch(Character::isDigit)を使用

		//名前チェック　リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		if (request.getParameter("name").isEmpty()) {
			errorMessage = errorMessage + "名前が入力されていません。";
		}
		//パスワードチェック　リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		if (request.getParameter("password").isEmpty()) {
			errorMessage = errorMessage + "パスワードが入力されていません。";
		}
		//分類チェック  リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		if (request.getParameter("dept").isEmpty()) {
			errorMessage = errorMessage + "分類が入力されていません。";
		}
		//入社日ちぇっく　リクエストパラメータから取得した値が空白をチェック(.isEmpty)
		if (request.getParameter("hireDate").isEmpty()) {
			errorMessage = errorMessage + "入社日が入力されていません。";
		}

		//最終判定　errorMessageが空白ならば、trueを戻す。
		if (errorMessage.isEmpty()) {
			return true;
		}
		//エラーの内容をリクエスト属性で保存する。
		request.setAttribute("alertMessage", errorMessage);
		//errorMessageがnullなら、falseを戻す。
		return false;
	}

	private void touroku(HttpServletRequest request) {
		// 画面に入力された情報をUserビーンに格納する。
		User userBean = new User();
		userBean.setUserId(request.getParameter("userId"));
		userBean.setName(request.getParameter("name"));
		userBean.setPassword(request.getParameter("password"));
		userBean.setDept(request.getParameter("dept"));
		userBean.setEtc(request.getParameter("etc"));
		userBean.setHireDate(request.getParameter("hireDate"));
		//UserDAOをインスタンス化する。
		UserDAO ud = new UserDAO();
		//UserDAOのtourokuメソッドを呼び(引数にはUserビーンをセット）結果を受け止める。
		int result = ud.touroku(userBean, request);

		if (result > 0) {
			request.setAttribute("alertMessage", "登録に成功しました。");
			//新番号で検索を行い、データをUserビーンに収める。
			User getUser = ud.search(String.valueOf(result));
			//JSPの一覧表示がわはList<user>形式でデータを送る必要があるので
			//送る形式を合わす
			List<User> listUser = new ArrayList<User>();
			//ud.searchから取得したgetUserをList<User>に追加
			listUser.add(getUser);
			//新iDで取得した情報をリクエスト属性で保存
			request.setAttribute("listuser", listUser);

		} else {
			request.setAttribute("alertMessage", "登録に失敗しました。管理者に問い合わせてください。");
		}

	}

	//ログインのセッションがあるかチェックするメソッド
	public boolean loginCheck(HttpSession session) {
		String result = (String) session.getAttribute("Login");
		if (result == null) {
			return false;
		} else {
			return true;
		}

	}

}
