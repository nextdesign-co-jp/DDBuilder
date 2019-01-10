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
package jp.co.nextdesign.ddb.core.template;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jp.co.nextdesign.ddb.BdApplicationProperty;
import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.ddb.core.user.BdUserClass;
import jp.co.nextdesign.ddb.core.user.BdUserServiceMethod;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * テンプレートプロジェクト
 * @author murayama
 *
 */
public class BdTemplateProject {
	
	public static final String DOMAIN_PACKAGE_NAME = "domain";
	public static final String SERVICE_PACKAGE_NAME = "service";
	public static final String VIEW_PACKAGE_NAME = "view";
	public static final String DOMAIN_BASE_CLASS_NAME = "DdBaseEntity";
	public static final String REQUIRED_CLASS_NAME_PREFIX = "Dd";
	public static final String G_PACKAGE_NAME = "g";
	public static final String DDB_FW_PACKAGE_NAME = "ddb";
	public static final String DDB_SERVICE_PACKAGE_AND_HEADER = ".service.g.Dd";
	
	public static final String MAVEN_STANDARD_DIRECTORY_LAYOUT = "src.main.java";
	public static final String JP = MAVEN_STANDARD_DIRECTORY_LAYOUT + ".jp";
	public static final String CO = MAVEN_STANDARD_DIRECTORY_LAYOUT + ".jp.co";
	public static final String NEXTDESIGN = MAVEN_STANDARD_DIRECTORY_LAYOUT + ".jp.co.nextdesign";
	public static final String SAMPLE_SUB_DOMAIN = NEXTDESIGN + ".domain.store";
	
	public static final String ARTIFACT_ID = "template";
	public static final String GROUP_ID = "jp.co.nextdesign";
	public static final String GROUP_ID_PATH = GROUP_ID.replaceAll("\\.", Matcher.quoteReplacement(File.separator));
	
	//GroupIdを構成するディレクトリの識別文字列。これらのディレクトリは作成しない
	protected static final String[] GROUP_DIR_PATHS = {
		JP.replaceAll("\\.", Matcher.quoteReplacement(File.separator)), 
		CO.replaceAll("\\.", Matcher.quoteReplacement(File.separator)), 
		SAMPLE_SUB_DOMAIN.replaceAll("\\.", Matcher.quoteReplacement(File.separator))
	};
	
	//これらのディレクトリとその配下のすべてのディレクトリとファイルは作成しない
	protected static final String[] IGNORED_DIIR_PATHS = {
		"target.classes.jp".replaceAll("\\.", Matcher.quoteReplacement(File.separator)),
		(NEXTDESIGN + ".domain.store").replaceAll("\\.", Matcher.quoteReplacement(File.separator))
	};
		
	private File rootFolder;
	private BdTemplateDirectory rootDirectory; //templateの場所
	private List<BdBaseCodeFile> codeTemplateFileList;
	
	/**
	 * コンストラクタ
	 * @param rootFolder
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException 
	 */
	public BdTemplateProject(File rootFolder) throws BdBuilderException, BdApplicationPropertyException {
		super();
		this.rootFolder = rootFolder;
		this.loadProject();
	}
	
	/**
	 * 1つのユーザドメインクラスをもとに各コードテンプレートをカスタマイズしたファイルのリストを生成し応答する
	 * @param c
	 * @return
	 * @throws BdBuilderException 
	 */
	public List<BdGeneratedFile> generate(BdUserClass c, String groupId) throws BdBuilderException {
		List<BdGeneratedFile> resultList = new ArrayList<BdGeneratedFile>();
		//コードテンプレートの数分繰り返す
		for(BdBaseCodeFile codeTemplateFile : this.getCodeTemplateFileList()){
			List<BdGeneratedFile> genertedFileList = codeTemplateFile.generate(c, groupId);
			if (genertedFileList != null && genertedFileList.size() > 0){
				resultList.addAll(genertedFileList);
			}
		}
		if (BdApplicationProperty.LOG_GENERATING_FILE_NAME){
			System.out.println("------------------------------------\n" + c.getName());
			for(BdGeneratedFile gf : resultList){
				System.out.println(gf.getFullPathName().replaceAll(Pattern.quote("C:\\_dev\\DDBuilderTemplate\\repos\\template\\src\\main\\java\\jp\\co\\nextdesign\\"), ""));
			}
		}
		return resultList;
	}
	
