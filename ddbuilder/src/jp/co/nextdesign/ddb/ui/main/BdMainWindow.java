/*
 * このソフトウエアはネクストデザイン有限会社に所有される機密情報です。
 * この機密情報を漏洩してはならず、当社の意図する許可の元において使用しなければなりません。
 * This software is confidential information owned by Next Design Ltd.
 * You must not disclose this confidential information and use it under the permission that we intend.
 * Copyright(c) 2015 NEXT DESIGN Ltd.
 */
package jp.co.nextdesign.ddb.ui.main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ProgressMonitor;
import javax.swing.UIDefaults;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import jp.co.nextdesign.ddb.BdApplicationProperty;
import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.BdConstants;
import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.ui.BdUiException;
import jp.co.nextdesign.ddb.ui.main.directoryselector.BdProjectFolderSelector;
import jp.co.nextdesign.ddb.ui.main.directoryselector.BdTemplateFolderSelector;
import jp.co.nextdesign.ddb.ui.propertywindow.BdPropertyFieldPanel;
import jp.co.nextdesign.util.NdUtilException;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * DDBuilder メイン画面
 * @author murayama
 */
public class BdMainWindow extends JFrame {

	private static final long serialVersionUID = 1L;
	public static String FILE_GENERATED_AT = "";
	private JLabel messageLabel; 
	private BdProjectFolderSelector projectFolderSelector;
	private BdTemplateFolderSelector templateFolderSelector;
	private JButton openPropertyWindowButton;
	private JButton reportButton;
	private JButton exitButton;
	private BdBuilderTask currentTask;
	private ProgressMonitor progressMonitor;
	private BdBuilderSetting directorySetting;
	private static final int MESSAGE_MAX_LINES = 15;
	private BdPropertyFieldPanel groupIdPanel;
	private BdPropertyFieldPanel artifactIdPanel;

	/**
	 * 表示メッセージを作成する
	 * @param enable
	 * @return
	 */
	private String makeOperationMessage(boolean enable){
		String result= BdMessageResource.get("status.ready"); //"操作できます。"
		if (enable){	
			if (currentTask != null){
				if (currentTask.isError()){
					result = BdMessageResource.get("status.error"); //"処理中にエラーが発生しました。";
				} else if (currentTask.isCancelled()){
					result = BdMessageResource.get("status.canceled"); //"処理はキャンセルされました。";
				} else if (currentTask.isDone()){
					result = BdMessageResource.get("status.completed"); //"処理は完了しました。";
				}
			}
		} else {
			result = BdMessageResource.get("status.busy"); //""更新中です。お待ちください。";
		}
		return BdMessageResource.get("status.title") + result; //STATUS_TITLE + result;
	}

	/**
	 * 現在のタスクを設定する
	 * @param currentTask
	 */
	public void setCurrentTask(BdBuilderTask currentTask){
		this.currentTask = currentTask;
	}

	/**
	 * 現在のタスクを応答する
	 * @return
	 */
	public BdBuilderTask getCurrentTask(){
		return this.currentTask;
	}

	/**
	 * バックグラウンドでエラーが起きていた場合はダイアログを表示する
	 */
	public void showMessegeDialogIfTaskEndedByError(){
		if ((currentTask != null) && (currentTask.isError())){
			String errorMessage = currentTask.getErrorMessage();
			JOptionPane.showMessageDialog(null, limitMessageLength(errorMessage));
		}
	}

	/**
	 * 画面を活性化または非活性化する
	 */
	public void setEnabled(boolean enable){
		if (messageLabel != null) messageLabel.setText(makeOperationMessage(enable));
		if (reportButton != null) reportButton.setEnabled(enable);
		if (exitButton != null) exitButton.setEnabled(enable);
		if (projectFolderSelector != null) projectFolderSelector.setEnabled(enable);
		if (templateFolderSelector != null) templateFolderSelector.setEnabled(enable);
	}

	/**
	 * メッセージを設定する
	 * @param message
	 */
	public void setMessage(String message){
		if (this.messageLabel != null){
			this.messageLabel.setText(BdMessageResource.get("status.title") + message);
		}
	}

	/**
	 * プログレスモニタを応答する
	 * @return
	 */
	public ProgressMonitor getProgressMonitor() {
		return progressMonitor;
	}

	/**
	 * プログレスモニタを設定する
	 * @param progressMonitor
	 */
	public void setProgressMonitor(ProgressMonitor progressMonitor) {
		this.progressMonitor = progressMonitor;
	}

	/**
	 * 終了動作（インターセプト）
	 */
	@Override
	public void dispose(){
		NdLogger.getInstance().flushLog();
		super.dispose();
	}

