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
package jp.co.nextdesign.ddb.ui.main;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

import jp.co.nextdesign.ddb.BdApplicationProperty;
import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.BdConstants;
import jp.co.nextdesign.ddb.BdMessageResource;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.template.BdGeneratedFile;
import jp.co.nextdesign.ddb.core.template.BdTemplateProject;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserProject;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.jcr.NdApplicationPropertyException;
import jp.co.nextdesign.jcr.documentation.NdDocumentationException;
import jp.co.nextdesign.jcr.documentation.classifier.NdClassifierReport;
import jp.co.nextdesign.jcr.documentation.project.NdProjectReport;
import jp.co.nextdesign.jcr.model.core.NdAbstractClassifier;
import jp.co.nextdesign.jcr.model.core.NdAbstractNamedElement;
import jp.co.nextdesign.jcr.model.core.NdEnum;
import jp.co.nextdesign.jcr.model.core.NdInterface;
import jp.co.nextdesign.jcr.model.core.NdModelException;
import jp.co.nextdesign.jcr.model.core.NdPackage;
import jp.co.nextdesign.jcr.model.core.NdProject;
import jp.co.nextdesign.jcr.model.core.NdProjectManager;
import jp.co.nextdesign.jcr.parser.NdParserException;
import jp.co.nextdesign.util.BdFileUtil;
import jp.co.nextdesign.util.NdFileUtil;
import jp.co.nextdesign.util.NdFileUtilStringReplacer;
import jp.co.nextdesign.util.NdUtilException;
import jp.co.nextdesign.util.logging.BdDdbRuleCheckLogger;
import jp.co.nextdesign.util.logging.NdLogger;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

/**
 * ビルド処理ワーカ 処理の中断や、進捗表示を実現するために、メインスレッドとは別のスレッドで実行するためのワーカーオブジェクト
 * 
 * @author murayama
 */
class BdBuilderTask extends SwingWorker<Void, Void> {

	private BdMainWindow mainWindow;
	private String currentTaskStateName;
	private boolean isError = false;
	private String errorMessage;

	/**
	 * タスクの状態名を応答する
	 * 
	 * @return
	 */
	public String getCurrentTaskStateName() {
		return currentTaskStateName;
	}

	/**
	 * エラー終了か否かを応答する
	 * 
	 * @return
	 */
	public boolean isError() {
		return isError;
	}

	/**
	 * エラー情報をクリアする
	 */
	private void clearError() {
		this.isError = false;
		this.errorMessage = "";
		BdDdbRuleCheckLogger.getInstance().clear();
	}

	/**
	 * エラーメッセージを応答する
	 * 
	 * @return
	 */
	public String getErrorMessage() {
		return errorMessage;
	}

	// 進捗率の配分
	private static final int PARSE_PROGRESS_STEP1_VAL = 2;
	private static final int PARSE_PROGRESS_STEP2_VAL = 5;
	private static final int PARSE_PROGRESS_STEP3_VAL = 9;
	private static final int PARSE_PROGRESS_START_VAL = 10;
	private static final int PARSE_PROGRESS_END_VAL = 47;
	private static final int ANALYZE_PROGRESS_END_VAL = 48;
	private static final int GENERATE_PROGRESS_1_END_VAL = 50;
	private static final int GENERATE_PROGRESS_2_END_VAL = 80;
	private static final int GENERATE_PROGRESS_3_END_VAL = 90;
	private static final int GENERATE_PROGRESS_4_END_VAL = 99;
	// import文解決のため再度userProjectを解析しjavaファイルを更新するときの進捗率
	//完了
	private static final int PROGRESS_END_VAL = 100;