	/**
	 * ドメインモデル全体(全ドメインクラスのリスト)をもとに各コードテンプレートをカスタマイズしたファイルのリストを生成し応答する
	 * @param cList
	 * @return
	 * @throws BdBuilderException
	 */
	public List<BdGeneratedFile> generate(List<BdUserClass> cList, String groupId) throws BdBuilderException{
		List<BdGeneratedFile> resultList = new ArrayList<BdGeneratedFile>();
		if (cList != null && cList.size() > 0){
			//コードテンプレートの数分繰り返す
			for(BdBaseCodeFile codeTemplateFile : this.getCodeTemplateFileList()){
				List<BdGeneratedFile> genertedFileList = codeTemplateFile.generate(cList, groupId);
				if (genertedFileList != null && genertedFileList.size() > 0){
					resultList.addAll(genertedFileList);
				}
			}
		} else {
			//ドメインクラスが１つも定義されていない場合
			//コードテンプレートの数分繰り返す
			for(BdBaseCodeFile codeTemplateFile : this.getCodeTemplateFileList()){
				List<BdGeneratedFile> genertedFileList = codeTemplateFile.generate(groupId);
				if (genertedFileList != null && genertedFileList.size() > 0){
					resultList.addAll(genertedFileList);
				}
			}
		}
		return resultList;
	}
	
	/**
	 * 1つのサービスメソッドをもとに各コードテンプレートをカスタマイズしたファイルのリストを生成し応答する
	 * @param m
	 * @return
	 * @throws BdBuilderException 
	 */
	public List<BdGeneratedFile> generate(BdUserServiceMethod m, String groupId) throws BdBuilderException {
		List<BdGeneratedFile> resultList = new ArrayList<BdGeneratedFile>();
		//コードテンプレートの数分繰り返す
		for(BdBaseCodeFile codeTemplateFile : this.getCodeTemplateFileList()){
			List<BdGeneratedFile> genertedFileList = codeTemplateFile.generate(m, groupId);
			if (genertedFileList != null && genertedFileList.size() > 0){
				resultList.addAll(genertedFileList);
			}
		}
		if (BdApplicationProperty.LOG_GENERATING_FILE_NAME){
			System.out.println("------------------------------------\n" + m.getName());
			for(BdGeneratedFile gf : resultList){
				System.out.println(gf.getFullPathName().replaceAll(Pattern.quote("C:\\_dev\\DDBuilderTemplate\\repos\\template\\src\\main\\java\\jp\\co\\nextdesign\\"), ""));
			}
		}
		return resultList;
	}


	/**
	 * サービスメソッドをもとに各コードテンプレートをカスタマイズしたファイルのリストを生成し応答する
	 * @param mList
	 * @return
	 * @throws BdBuilderException
	 */
	public List<BdGeneratedFile> generate4Service(List<BdUserServiceMethod> mList, String groupId) throws BdBuilderException{
		List<BdGeneratedFile> resultList = new ArrayList<BdGeneratedFile>();
		if (mList != null && mList.size() > 0){
			//コードテンプレートの数分繰り返す
			for(BdBaseCodeFile codeTemplateFile : this.getCodeTemplateFileList()){
				List<BdGeneratedFile> genertedFileList = codeTemplateFile.generate4Service(mList, groupId);
				if (genertedFileList != null && genertedFileList.size() > 0){
					resultList.addAll(genertedFileList);
				}
			}
		} else {
			//何も作成しない（サービス一覧は作成済みなので）
		}
		return resultList;
	}

	/**
	 * 全てのコートテンプレートファイルを応答する
	 * @return
	 */
	public List<BdBaseCodeFile> getCodeTemplateFileList(){
		if (this.codeTemplateFileList == null){
			this.codeTemplateFileList = this.rootDirectory.getCodeTemplateFileList();
		}
		return this.codeTemplateFileList;
	}

	/**
	 * プロジェクトのルートディレクトリを応答する
	 * @return
	 */
	public BdTemplateDirectory getRootDirectory(){
		return this.rootDirectory;
	}
	
	/**
	 * プロジェクトルートのパスを応答する 
	 * @return
	 */
	public String getProjectRootFullPath(){
		return this.rootFolder.getAbsolutePath() + File.separator + ARTIFACT_ID;
	}