	/**
	 * 終了ボタンアクション
	 */
	public void exitAction(){
		NdLogger.getInstance().flushLog();
		this.dispose();
		System.exit(0);
	}
	
	//BdPropertyFieldPanel配置の上部インセットの値
	private static final int topInset = 20;

	/**
	 * 画面を構築する
	 */
	private void buildWindow(){
		this.setTitle(BdConstants.TITLE + "  ver" + BdConstants.VERSION);
		JPanel basePanel = new JPanel(new BorderLayout());
		basePanel.setBorder(new EmptyBorder(15,5,10,5));
		this.getContentPane().add(basePanel);
		//パネルを3段の領域にする（横方向にのみリサイズするため）
		JPanel rowPanel1 = new JPanel(new BorderLayout());
		JPanel rowPanel2 = new JPanel(new BorderLayout());
		JPanel rowPanel2_1 = new JPanel(new BorderLayout());
		JPanel rowPanel3 = new JPanel(new BorderLayout());
		JPanel rowPanel3_1 = new JPanel(new BorderLayout());
		JPanel rowPanel4 = new JPanel(new BorderLayout());
		JPanel rowPanel4_1 = new JPanel(new BorderLayout());
		JPanel rowPanel5 = new JPanel(new BorderLayout());
		JPanel rowPanel5_1 = new JPanel(new BorderLayout());
		JPanel rowPanel6 = new JPanel(new BorderLayout());
		JPanel rowPanel7 = new JPanel(new BorderLayout());
		JPanel rowPanel8 = new JPanel(new BorderLayout());
		basePanel.add(rowPanel1, BorderLayout.NORTH);
		rowPanel1.add(rowPanel2, BorderLayout.SOUTH);
		rowPanel2.add(rowPanel2_1, BorderLayout.SOUTH);
		rowPanel2_1.add(rowPanel3, BorderLayout.SOUTH);
		rowPanel3.add(rowPanel3_1, BorderLayout.SOUTH);
		rowPanel3_1.add(rowPanel4, BorderLayout.SOUTH);
		rowPanel4.add(rowPanel4_1, BorderLayout.SOUTH);
		rowPanel4_1.add(rowPanel5, BorderLayout.SOUTH);
		rowPanel5.add(rowPanel5_1, BorderLayout.SOUTH);
		rowPanel5_1.add(rowPanel6, BorderLayout.SOUTH);
		rowPanel6.add(rowPanel7, BorderLayout.SOUTH);
		rowPanel7.add(rowPanel8, BorderLayout.SOUTH);
		//メッセージ部
		messageLabel = new JLabel();
		rowPanel1.add(messageLabel, BorderLayout.NORTH);
		messageLabel.setBorder(new EmptyBorder(0,5,0,0));
		//出力先ディレクトリ選択部
		projectFolderSelector = new BdProjectFolderSelector(this);
		rowPanel2.add(projectFolderSelector, BorderLayout.NORTH);
		projectFolderSelector.setBorder(new EmptyBorder(20,5,5,5));
		rowPanel2_1.add(this.createExplanationLabel(BdMessageResource.get("explanation.output"))); //"<html>[説明] Webアプリケーションの出力先を指定します。 [例] C:\\MyWebApplications"
		//テンプレートディレクトリ選択部
//		templateFolderSelector = new BdTemplateFolderSelector(this);
//		rowPanel3.add(templateFolderSelector, BorderLayout.NORTH);
//		templateFolderSelector.setBorder(new EmptyBorder(20,5,5,5));
//		rowPanel3_1.add(this.createExplanationLabel("[説明] 固定"));
		//上の4行コメントアウトし、下の3行を追加。2016.11.2 テンプレートフォルダを固定にするため
		JLabel fixedFolderLabel = new JLabel(BdMessageResource.get("title.fixed")); //"(2) 現在固定"
		fixedFolderLabel.setBorder(new EmptyBorder(20,5,5,5));
		rowPanel3.add(fixedFolderLabel);

		//グループID
		groupIdPanel= new BdPropertyFieldPanel(BdMessageResource.get("title.groupid"), topInset); //"(3) GroupId："
		rowPanel4.add(groupIdPanel);
		groupIdPanel.setValue(getOldGroupId());
		rowPanel4_1.add(this.createExplanationLabel(BdMessageResource.get("explanation.groupid"))); //"[説明] パッケージ名を指定します。 [例] jp.co.nextdesign"
		//アーティファクトID
		artifactIdPanel= new BdPropertyFieldPanel(BdMessageResource.get("title.artifactid"), topInset); //"(4) ArtifactId："
		rowPanel5.add(artifactIdPanel);
		artifactIdPanel.setValue(getOldArtifactId());
		rowPanel5_1.add(this.createExplanationLabel(BdMessageResource.get("explanation.artifactid"))); //"[説明] システム名を指定します。 [例] app1"
		//ボタン部
		JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rowPanel6.add(buttonsPanel, BorderLayout.NORTH);
		rowPanel6.setBorder(new EmptyBorder(15,0,0,0));
		//設定ボタン
		openPropertyWindowButton = new JButton(BdMessageResource.get("button.othersettings")); //"他の設定"
		buttonsPanel.add(openPropertyWindowButton);
		openPropertyWindowButton.addActionListener(new BdOpenPropertyWindowActionListener(this));
		//レポート作成ボタン
		reportButton = new JButton(BdMessageResource.get("button.createorupdate")); //" 作成／更新 "
		buttonsPanel.add(reportButton);
		reportButton.addActionListener(new BdBuildActionListener(this));
		//終了ボタン
		exitButton = new JButton(BdMessageResource.get("button.close")); //" 閉じる "
		buttonsPanel.add(exitButton);
		exitButton.addActionListener(new BdExitActionListener(this));
		//閉じるボタン・終了メニュー時のメッセージ表示
		this.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e){
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, BdMessageResource.get("warning.onclose")); //"終了する場合は右中段の「閉じる」ボタンをクリックしてください。"
			}
		});
		//説明部
		JLabel howtoLabel = new JLabel();
		Font howtoLabelCurrentFont = howtoLabel.getFont();
		howtoLabel.setFont(new Font(howtoLabelCurrentFont.getFontName(), Font.PLAIN, howtoLabelCurrentFont.getSize()));
		howtoLabel.setText(
				BdMessageResource.get("explanation.line1") + 
				BdMessageResource.get("explanation.line2") + 
				BdMessageResource.get("explanation.line3") + 
				BdMessageResource.get("explanation.line4") + 
				BdMessageResource.get("explanation.line5") + 
				BdMessageResource.get("explanation.line6") + 
				BdMessageResource.get("explanation.line7") + 
				BdMessageResource.get("explanation.line8") + 
				BdMessageResource.get("explanation.line9") + 
				BdMessageResource.get("explanation.line10"));
				