	/**
	 * バックグラウンド処理 this.setProgress() -->
	 * NdReportTaskPropertyChangeListener#propertyChange()
	 * ----------------------------------------------------------
	 * JClassReport機能がデグレードしていないかチェックする場合は、 JClassReportの出力結果を比較する。
	 * テスト用コードと正しい出力結果(EXCELとCSV)は、以下に保管してある。
	 * C:\\_dev\\JClassReport_TestSource\\TestSource\\src
	 * C:\\_dev\\JClassReport_TestSource\\TestSource2\\src
	 * C:\\_dev\\JCR_DDB_Output_比較用\\jsr\\testSource
	 * C:\\_dev\\JCR_DDB_Output_比較用\\jcr\\testSource2
	 * 比較対象は、以前にJClassReportで出力結果とそのCSVは以下に保管してある。
	 * COMPARE_WITH_JCLASSREPORT=trueにすると、 JClassReportとして以下の場所に結果を出力するようになる。
	 * ここをCsvToolでCSV化してWinMergeでCSVのみ比較すれば、デグレードチェックできる。
	 * C:\\_dev\\JCR_DDB_Output_比較用\\ddb\\testSource
	 * C:\\_dev\\JCR_DDB_Output_比較用\\ddb\\testSource2
	 * ※testSourceとtestSource2でそれぞれ実行する必要がある。
	 * ---------------------------------------------------------- デグレードチェックとは
	 * DDBuilderはJClassReportを含むが、DDBuilderの開発作業によって、
	 * JClassReport部分にデグレードが生じていないかをチェックすることである。
	 * ----------------------------------------------------------
	 */
	@Override
	public Void doInBackground() {
		boolean CHECK_JCR = false; //<--- JCRデグレードチェック用
		try {
			this.clearError();
			int progressPercentage = 0;
			this.currentTaskStateName = BdMessageResource.get("builder.analysing"); //"ソース解析中  ";
			// テンプレートプロジェクトをロードして必須ディレクトリと必須ファイルを追加する
			// 新規の場合はこのステップにおいてユーザプロジェクトが新規作成される
			// このステップはユーザプロジェクトのロードよりも先に実行されなければならない
			// 実行順序が逆になると、新規の場合、ユーザプロジェクトを検出できなくなる
			// 新規の場合以外は、現実問題として、このステップによってユーザプロジェクトはなにも変化しない

			// (1)テンプレートプロジェクトを読込む
			BdTemplateProject templateProject = new BdTemplateProject(mainWindow.getBuilderSetting().getTemplateProjectFolder());

			this.setProgressIntercept(PARSE_PROGRESS_STEP1_VAL);
			// (2)ユーザプロジェクトを読込む
			BdUserProject userProject = new BdUserProject(
					mainWindow.getBuilderSetting().getUserProjectFolder(), 
					mainWindow.getBuilderSetting().getArtifactId(), 
					mainWindow.getBuilderSetting().getGroupId());

			this.setProgressIntercept(PARSE_PROGRESS_STEP2_VAL);
			// (3)必須のディレクトリとファイルを追加する
			this.addRequiredFiles(templateProject, userProject);

			NdLogger.getInstance().debug("****************************");
			NdLogger.getInstance().debug("JCR ANALYSE THE USER PROJECT"); //ユーザプロジェクトをJCRで解析
			NdLogger.getInstance().debug("****************************");
			this.setProgressIntercept(PARSE_PROGRESS_STEP3_VAL);
			// (4)ユーザプロジェクトのNdProject属性を作成する（BdUserProject.ndProject）
			userProject.createNdProject();

			// (5)ソース解析(JCR)
			int compilationUnitCount = userProject.getAllCompilationUnitCount();
			int parseCount = 0;
			while (userProject.parseNext()) { // Java解析
				parseCount++;
				progressPercentage = calculateProgressPercentage(
						PARSE_PROGRESS_START_VAL, PARSE_PROGRESS_END_VAL,
						parseCount, compilationUnitCount);
				this.setProgressIntercept(progressPercentage);
				if (this.isCancelled()) { //SwingWorker#isCancelled()
					break; //中断されましたメッセージはmainWindowに表示される
				}
			}
			this.setProgressIntercept(PARSE_PROGRESS_END_VAL);
			userProject.analyzeProject(); // 関連などの解析
			this.setProgressIntercept(ANALYZE_PROGRESS_END_VAL);

			// (6)NdProjectの(一部の)オブジェクトをBdUserProjectの該当クラスでラップする。
			// (6)を終了しないとドメインクラスツリーは未完成なので、this.checkDomainRuleは(6)以降で実行する必要がある。
			// しかし、BdUserAttributeのコンストラクタでddbKeyが""と判定された属性は、BdUserClassのattributeListに追加されず、すでに除外された状態になっている。
			// つまり、this.checkDomainRuleでチェックしようとしても取得できない。従って、BdDdbRuleCheckLoggerログを出せない。
			// そのため、BdUserClass#loadAttributes()でattributeListに追加しなかった属性については、BdUserClass#loadAttributes()でBdDdbRuleCheckLoggerログを追加している。
			// 但し、ドメインモデルは未完成のため、List<T>のTがドメインモデルであるか否かのチェックは出来ないので、this.checkDomainRuleでさらにちぇっくする。
			userProject.importNdProject();

			//デバッグ情報出力
			this.debug(templateProject, userProject);

			//(7)全てのドメインクラスが揃った後で可能なDDBルールチェック。BdDdbRuleCheckExceptionの場合は(8)以降はスキップする。
			userProject.checkDdbRule();

			//(8)生成1 ドメインモデル全体を元に生成するケース（プログレス表示のためにあえてここでループさせている処理もある）
			List<BdUserClass> targetDomainClassList = userProject.getEntityAnnotatedDomainClassList();
			List<BdGeneratedFile> genertedFileList1 = templateProject.generate(targetDomainClassList, userProject.getGroupId());
			//コードファイルを物理的に出力
			this.writeFiles(genertedFileList1, templateProject, userProject, ANALYZE_PROGRESS_END_VAL, GENERATE_PROGRESS_1_END_VAL);

			//(9)生成2 ドメインクラス毎にテンプレートを適用してコード生成するケース
			double startProgressVal = GENERATE_PROGRESS_1_END_VAL;
			double span =(GENERATE_PROGRESS_2_END_VAL - GENERATE_PROGRESS_1_END_VAL) / ((double)targetDomainClassList.size());
			double endProgressVal = startProgressVal + span;
			//ドメインクラスの数分繰り返す
			for(BdUserClass c : targetDomainClassList){
				//コードファイル生成
				List<BdGeneratedFile> genertedFileList2 = templateProject.generate(c, userProject.getGroupId());
				//コードファイルを物理的に出力
				startProgressVal += span;
				endProgressVal += span;
				this.writeFiles(genertedFileList2, templateProject, userProject, startProgressVal, endProgressVal);
			}

			//(10)生成3 サービスメソッド用のコードを生成する
			List<BdUserServiceMethod> userServiceMethodList = userProject.getServiceMethodList();
			List<BdGeneratedFile> genertedFileList3 = templateProject.generate4Service(userServiceMethodList, userProject.getGroupId());
			//コードファイルを物理的に出力
			this.writeFiles(genertedFileList3, templateProject, userProject, GENERATE_PROGRESS_2_END_VAL, GENERATE_PROGRESS_3_END_VAL);

			//(11)生成4 サービスメソッド毎にテンプレートを適用してコード生成するケース
			startProgressVal = GENERATE_PROGRESS_3_END_VAL;
			span =(GENERATE_PROGRESS_4_END_VAL - GENERATE_PROGRESS_3_END_VAL) / ((double)userServiceMethodList.size());
			endProgressVal = startProgressVal + span;
			//サービスメソッドの数分繰り返す
			for(BdUserServiceMethod m : userServiceMethodList){
				//コードファイル生成
				List<BdGeneratedFile> genertedFileList4 = templateProject.generate(m, userProject.getGroupId());
				//コードファイルを物理的に出力
				startProgressVal += span;
				endProgressVal += span;
				this.writeFiles(genertedFileList4, templateProject, userProject, startProgressVal, endProgressVal);
			}

			this.setProgressIntercept(PROGRESS_END_VAL);

			// EXCELレポートを出力するか否か
			if (CHECK_JCR) {
				doJcr( ANALYZE_PROGRESS_END_VAL, PROGRESS_END_VAL);
			}
		} catch(BdDdbRuleCheckException ex) {
			this.finalizeOnException(ex);
		} catch (BdBuilderException ex) {
			this.finalizeOnException(ex);
		} catch (NdParserException ex) {
			this.finalizeOnException(ex);
		} catch (NdModelException ex) {
			this.finalizeOnException(ex);
		} catch (NdUtilException ex) {
			this.finalizeOnException(ex);
		} catch (Exception ex) {
			this.finalizeOnException(ex);
		} finally {
			this.setProgressIntercept(PROGRESS_END_VAL);
		}
		return null;
	}