	/**
	 * folder/template/src/main/java/jp/co/nextdesignまでのフルパス名を応答する
	 * @return
	 */
	public String getFullPathFromFolderToGroupIdPackage(){
		return this.getProjectRootFullPath()
				+ File.separator + MAVEN_STANDARD_DIRECTORY_LAYOUT.replaceAll("\\.",  Matcher.quoteReplacement(File.separator))
				+ File.separator + GROUP_ID_PATH;
	}

	/**
	 * すべての必須ディレクトリ情報を応答する(階層レベルが浅いもの順)
	 * @return
	 */
	public List<File> getRequiredDirestories(){
		List<File> resultList = new ArrayList<File>();
		List<BdTemplateDirectory> list = this.getAllDirectoryList();
		Collections.sort(list, new BdDirectoryLevelComparator());
		for(BdTemplateDirectory dir : list){
			if (!dir.isGroupDirectory() && !dir.isIgnoredContents()){
				resultList.add(dir.getPlainDirectory());
			}
		}
		return resultList;
	}
	
	/**
	 * すべての必須ファイル情報を応答する
	 * @return
	 */
	public List<File> getRequiredFiles(){
		List<File> resultList = new ArrayList<File>();
		for(BdTemplateFile file : this.getAllFileList()){
			if (file.isRequiredFile() && !file.isIgnoredContents()){
				resultList.add(file.getPlainFile());
			}
		}
		return resultList;
	}
	
	/**
	 * テンプレートファイル：SampleViewPage.htmlファイルを応答する
	 * @return
	 * @throws BdBuilderException
	 */
	public File getSampleFileOfViewPageHtml() throws BdBuilderException{
		return getSampleFile(BdTemplateProject.VIEW_PACKAGE_NAME, "SampleViewPage.html");
	}
	
	/**
	 * 指定されたサンプルファイルを応答する
	 * @param templatePackagePath
	 * @param sampleFileName
	 * @return
	 * @throws BdBuilderException
	 */
	private File getSampleFile(String layerPackageName, String sampleFileName) throws BdBuilderException{
		String fullPath = getFullPathFromFolderToGroupIdPackage()
				+ File.separator + layerPackageName
				+ File.separator + sampleFileName;
		File result = new File(fullPath);
		if (!result.exists()){
			throw new BdBuilderException(this.getClass().getName() + "#getFile() Not Exists Template File : " + fullPath);
		}
		return result;
	}
	
	/**
	 * プロジェクト内のすべてのディレクトリリストを応答する
	 * @return
	 */
	public List<BdTemplateDirectory> getAllDirectoryList(){
		List<BdTemplateDirectory> resultList = new ArrayList<BdTemplateDirectory>();
		if (this.rootDirectory != null){
			resultList.add(rootDirectory);
			resultList.addAll(this.rootDirectory.getAllDirectoryList());
		}
		return resultList;
	}

	/**
	 * プロジェクト内のすべてのファイルリストを応答する
	 * @return
	 */
	public List<BdTemplateFile> getAllFileList(){
		List<BdTemplateFile> resultList = new ArrayList<BdTemplateFile>();
			for(BdTemplateDirectory dir : this.getAllDirectoryList()){
				resultList.addAll(dir.getFileList());
			}
		return resultList;
	}

	/**
	 * テンプレートプロジェクトを読み込む
	 * @throws BdBuilderException
	 * @throws BdApplicationPropertyException 
	 */
	private void loadProject() throws BdBuilderException, BdApplicationPropertyException {
		File dir = new File(this.rootFolder.getAbsolutePath() + File.separator + ARTIFACT_ID);
		if (dir.exists()){
			rootDirectory = new BdTemplateDirectory(dir, this);
			rootDirectory.load();
		}else{
			throw new BdBuilderException(this.getClass().getSimpleName() + "#CONSTRUCTER: CAN NOT FOUND template PROJECT.” ＋ BdConstants.CR + ”CHECK! -> template FOLDER.");
		}
	}
	
	/**
	 * デバッグ
	 * @param tab
	 */
	public void debugPrint(){
		NdLogger.getInstance().debug("*****************************************************");
		NdLogger.getInstance().debug("TEMPLATE PROJECT");
		NdLogger.getInstance().debug("*****************************************************");
		if (!NdLogger.getInstance().getDebugLogging()) return;		
		rootDirectory.debugPrint("");
	}
}
