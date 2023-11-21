package main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.CustomerMaster;
import bean.Issue;
import bean.ProductMaster;
import bean.PurchaseOrder;
import dao.CustomerMasterDAO;
import dao.ProductMasterDAO;
import dao.ProductStockDAO;
import dao.PurchaseOrderDAO;

@WebServlet("/issue")
public class IssueMain extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println("JSPから実行してください。");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setContentType("text/html; charset=UTF-8");

//セッション確認
		HttpSession session = request.getSession();
		boolean login = loginCheck(session);

// >>ログアウト状態のとき
		if (login == false) {
			request.setAttribute("alertMessage", "ログインができてません。ログインしてください。");
			request.getRequestDispatcher("login.jsp").forward(request, response);//ログイン画面を表示

// >>ログイン状態のとき↓↓
		} else {

			String getAct = request.getParameter("doAct");
			Issue issuebean=getScreenValue(request);//画面の入力値取得

			switch (getAct) {
				case "search"://[受注番号入力時] 受注番号と紐づく情報を検索。あれば表示。なければエラー。
					//受注テーブル検索
					PurchaseOrder puo=searchPurchase(request);
					if(puo.getPoNo() == null) {
						request.setAttribute("alertMessage", "受注番号が存在しません。");
						issuebean=null;
						break;
					}
					if(puo.getFinFlg() == 1) {
						request.setAttribute("alertMessage", "受注番号 ["+issuebean.getPoNo()+"] は出荷完了しています。");
						issuebean=null;
						break;
					}
					//画面用beanにセット
					issuebean.setOrderDte(puo.getOrderDate());//受注日set
					issuebean.setCustomerNo(puo.getCustomerNo());//顧客コードset
					issuebean.setProductNo(puo.getProductNo());//品番set
					issuebean.setPoQty(String.valueOf(puo.getPoQty()));//数量
					issuebean.setShipDate(puo.getShipDate());//出荷日

					//顧客名検索
					CustomerMaster cm=searchCustomer(issuebean.getCustomerNo());//顧客先マスタテーブルsearch
					issuebean.setCustomerName(cm.getCustomerName());//顧客名set

					//品名検索
					ProductMaster pm=searchProductMaster(issuebean.getProductNo());//品番マスタテーブルsearch
					issuebean.setProductName(pm.getProductName());//品名set

					break;

				case "regist":
					//データベースを更新する。
					//受注テーブルupdate
					int result1 = registPurchaseOrder(request,issuebean);
					if(result1 == 0) {
						request.setAttribute("alertMessage", "受注テーブルの更新に失敗しました。");
						break;
					}
					//在庫テーブルupdate
					int result2 = registProductStock(issuebean);
					if(result2 == 0) {
						request.setAttribute("alertMessage", "在庫テーブルの更新に失敗しました。");
					}
					request.setAttribute("alertMessage", "注文番号 [" +issuebean.getPoNo()+"] の出荷処理を完了しました。");
					issuebean=null;
					break;

				case "cancel":
					issuebean=null;
					break;

				default:
			}

			request.setAttribute("issuebean",issuebean);
			request.getRequestDispatcher("issue.jsp").forward(request, response);
		}
// >>ログイン状態のとき↑↑
	}




//----------------------------------------以下メソッド-------------------------------------------------

//【画面入力値の取得】
	private Issue getScreenValue(HttpServletRequest request) {
		Issue getScreen=new Issue();
		getScreen.setPoNo(request.getParameter("poNo"));
		getScreen.setOrderDte(request.getParameter("orderDte"));
		getScreen.setCustomerNo(request.getParameter("customerNo"));
		getScreen.setCustomerName(request.getParameter("customerName"));
		getScreen.setProductNo(request.getParameter("productNo"));
		getScreen.setProductName(request.getParameter("productName"));
		getScreen.setPoQty(request.getParameter("poQty"));
		getScreen.setShipDate(request.getParameter("shipDate"));
			return getScreen;
	}


//【受注テーブル検索】検索キー：受注番号
		private PurchaseOrder searchPurchase(HttpServletRequest request) {
			PurchaseOrderDAO dao=new PurchaseOrderDAO();
			PurchaseOrder puobean=dao.searchPurchaseOrderSingleData(request.getParameter("poNo"));
			return puobean;
		}


//【顧客先マスタテーブル検索】検索キー：顧客コード ※OrderMainからコピー
		private CustomerMaster searchCustomer (String customerNo) {
			CustomerMasterDAO dao=new CustomerMasterDAO();
			CustomerMaster bean=dao.searchCustomerMaster(customerNo);
			return bean;
		}


//【品番で品名検索。品番マスタテーブル】※DeliveryMainからコピー(元の修正予定)
		private ProductMaster searchProductMaster(String productNo) {
			ProductMasterDAO dao=new ProductMasterDAO();
			ProductMaster pmbean=dao.searchProductMaster(productNo);
			return pmbean;
		}

//【受注テーブル更新】完了フラグ立てる
		private int registPurchaseOrder(HttpServletRequest request, Issue issuebean) {
			PurchaseOrderDAO dao=new PurchaseOrderDAO();
			int result=dao.updatePurchaseOrder(issuebean, request);
			return result;
		}


//【在庫テーブル更新】
		private int registProductStock(Issue issuebean) {
			ProductStockDAO dao=new ProductStockDAO();
			int result=dao.updateProductStockIssue(issuebean);
			return result;
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