	/** 進捗率デバッグのため */
	private void setProgressIntercept(int progress){
		//		try{
		//			Thread.sleep(100);
		//		} catch(InterruptedException e) {
		//		}
		//		System.out.println("progress=" + progress);
		this.setProgress(progress);
	}

	/** コードファイルを物理的に出力する */
	private void writeFiles(List<BdGeneratedFile> fileList, BdTemplateProject templateProject, BdUserProject userProject, double startProgressVal, double endProgressVal) 
			throws NdUtilException, BdApplicationPropertyException{
		String characterCode = BdApplicationProperty.getInstance().getCharacterCode();
		int writtenCount = 0;
		for(BdGeneratedFile genertedFile : fileList){
			File toFile = new File(this.convertTemplatePathToUserPath(genertedFile.getFullPathName(), templateProject, userProject));
			//TestDataServiceは上書きしない
			if (genertedFile.isOverwriteIfExists() || !toFile.exists()){
				NdFileUtil.writeFile(genertedFile, toFile, characterCode);
			}
			writtenCount++;
			int progressPercentage = calculateProgressPercentage(startProgressVal, endProgressVal, writtenCount, fileList.size());
			this.setProgressIntercept(progressPercentage);
		}
	}

	/**
	 * 必須のディレクトリとファイルをユーザプロジェクトに追加する
	 * @param templateProject
	 * @param userProject
	 * @throws NdUtilException 
	 * @throws BdApplicationPropertyException 
	 * @throws IOException 
	 */
	private void addRequiredFiles(BdTemplateProject templateProject, BdUserProject userProject) 
			throws NdUtilException, BdApplicationPropertyException, IOException {
		for(File fromDir : templateProject.getRequiredDirestories()){
			File toDir = new File(this.convertTemplatePathToUserPath(fromDir.getAbsolutePath(), templateProject, userProject));
			NdFileUtil.makeDirectory(toDir.getAbsolutePath());
		}
		String characterCode = BdApplicationProperty.getInstance().getCharacterCode();
		NdFileUtilStringReplacer replacer = createStringReplacer();
		NdFileUtilStringReplacer replacerForHtml = this.appendForDdbHtml(replacer);
		for(File fromFile : templateProject.getRequiredFiles()){
			File toFile = new File(this.convertTemplatePathToUserPath(fromFile.getAbsolutePath(), templateProject, userProject));
			if (! isOssJarFile(fromFile) && ! BdFileUtil.isImageFile(fromFile)){
				if (toFile.exists()) {
					toFile.delete();
				}
				if (BdFileUtil.isHtmlFile(fromFile)){
					NdFileUtil.copyFileUsingReplacer(fromFile, toFile, characterCode, replacerForHtml);
				} else {
					NdFileUtil.copyFileUsingReplacer(fromFile, toFile, characterCode, replacer);
				}
			} else {
				//OSS Jar Fileの場合
				Path sourcePath = FileSystems.getDefault().getPath(fromFile.getAbsolutePath());
				Path outPath = FileSystems.getDefault().getPath(toFile.getAbsolutePath());	
				if (!Files.exists(outPath)){
					Files.copy(sourcePath, outPath, StandardCopyOption.REPLACE_EXISTING);
				}
			}
		}
	}

