package main;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import bean.EntryExitInfo;
import bean.InOut;
import bean.ProductMaster;
import bean.ProductStock;
import dao.EntryExitInfoDAO;
import dao.ProductMasterDAO;
import dao.ProductStockDAO;
import tool.DateUtils;

@WebServlet("/inout")
public class InOutMain extends HttpServlet {

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

			String getAct = request.getParameter("doAct");//hiddenの"doAct"
			InOut inoutbean=getScreenValue(request);//画面の値
			EntryExitInfo eeibean = new EntryExitInfo();
			ProductStock psbean = new ProductStock();

			switch (getAct) {
				case "searchProduct":
					if(request.getParameter("productNo") == "") {
						inoutbean.setProductNo(null);
						inoutbean.setProductName(null);
						break;
					}
					ProductMaster pm=searchProductName(request);
					inoutbean.setProductName(pm.getProductName());
					if(pm.getProductNo()==null) {
						request.setAttribute("alertMessage", "品番マスタに登録がありません。");
						inoutbean.setProductNo(null);
					}

					break;

				case "checkQty"://[数量]に入力された値をチェック。➀0でないこと➁マイナス値でないこと➂数字以外でないこと。各チェックに引っかかればエラー。
					try{
						int qty=Integer.parseInt(inoutbean.getNyukoQty_or_syukoQty());
						if(qty == 0) {
							request.setAttribute("alertMessage", "数量に「0」は入力できません。");
							inoutbean.setNyukoQty_or_syukoQty(null);
						}else if(qty < 0){
							request.setAttribute("alertMessage", "数量にマイナスの値は入力できません。");
							inoutbean.setNyukoQty_or_syukoQty(null);
						}
					}catch(NumberFormatException e) {
						request.setAttribute("alertMessage", "数字を入力してください。");
						inoutbean.setNyukoQty_or_syukoQty(null);
					}
					break;

				case "regist"://登録ボタンが押されたとき。データベースを更新する。
					boolean checkOk = checkBlank(inoutbean,request);
					if(checkOk == true) {
						//➀品番に入力された値で在庫テーブル検索。あったら在庫テーブルに情報を登録(undate)。なければエラー。
						psbean=searchProductStock(request);

						if(psbean.getProductNo() == null) {
							request.setAttribute("alertMessage", "在庫テーブルに登録がありません。");
							break;
						}
						int result1 = updateProductStock(request,inoutbean,psbean);
						if(result1 == 0) {
							request.setAttribute("alertMessage", "在庫テーブルの更新に失敗しました。");
							break;
						}
						//➁入出庫テーブルに情報を登録(insert)する。失敗したらエラー。
						if(inoutbean.getNyukoSyuko().equals("nyuko")) {
							eeibean.setNyukoQty(Integer.parseInt(inoutbean.getNyukoQty_or_syukoQty()));
						}
						if(inoutbean.getNyukoSyuko().equals("syuko")) {
							eeibean.setSyukoQty(Integer.parseInt(inoutbean.getNyukoQty_or_syukoQty()));
						}
						int result2 = registEntryExitInfo(request,inoutbean,eeibean);

						if (result2 == 0) {
							request.setAttribute("alertMessage", "入出庫テーブルの登録に失敗しました。");
						}else {
							request.setAttribute("alertMessage", "入出庫情報を登録しました。");
							inoutbean=null;
						}
					}
					break;

				case "cancel"://キャンセルボタンが押されたとき。画面の入力値をリセットする。
					inoutbean=null;
					break;
				default:
			}

			request.setAttribute("inoutbean",inoutbean );
			request.getRequestDispatcher("inOut.jsp").forward(request, response);
		}
// >>ログイン状態のとき↑↑
	}


//----------------------------------------以下メソッド-------------------------------------------------

//ログインのセッションがあるかチェック
		public boolean loginCheck(HttpSession session) {
			String result = (String) session.getAttribute("Login");
			if (result == null) {
				return false;
			} else {
				return true;
			}
		}

//【画面の入力値取得】
		private InOut getScreenValue(HttpServletRequest request) {
			InOut getScreen=new InOut();
			getScreen.setProductNo(request.getParameter("productNo"));
			getScreen.setProductName(request.getParameter("productName"));
			getScreen.setNyukoSyuko(request.getParameter("nyukoSyuko"));
			getScreen.setNyukoQty_or_syukoQty(request.getParameter("nyukoQty_or_syukoQty"));
			getScreen.setReason(request.getParameter("reason"));
			return getScreen;
		}

//【品番マスタテーブル品名検索】※OrderMainからコピー
			public ProductMaster searchProductName(HttpServletRequest request) {
				ProductMasterDAO dao=new ProductMasterDAO();
				ProductMaster bean=dao.searchProductMaster((request.getParameter("productNo")));
				return bean;
			}

//【入出庫テーブル登録】
				public int registEntryExitInfo(HttpServletRequest request,InOut inoutbean,EntryExitInfo eeibean) {
					EntryExitInfoDAO dao=new EntryExitInfoDAO();
					int result=dao.insertEntryExitInfo(request,inoutbean,eeibean);
					return result;
				}

//【在庫テーブル品番検索】
			public ProductStock searchProductStock(HttpServletRequest request) {
				ProductStockDAO dao=new ProductStockDAO();
				String ym= DateUtils.getSystemDateWithSlash().replace("/", "");
				String stockInfoDate= ym.substring(0,6);
				ProductStock psbean=dao.searchPuroductStock(stockInfoDate,request.getParameter("productNo"));
				return psbean;
			}

//【在庫テーブル更新】
			public int updateProductStock(HttpServletRequest request,InOut iobean,ProductStock psbean) {
				ProductStockDAO dao=new ProductStockDAO();
				int result=dao.updateProductStockInOut(iobean,psbean);
				return result;
			}

//【入力漏れチェック】
			private boolean checkBlank(InOut iobean,HttpServletRequest request) {
				String message="";
				String nyukoSyuko=iobean.getNyukoSyuko();
				String nyukoQty_or_syukoQty=iobean.getNyukoQty_or_syukoQty();
				String reason=iobean.getReason();
				if(nyukoSyuko==null || nyukoSyuko.isEmpty()) {
					nyukoSyuko="「入出庫選択」";
				}else {
					 nyukoSyuko="";
				}
				if(nyukoQty_or_syukoQty==null || nyukoQty_or_syukoQty.isEmpty()) {
					nyukoQty_or_syukoQty="「数量」";
				}else {
					nyukoQty_or_syukoQty="";
				}
				if(reason==null || reason.isEmpty()) {
					reason="「理由」";
				}else {
					reason="";
				}
				message=nyukoSyuko+nyukoQty_or_syukoQty+reason;
				if(message.isEmpty()) {
					return true;
				}
				request.setAttribute("alertMessage",message+"が入力されていません。");
				return false;
			}


}
