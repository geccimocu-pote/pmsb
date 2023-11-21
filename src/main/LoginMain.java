package main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.Login;
import bean.User;
import bean.UserMaster;
import dao.UserMasterDAO;

@WebServlet("/login")
public class LoginMain extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("JSPから実行してください。");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//文字コードを設定する。個別（サーブレット毎に設定するかフィルターですべてのサーブレットを
		//対象にするかは選択できる。　すべてのサーブレットの場合はbookー＞scrー＞toolパッケージ内の
		//EncodingFilter.javaをこのプロジェクトのパッケージ内にコピーすれば良い。
		//個別の場合は下記の二行を追加する。
		response.setContentType("text/html; charset=UTF-8");
		request.setCharacterEncoding("UTF-8");

		HttpSession session = request.getSession();

		//リクエストパラメータのbunkiの値を取得する。
		String getBunki = request.getParameter("bunki");

		//画面の入力情報を取得してUserビーンに収める。
		Login loginBean = getScreenValue(request);
		User userBean=new User();

		//bunkiの値の内容で処理を分岐する。

		switch (getBunki) {
		case "searchId"://idが入力された場合
			User getUser = searchIdMethod(loginBean);//idで検索するメソッド
           //入力されたIDがデータベーステーブルに未登録の場合
			//エラーメッセージを、登録ありの場合、userBeanに値を渡す。
			if (getUser.getName()==null) {
            	//アラート用のメッセージをリクエスト属性で保存
            	request.setAttribute(
        "alertMessage", "入力されたIDは登録されていません。");


            } else {
            }
			request.setAttribute("user", getUser);
			request.getRequestDispatcher("login.jsp").forward(request, response);
			break;

		case "login":
			//daoでsearch
			UserMasterDAO umdao = new UserMasterDAO();
			UserMaster usermaster = umdao.searchUserMaster(loginBean);

			//
			if (usermaster.getName() != null) {
				session.setAttribute("Login", usermaster.getUserId());

				request.getRequestDispatcher("menu.jsp").forward(request, response);
			}else {
				//アラート用のメッセージをリクエスト属性で保存
            	request.setAttribute(
        "alertMessage", "未入力もしくは、入力されたIDは登録されていません。");
			}

		default:
			//画面に表示する情報（ビーン）をリクエスト属性で保存する。
			//名前："user”　値：userBean
			request.setAttribute("client", userBean);

			//画面を遷移する。
			request.getRequestDispatcher("login.jsp").forward(request, response);

		}





	}

	private Login getScreenValue(HttpServletRequest request) {
		// TODO 自動生成されたメソッド・スタブ
		//jspからuser_idとnameとpasswordを取得する
				String user_id = request.getParameter("userid");
				String name = request.getParameter("name");
				String password = request.getParameter("password");
				//ビーンにセットする
				Login loginBean = new Login();

				loginBean.setUserid(user_id);
				loginBean.setName(name);
				loginBean.setPassword(password);
		return loginBean;
	}

	private User searchIdMethod(Login loginBean) {
		//1.リクエストパラメータuser_idから値を取得する。
		String user =loginBean.getUserid();
		//2.UserDAOをインスタンス化する。
		UserMasterDAO ud = new UserMasterDAO();
		//3.serchメソッドを呼び出す。引数はgetUser_Id,戻り値は
		//Userビーンなので、Userビーンで受け取る。
		//この下二個のgetUser_IdをgetUseridにするとエラーが出るのでそのまま
		User userI = ud.searchUserId(user);
		//4.受け取ったUserビーンを戻す。
		return userI;
	}

	//ログインのセッションがあるかチェック
	public boolean loginCheck(HttpSession session) {
		String result = (String) session.getAttribute("Login");
		if (result == null) {
			return false;
		} else {
			return true;
		}

	}

}
