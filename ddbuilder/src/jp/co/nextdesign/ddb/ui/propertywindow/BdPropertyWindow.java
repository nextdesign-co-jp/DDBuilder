/*
 * DDBuilder
 * http://www.nextdesign.co.jp/ddd/index.html
 * Copyright 2015 NEXT DESIGN Ltd.
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package jp.co.nextdesign.ddb.ui.propertywindow;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import jp.co.nextdesign.ddb.BdApplicationProperty;
import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.ui.main.BdMainWindow;

/**
 * プロパティ設定画面
 * @author murayama
 */
public class BdPropertyWindow extends JDialog {
	
	private static final long serialVersionUID = 1L;
	private BdMainWindow mainWindow;
//	private BdPropertyFieldPanel warningMethodStepCountPanel;
//	private BdPropertyFieldPanel warningMethodNestCountPanel;
//	private BdPropertyFieldPanel characterCodeNamePanel;
	private BdPropertyFieldPanel systemNamePanel;
	private BdPropertyFieldPanel companyNamePanel;
	private BdPropertyFieldPanel homePageClassNamePanel;
//	private BdPropertyLabelPanel versionPanel;
//	private BdPropertyLabelPanel userNamePanel;

	/**
	 * コンストラクタ
	 * @param mainWindow
	 * @throws BdApplicationPropertyException 
	 */
	public BdPropertyWindow(BdMainWindow mainWindow) throws BdApplicationPropertyException{
		super(mainWindow);
		this.mainWindow = mainWindow;
		this.buildWindow();
		this.setValuesByApplicationProperties();
		this.setLocationRelativeTo(null);
	}
	
	//BdPropertyFieldPanel配置の上部インセットの値
	private static int topInset = 2;
	
	/**
	 * 画面を構築する
	 */
	private void buildWindow(){
		this.setTitle(BdMessageResource.get("propertywindow.title")); //"プロパティ設定"
		this.getContentPane().setLayout(new BorderLayout());
		JPanel mainPanel = new JPanel();
		mainPanel.setLayout(new GridLayout(8,1));
		mainPanel.setBorder(new EmptyBorder(10,0,0,0));
		this.add(mainPanel, BorderLayout.NORTH);
//		warningMethodStepCountPanel = new BdIntegerPropertyFieldPanel("警告メソッド行数：");
//		warningMethodNestCountPanel = new BdIntegerPropertyFieldPanel("警告ネストレベル：");
//		characterCodeNamePanel = new BdPropertyFieldPanel("Javaソースの文字コード（UTF-8,SJIS）：");
		systemNamePanel = new BdPropertyFieldPanel(BdMessageResource.get("title.applicationname"), topInset); //"アプリケーション名："
		companyNamePanel = new BdPropertyFieldPanel(BdMessageResource.get("title.companyname"), topInset); //"会社名："
		homePageClassNamePanel = new BdPropertyFieldPanel(BdMessageResource.get("title.startpage"), topInset); //"ホームページクラス完全名："
		
//		versionPanel = new BdPropertyLabelPanel("バージョン：" + BdConstants.VERSION);
//		userNamePanel = new BdPropertyLabelPanel("お客様名：" + NdKeyManager.getInstance().getKey().getUserName());
		
//		mainPanel.add(warningMethodStepCountPanel);
//		mainPanel.add(warningMethodNestCountPanel);
//		mainPanel.add(characterCodeNamePanel);
		mainPanel.add(systemNamePanel);
		mainPanel.add(companyNamePanel);
		mainPanel.add(homePageClassNamePanel);
		
//		mainPanel.add(versionPanel);
//		mainPanel.add(userNamePanel);
		
		mainPanel.add(this.createButtonPanel());
		this.setSize(700,400);
		//this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		this.addWindowCloseActionListener();
	}
	
	/**
	 * ウィンドウ右上の閉じるボタンをインターセプトする
	 */
	private void addWindowCloseActionListener(){
		this.addWindowListener(new WindowAdapter(){
			@Override public void windowClosing(WindowEvent e){
				Toolkit.getDefaultToolkit().beep();
				JOptionPane.showMessageDialog(null, BdMessageResource.get("propertywindow.dialog.message")); //"「設定」または「キャンセル」ボタンをクリックしてください。"
			}
		});
	}

	/**
	 * 現在のApplicationPropertiesの値をフィールドに設定する
	 * @throws BdApplicationPropertyException 
	 */
	void setValuesByApplicationProperties() throws BdApplicationPropertyException{
		BdApplicationProperty prop = BdApplicationProperty.getInstance();
//		warningMethodStepCountPanel.setValue(prop.getMethodSizeWarning());
//		warningMethodNestCountPanel.setValue(prop.getMethodNestWarning());
//		characterCodeNamePanel.setValue(prop.getCharacterCode());
		systemNamePanel.setValue(prop.getSystemName());
		companyNamePanel.setValue(prop.getCompanyName());
		homePageClassNamePanel.setValue(prop.getHomePageClassName());
	}
	
	/**
	 * フィールド値でApplicationPropertiesを更新する
	 * @throws BdApplicationPropertyException 
	 */
	void updateApplicationProperties() throws BdApplicationPropertyException{
		BdApplicationProperty prop = BdApplicationProperty.getInstance();
//		prop.setMethodSizeWarning(warningMethodStepCountPanel.getIntValue());
//		prop.setMethodNestWarning(warningMethodNestCountPanel.getIntValue());
//		prop.setCharacterCode(characterCodeNamePanel.getStringValue());
		prop.setSystemName(systemNamePanel.getStringValue());
		prop.setCompanyName(companyNamePanel.getStringValue());
		prop.setHomePageClassName(homePageClassNamePanel.getStringValue());
		prop.store();
	}

	/**
	 * ボタン部を作成する
	 * @return
	 */
	private JPanel createButtonPanel(){
		JPanel resultl = new JPanel();
		resultl.setBorder(new EmptyBorder(10,0,0,7));
		resultl.setLayout(new FlowLayout(FlowLayout.RIGHT));
		JButton completeButton = new JButton(BdMessageResource.get("button.ok")); //"完了"
		resultl.add(completeButton);
		completeButton.addActionListener(new BdCompleteActionListener(this));
		JButton cancelButton = new JButton(BdMessageResource.get("button.cancel")); //"キャンセル"
		resultl.add(cancelButton);
		cancelButton.addActionListener(new BdCancelActionListener(this));
		return resultl;
	}
	
	/**
	 * メインウィンドウのメッセージを設定する
	 * @param message
	 */
	void setMainWindowMessage(String message){
		if (this.mainWindow != null){
			this.mainWindow.setMessage(message);
		}
	}
}