	/** oss jarファイルか否か */
	private boolean isOssJarFile(File f){
		return f != null && "lib".equals(f.getParentFile().getName());
	}
	
	/**
	 * テンプレートファイルをユーザプロジェクトにコピーする前にファイル内容の置換パターンを定義する
	 * @return
	 * @throws BdApplicationPropertyException 
	 */
	private NdFileUtilStringReplacer createStringReplacer() throws BdApplicationPropertyException{
		NdFileUtilStringReplacer result = new NdFileUtilStringReplacer();
		result.addReplacementPattern(BdTemplateProject.ARTIFACT_ID, mainWindow.getBuilderSetting().getArtifactId());
		result.addReplacementPattern(BdTemplateProject.GROUP_ID, mainWindow.getBuilderSetting().getGroupId());
		result.addReplacementPattern("%DDB_VERSION%", BdConstants.VERSION);
		result.addReplacementPattern("%FILE_GENERATED_AT%", BdMainWindow.FILE_GENERATED_AT);
		//次の置換パターンは、Wicketアプリケーションのホームページクラス指定（DdApplication#getHomePage()）をユーザが自作したページに変更するために必要 2017.1.17
		String homePageClassName = BdApplicationProperty.getInstance().getHomePageClassName();
		if (homePageClassName != null && !"".equals(homePageClassName)){
			result.addReplacementPattern("return DdHomePage.class;", "return " + homePageClassName + ".class;");
		}
		try{
			String applicationName = BdApplicationProperty.getInstance().getSystemName();
			if (!applicationName.isEmpty()){
				result.addReplacementPattern("%仮アプリケーション名%", applicationName);
			}
		} catch(BdApplicationPropertyException ex){
			NdLogger.getInstance().error("DDBApplication.propertiesの読取りエラー", ex);
		}
		return result;
	}
	