//		howtoLabel.setText(""
//				+ "<html>操作例"
//				+ "<br>① 入力項目(1)(3)(4)に各[例]を参考に入力します。"
//				+ "<br>② [作成／更新]ボタンを押下します。"
//				+ "<br>③ (1)出力先にWebアプリケーションフォルダが作成されます。[例]C:\\MyWebApplications\\app1"
//				+ "<br>④ 作成されたWebアプリケーションフォルダの内容はEclipseプロジェクトとして構成されています。"
//				+ "<br>⑤ Eclipseの[既存のプロジェクトをワークスペースへ]操作でインポートします。2回目以降の繰り返しでは[リフレッシュ]操作です。"
//				+ "<br>⑥ Eclipseで動作確認し、必要に応じてドメインモデルを追加・更新し、②に戻り繰り返します。"
//				+ "<br>※ インポートしたプロジェクトはEclipse上で[サーバで実行]や[サーバでデバッグ]できます。"
//				+ "<br>※ ①時点ですでにフォルダが存在する場合は、⑥で追加・更新されたドメインクラスをもとに内容は上書き更新されます。"
//				+ "</html>");
		rowPanel7.add(howtoLabel);
		rowPanel7.setBorder(new EmptyBorder(10,5,0,5));

		//PR部
		JLabel prLabel = new JLabel();
		Font prLabelCurrentFont = prLabel.getFont();
		prLabel.setFont(new Font(prLabelCurrentFont.getFontName(), Font.PLAIN, prLabelCurrentFont.getSize() - 2));
		
		prLabel.setText(
				BdMessageResource.get("license.line1") +
				BdMessageResource.get("license.line2") +
				BdMessageResource.get("license.line3") +
				BdMessageResource.get("license.line4"));		

