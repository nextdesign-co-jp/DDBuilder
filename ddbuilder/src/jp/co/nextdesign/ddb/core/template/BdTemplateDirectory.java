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
import java.util.List;
import java.util.regex.Pattern;

import jp.co.nextdesign.ddb.BdApplicationPropertyException;
import jp.co.nextdesign.ddb.core.BdBuilderException;
import jp.co.nextdesign.jcr.NdConstants;
import jp.co.nextdesign.util.logging.NdLogger;

/**
 * テンプレートプロジェクトの内のディレクトリ
 * @author murayama
 *
 */
public class BdTemplateDirectory {
	private BdTemplateProject project;
	private File plainDirectory;
	private BdTemplateDirectory parentDirectory;
	private List<BdTemplateDirectory> childDirectoryList;
	private List<BdTemplateFile> fileList;
	private int directoryLevel = 0;
	
	/**
	 * コンストラクタ
	 * @param directory
	 */
	public BdTemplateDirectory(File directory, BdTemplateProject project){
		super();
		this.plainDirectory = directory;
		this.project = project;
		this.childDirectoryList = new ArrayList<BdTemplateDirectory>();
		this.fileList = new ArrayList<BdTemplateFile>();
		this.directoryLevel = this.plainDirectory.getAbsolutePath().split(Pattern.quote(File.separator)).length;
	}
	
	/**
	 * 配下の全コートテンプレートファイルを応答する
	 * @return
	 */
	public List<BdBaseCodeFile> getCodeTemplateFileList(){
		List<BdBaseCodeFile> resultList = new ArrayList<BdBaseCodeFile>();
		for(BdTemplateFile file : this.getFileList()){
			if (file instanceof BdBaseCodeFile){
				resultList.add((BdBaseCodeFile)file);
			}
		}
		for(BdTemplateDirectory child : this.childDirectoryList){
			resultList.addAll(child.getCodeTemplateFileList());
		}
		return resultList;
	}

	/**
	 * ディレクトリ階層構造の何階層目かを応答する
	 * @return
	 */
	public int getDirectoryLevel() {
		return directoryLevel;
	}

	/**
	 * GROUP_IDを構成するディレクトリか否か
	 * @return
	 */
	public boolean isGroupDirectory(){
		boolean result = false;
		for(String path : BdTemplateProject.GROUP_DIR_PATHS){
			if (this.plainDirectory.getAbsolutePath().endsWith(path)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 作成しないディレクトリか否か
	 * @return
	 */
	public boolean isIgnoredContents(){
		boolean result = false;
		for(String path : BdTemplateProject.IGNORED_DIIR_PATHS){
			if (this.plainDirectory.getAbsolutePath().contains(path)){
				result = true;
				break;
			}
		}
		return result;
	}
	
	/**
	 * 配下のすべてのディレクトリを応答する
	 * @return
	 */
	public List<BdTemplateDirectory> getAllDirectoryList(){
		List<BdTemplateDirectory> resultList = new ArrayList<BdTemplateDirectory>(this.childDirectoryList);
		for(BdTemplateDirectory dir : this.childDirectoryList){
			resultList.addAll(dir.getAllDirectoryList());
		}
		return resultList;
	}
	
	/**
	 * 配下のディレクトリとファイルを読み込む
	 * @throws BdApplicationPropertyException 
	 * @throws BdBuilderException 
	 */
	public void load() throws BdBuilderException, BdApplicationPropertyException {
		for (File f : this.plainDirectory.listFiles()) {
			if (f.isFile()) {
				BdTemplateFile newFile = BdTemplateFileFactory.create(f, this.project);
				if (newFile instanceof BdBaseCodeFile){
					((BdBaseCodeFile)newFile).buildCodeBlockList();
				}
				this.addFile(newFile);
			} else if (f.isDirectory()) {
				if (!isEclipseTargetDirectory(f)){
					BdTemplateDirectory childDirectory = new BdTemplateDirectory(f, this.project);
					this.addChildDirectory(childDirectory);
					childDirectory.load();
				}
			}
		}
	}
	
	/**
	 * テンプレートEclipseプロジェクトののtargetディレクトリか否か
	 * @param f
	 * @return
	 */
	private boolean isEclipseTargetDirectory(File f){
		boolean result = false;
		if (f != null && f.isDirectory()){
			result = f.getPath().toLowerCase().equals(this.project.getProjectRootFullPath().toLowerCase() + File.separator + "target");
		}
		return result;
	}
	
	/**
	 * 直下のファイルを追加する
	 * @param file
	 */
	public void addFile(BdTemplateFile file){
		if (file != null && !this.fileList.contains(file)){
			this.fileList.add(file);
			file.setDirectory(this);
		}
	}
	
	/**
	 * 直下のファイルを応答する
	 * @return
	 */
	public List<BdTemplateFile> getFileList(){
		return this.fileList;
	}
	
	/**
	 * 直下の子ディレクトリを追加する
	 * @param child
	 */
	public void addChildDirectory(BdTemplateDirectory child){
		if (child != null && !this.childDirectoryList.contains(child)){
			this.childDirectoryList.add(child);
			child.setParentDirectory(this);
		}
	}
	
	/**
	 * 親ディレクトリを設定する
	 * @param parent
	 */
	public void setParentDirectory(BdTemplateDirectory parent){
		if (parent != null && this.parentDirectory != parent){
			this.parentDirectory = parent;
			parent.addChildDirectory(this);
		}
	}
	
	/**
	 * System.io.Fileを応答する
	 * @return
	 */
	public File getPlainDirectory(){
		return this.plainDirectory;
	}
	
	/**
	 * デバッグ
	 * @param tab
	 */
	public void debugPrint(String tab){
		if (!NdLogger.getInstance().getDebugLogging()) return;
		String line = tab  + this.plainDirectory.getName() + "  [" + this.getClass().getSimpleName() + "]";
		line += this.isIgnoredContents() ? "....IGNORED" : "";
		line += this.isGroupDirectory() ? "....GROUPDIR" : "";
		NdLogger.getInstance().debug(line);
		for(BdTemplateFile f : this.fileList){
			f.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
		for(BdTemplateDirectory dir : this.childDirectoryList){
			dir.debugPrint(tab + NdConstants.DEBUG_TAB);
		}
	}
}