	/**
	 * 与えられたNdFileUtilStringReplacerから新しいNdFileUtilStringReplacerを応答する。
	 * 新しいNdFileUtilStringReplacerには、ddbパッケージのHTML中のキーワードを多言語対応で置き換えるためのパターンが追加されている。
	 * @param original
	 * @return
	 */
	private NdFileUtilStringReplacer appendForDdbHtml(NdFileUtilStringReplacer original){
		NdFileUtilStringReplacer result = original.cloneNdFileUtilStringReplacer();
		int key=1;
		while(true){
			String val = BdMessageResource.get("html.ddb." + key++);
			if (val == null || val.length() < 1){
				break;
			}
			String[] splitted = val.split(",");
			result.addReplacementPattern(splitted[0], splitted[1]);
		}
		return result;
	}

	/*
	 * テンプレートプロジェクト用パスをユーザプロジェクト用パスに変換する
	 * @param templatePath
	 * @return
	 */
	private String convertTemplatePathToUserPath(String templatePath, BdTemplateProject templateProject, BdUserProject userproject){
		String templateProjectRootPath = templateProject.getProjectRootFullPath();
		String userProjectRootPath = userproject.getProjectRootFullPath();
		String result = templatePath.replace(templateProjectRootPath, userProjectRootPath); //templateディレクトリ絶対パス部分をユーザプロジェクトフォルダの絶対パスに置き換える
		String userGroupIdPath = mainWindow.getBuilderSetting().getGroupId().replaceAll("\\.", Matcher.quoteReplacement(File.separator));
		result = result.replace(BdTemplateProject.GROUP_ID_PATH, userGroupIdPath); //"jp.co.nextdesign"部分をユーザ指定のGroupIdに置き換える
		return result;
	}