//		prLabel.setText("<html>※DDBuilder は無料・無保証のソフトウェアです。"
//				+ "<br>※本ソフトウエアの使用に関し、お客様に生じた損害に対する賠償およびその他の責任は一切負わないものとします。"
//				+ "<br>※本ソフトウェアでは、Apache Wicket, Java EE JPA/Hibernate, Apache Derby, Eclipse を使用しています。"
//				+ "<br>※開発者: http://www.nextdesign.co.jp</html>");
		rowPanel8.add(prLabel);
		rowPanel8.setBorder(new EmptyBorder(10,5,0,5));

		//表示する
		this.setPreferredSize(new Dimension(800, 660));
		this.pack();
		this.setEnabled(true);
	}

	/**
	 * 説明文ラベルを作成し応答する
	 * @param text
	 * @return
	 */
	private JLabel createExplanationLabel(String text){
		JLabel result = new JLabel();
		Font currentFont = result.getFont();
		Font explanationFont = new Font(currentFont.getFontName(), Font.PLAIN, currentFont.getSize() - 1);
		result.setBorder(new EmptyBorder(0, 20, 0, 0));
		result.setFont(explanationFont);
		result.setText(text);
		return result;
	}

	/**
	 * ディレクトリ設定情報を応答する
	 * @return
	 */
	public BdBuilderSetting getBuilderSetting() {
		return directorySetting;
	}

	/**
	 * グループIDを応答する
	 * @return
	 */
	public String getGroupId(){
		return groupIdPanel.getStringValue();
	}

	/**
	 * 前回のグループIDを応答する
	 * @return
	 */
	public String getOldGroupId(){
		String result = "";
		if (this.getBuilderSetting() != null){
			result = this.getBuilderSetting().getGroupId();
		}
		return result;
	}

	/**
	 * アーティファクトIDを応答する
	 * @return
	 */
	public String getArtifactId(){
		return artifactIdPanel.getStringValue();
	}

	/**
	 * 前回のアーティファクトIDを応答する
	 * @return
	 */
	public String getOldArtifactId(){
		String result = "";
		if (this.getBuilderSetting() != null){
			result = this.getBuilderSetting().getArtifactId();
		}
		return result;
	}

	/**
	 * コンストラクタ
	 * @param lastSetting
	 */
	public BdMainWindow(BdBuilderSetting lastSetting){
		super();
		//Toolkit toolkit = Toolkit.getDefaultToolkit();
		ImageIcon icon = new ImageIcon("./images/ddb.png");
		this.setIconImage(icon.getImage());
		this.directorySetting = lastSetting;
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		buildWindow();
		this.setLocationRelativeTo(null);
	}

	/**
	 * メイン画面を開始する
	 * @param args
	 */
	public static void main(String[] args){
		try{
			setUiDefaults();
			initLoggingMode();
			BdBuilderSetting lastSetting = BdBuilderSetting.retrieveLastSetting();
			BdMainWindow mainWindow = new BdMainWindow(lastSetting);
			mainWindow.setVisible(true);
		} catch(BdUiException ex){
			JOptionPane.showMessageDialog(null, limitMessageLength(ex.toString()));
		} catch(NdUtilException ex) {
			JOptionPane.showMessageDialog(null, limitMessageLength(ex.toString()));
		} catch(Exception ex){
			JOptionPane.showMessageDialog(null, limitMessageLength(ex.toString()));
		}
	}

	private static void setUiDefaults(){
		System.setProperty("awt.useSystemAAFontSettings",  "lcd");
		UIDefaults uiDefault = UIManager.getDefaults();
		uiDefault.put("Label.font", new Font("Meiryo", Font.PLAIN, 12));
		uiDefault.put("Panel.font", new Font("Meiryo", Font.PLAIN, 12));
		uiDefault.put("Button.font", new Font("Meiryo", Font.PLAIN, 12));
		uiDefault.put("TextArea.font", new Font("Meiryo", Font.PLAIN, 12));
		uiDefault.put("TextField.font", new Font("Meiryo", Font.PLAIN, 12));
	}

	/**
	 * 画面を出ない程度の行数にする
	 * @param ex
	 * @return
	 */
	private static String limitMessageLength(String msg){
		String result = "";
		if (msg != null){
			String[] splitted = msg.split(BdConstants.CR);
			int lineCount = splitted.length;
			if (lineCount > MESSAGE_MAX_LINES){
				lineCount = MESSAGE_MAX_LINES;
			}
			for(int i=0; i<lineCount; i++){
				result += BdConstants.CR +  splitted[i];
			}
		} else {
			result = msg;
		}
		return result;
	}
	
	/**
	 * ログ出力レベルを設定する NdLoggingが参照する
	 * @throws BdApplicationPropertyException 
	 */
	private static void initLoggingMode() throws BdApplicationPropertyException{
		if (BdApplicationProperty.getInstance().isProduct()){
			NdLogger.getInstance().setFileLogging(true);
			NdLogger.getInstance().setTimeStampLogging(true);
			NdLogger.getInstance().setInfoLogging(true);
			NdLogger.getInstance().setDebugLogging(false);
			NdLogger.getInstance().setErrorLogging(true);
		} else {
			NdLogger.getInstance().setFileLogging(false);
			NdLogger.getInstance().setTimeStampLogging(false);
			NdLogger.getInstance().setInfoLogging(false);
			NdLogger.getInstance().setDebugLogging(false);
			NdLogger.getInstance().setErrorLogging(true);
		}
	}
}