	/**
	 * JClassReportデグレードテスト
	 * JCRのテスト用ソースコードのレポートを作成する。
	 * JClassReportの出力結果とWinMerge等で比較してデグレードが起きていないかチェックする。
	 * @param startProgressVal
	 * @param endProgressVal
	 * @throws NdApplicationPropertyException
	 * @throws InvalidFormatException
	 * @throws NdDocumentationException
	 * @throws IOException
	 * @throws NdUtilException 
	 * @throws NdParserException 
	 * @throws BdBuilderException 
	 * @throws NdModelException 
	 */
	private void doJcr(int startProgressVal, int endProgressVal) 
			throws NdApplicationPropertyException, InvalidFormatException, NdDocumentationException, IOException, NdParserException, NdUtilException, NdModelException, BdBuilderException {
		//-------------------------------------------------------------------------------------------------------------
		//File inputDirectory = new File("C:\\_dev\\JClassReport_TestSource\\TestSource\\src");
		//File outputDirectory = new File(	"C:\\_dev\\JCR_DDB_Output_比較用\\ddb\\testSource");
		// File inputDirectory = new File("C:\\_dev\\JClassReport_TestSource\\TestSource2\\src");
		// File outputDirectory = new File("C:\\_dev\\JCR_DDB_Output_比較用\\ddb\\testSource2");
		//-------------------------------------------------------------------------------------------------------------

		int progressPercentage = startProgressVal;		
		File inputDirectory = null;
		File outputDirectory = null;

		//テストソース1と2、2回実行する
		for(int testCaseCount=1; testCaseCount<=2; testCaseCount++){
			if (testCaseCount == 1){
				inputDirectory = new File("C:\\_dev\\JClassReport_TestSource\\TestSource\\src");
				outputDirectory = new File(	"C:\\_dev\\JCR_DDB_Output_比較用\\ddb\\testSource");
			}else{
				inputDirectory = new File("C:\\_dev\\JClassReport_TestSource\\TestSource2\\src");
				outputDirectory = new File("C:\\_dev\\JCR_DDB_Output_比較用\\ddb\\testSource2");
			}
			if (!outputDirectory.exists()){
				outputDirectory.mkdir();
			}

			NdProject ndProject = NdProjectManager.getInstance().createProject(inputDirectory);

			// (5)ソース解析(JCR)
			int compilationUnitCount = ndProject.getAllCompilationUnitCount();
			int parseCount = 0;
			while (ndProject.parseNext()) { // Java解析
				parseCount++;
				progressPercentage = calculateProgressPercentage(
						PARSE_PROGRESS_START_VAL, PARSE_PROGRESS_END_VAL,
						parseCount, compilationUnitCount);
				this.setProgressIntercept(progressPercentage);
				if (this.isCancelled()) {
					break;
				}
			}

			// EXCELレポートを出力する
			if (!this.isCancelled()) {
				this.currentTaskStateName = BdMessageResource.get("builder.writingFile"); //"ソース解析中 > ファイル出力中  ";
				NdPackage rootPackage = ndProject.getRootPackage();
				if (rootPackage != null) {
					List<NdAbstractNamedElement> elementList = rootPackage.getNamedElementList();
					List<NdAbstractClassifier> classifierList = new ArrayList<NdAbstractClassifier>();
					for (int i = 0; i < elementList.size(); i++) {
						NdAbstractNamedElement element = elementList.get(i);
						if (element instanceof NdAbstractClassifier) {
							NdAbstractClassifier classifier = (NdAbstractClassifier) element;
							if (classifier.isSubjectOfClassifierReport()) {
								classifierList.add(classifier);
							}
						}
					}
					this.setProgressIntercept(PARSE_PROGRESS_END_VAL);
					ndProject.analyzeProject(); // 関連などの解析
					this.setProgressIntercept(ANALYZE_PROGRESS_END_VAL);					

					int totalOutputFileCount = classifierList.size() + 1; // プロジェクトレポート＋クラスレポート
					int writeCount = 0;
					// プロジェクトレポート出力
					NdProjectReport projectReport = new NdProjectReport(ndProject, outputDirectory);
					projectReport.write();
					writeCount++;
					progressPercentage = this.calculateProgressPercentage(startProgressVal, endProgressVal, writeCount, totalOutputFileCount);
					this.setProgressIntercept(progressPercentage);
					// クラスレポート出力
					for (int i = 0; i < classifierList.size(); i++) {
						if (this.isCancelled()) {
							break;
						}
						NdClassifierReport classifierReport = new NdClassifierReport(classifierList.get(i), outputDirectory);
						classifierReport.write();
						writeCount++;
						progressPercentage = this.calculateProgressPercentage(startProgressVal, endProgressVal, writeCount,	totalOutputFileCount);
						this.setProgressIntercept(progressPercentage);
					}
				}
			}
		}
		this.setProgressIntercept(endProgressVal);
	}

	/**
	 * 例外時の終了処理を行う
	 * 
	 * @param ex
	 */
	private void finalizeOnException(Exception ex) {
		this.isError = true;
		if (ex instanceof BdDdbRuleCheckException){
			this.errorMessage = ((BdDdbRuleCheckException)ex).getApplMessage();
		} else {
			this.errorMessage = this.getStackTraceOf(ex);
		}
		this.setProgressIntercept(PROGRESS_END_VAL);
	}

	/**
	 * 進捗率を計算する
	 * 
	 * @param min
	 * @param max
	 * @param processed
	 * @param total
	 * @return
	 */
	private int calculateProgressPercentage(double min, double max, int processed, int total) {
		int result = (int) ((min + (max - min) * (processed / (double) total)) + 0.5);
		if (result > 100) {
			result = 100;
		}
		return result;
	}

	/**
	 * バックグラウンド処理終了時のコールバック
	 */
	@Override
	public void done() {
		Toolkit.getDefaultToolkit().beep();
		mainWindow.showMessegeDialogIfTaskEndedByError();
		mainWindow.setEnabled(true); // メイン画面を活性化する
		for(String msg : NdLogger.getInstance().getUserMessages()){
			System.out.println(msg);
		}
		String msg = "";
		if (BdDdbRuleCheckLogger.getInstance().hasLog()){
			for(String log : BdDdbRuleCheckLogger.getInstance().getLogList()){
				msg += BdConstants.CR + log;
			}
		} else {
			msg = BdMessageResource.get("builder.completed"); //"完了しました。";
		}
		Toolkit.getDefaultToolkit().beep();
		JOptionPane.showMessageDialog(null, msg);
	}

	/**
	 * コンストラクタ
	 * 
	 * @param mainWindow
	 */
	public BdBuilderTask(BdMainWindow mainWindow) {
		super();
		this.mainWindow = mainWindow;
		this.currentTaskStateName = "";
		this.clearError();
	}

	/**
	 * Exception#printStackTrace文字列を取得する
	 * 
	 * @param ex
	 * @return
	 */
	private String getStackTraceOf(Exception ex) {
		String result = "";
		if (ex != null) {
			StringWriter stringWriter = new StringWriter();
			PrintWriter printWriter = new PrintWriter(stringWriter);
			ex.printStackTrace(printWriter);
			result += stringWriter.toString();
		}
		return result;
	}

	/** デバッグ */
	private void debug(BdTemplateProject templateProject, BdUserProject userProject){
		//デバッグ情報の出力有無はBdMainWindow#initLoggingMode()でセットしている
		//ここで参照しているBdApplicationPropertyの各boolean値はJCRに影響しないように追加した。
		//注意：NdLoggerを使用しないのでログファイルに出ることはない。
		if (BdApplicationProperty.LOG_TEMPLATE_PROJECT_DUMP) {
			templateProject.debugPrint();
		}
		if (BdApplicationProperty.LOG_USER_PROJECT_DUMP) {
			userProject.debugPrint();
		}
		//ドメイン
		if (BdApplicationProperty.LOG_ENTITY_AND_ALL_DOMAIN_CLASS_LIST){
			System.out.println("AllDomainClassList ----------");
			int index = 1;
			for(BdUserClass bduc : userProject.getAllDomainClassList()){
				System.out.println("AllDomainClassList[" + index + "] " +  bduc.getNdClass().getQualifiedName());
				index++;
			}
			System.out.println("EntityAnnotatedDomainClassList ----------");
			index = 1;
			for(BdUserClass bduc : userProject.getEntityAnnotatedDomainClassList()){
				System.out.println("EntityAnnotatedDomainClassList[" + index + "] " +  bduc.getNdClass().getQualifiedName());
				index++;
			}
			System.out.println("AllDomainEnumList ----------");
			index = 1;
			for(NdEnum nde : userProject.getAllDomainEnumList()){
				System.out.println("AllDomainEnumList[" + index + "] " +  nde.getQualifiedName());
				index++;
			}
			System.out.println("AllDomainInterfaceList ----------");
			index = 1;
			for(NdInterface ndi : userProject.getAllDomainInterfaceList()){
				System.out.println("AllDomainInterfaceList[" + index + "] " +  ndi.getQualifiedName());
				index++;
			}
		}
		//サービス
		if (BdApplicationProperty.LOG_SERVICE_CLASS_LIST){
			System.out.println("AllServiceClassList ----------");
			int index = 1;
			for(BdUserClass bduc : userProject.getAllServiceClassList()){
				System.out.println("AllServiceClassList[" + index + "] " +  bduc.getNdClass().getQualifiedName());
				index++;
			}
			System.out.println("AppendedServiceClassList ----------");
			index = 1;
			for(BdUserClass bduc : userProject.getAppendedServiceClassList()){
				System.out.println("AppendedServiceClassList[" + index + "] " +  bduc.getNdClass().getQualifiedName());
				index++;
			}
		}
	}
}
